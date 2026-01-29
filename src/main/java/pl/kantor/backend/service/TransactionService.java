package pl.kantor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantor.backend.dto.ExchangeRequest;
import pl.kantor.backend.dto.TransactionResponse;
import pl.kantor.backend.entity.Transaction;
import pl.kantor.backend.entity.TransactionType;
import pl.kantor.backend.entity.User;
import pl.kantor.backend.entity.Wallet;
import pl.kantor.backend.exception.BadRequestException;
import pl.kantor.backend.exception.InsufficientFundsException;
import pl.kantor.backend.exception.ResourceNotFoundException;
import pl.kantor.backend.repository.TransactionRepository;
import pl.kantor.backend.repository.WalletRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final NbpService nbpService;

    @Transactional
    public TransactionResponse exchange(User user, ExchangeRequest request) {
        String fromCurrency = request.getFromCurrency().toUpperCase();
        String toCurrency = request.getToCurrency().toUpperCase();
        BigDecimal amount = request.getAmount();

        if (fromCurrency.equals(toCurrency)) {
            throw new BadRequestException("Source and target currencies must be different");
        }

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        var fromBalance = wallet.getBalance(fromCurrency);
        if (fromBalance == null || fromBalance.getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in " + fromCurrency);
        }

        BigDecimal exchangeRate;
        BigDecimal toAmount;

        if ("PLN".equals(fromCurrency)) {
            BigDecimal askRate = nbpService.getBuyRate(toCurrency);
            exchangeRate = BigDecimal.ONE.divide(askRate, 6, RoundingMode.HALF_UP);
            toAmount = amount.divide(askRate, 2, RoundingMode.HALF_UP);
        } else if ("PLN".equals(toCurrency)) {
            BigDecimal bidRate = nbpService.getSellRate(fromCurrency);
            exchangeRate = bidRate;
            toAmount = amount.multiply(bidRate).setScale(2, RoundingMode.HALF_UP);
        } else {
            BigDecimal fromToPlnRate = nbpService.getSellRate(fromCurrency);
            BigDecimal plnToToRate = nbpService.getBuyRate(toCurrency);
            exchangeRate = fromToPlnRate.divide(plnToToRate, 6, RoundingMode.HALF_UP);
            BigDecimal plnAmount = amount.multiply(fromToPlnRate);
            toAmount = plnAmount.divide(plnToToRate, 2, RoundingMode.HALF_UP);
        }

        wallet.subtractBalance(fromCurrency, amount);
        wallet.addBalance(toCurrency, toAmount);
        walletRepository.save(wallet);

        TransactionType type = "PLN".equals(fromCurrency) ? TransactionType.BUY : TransactionType.SELL;

        Transaction transaction = Transaction.builder()
                .user(user)
                .type(type)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .fromAmount(amount)
                .toAmount(toAmount)
                .exchangeRate(exchangeRate)
                .build();

        transaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(transaction);
    }

    public List<TransactionResponse> getTransactionHistory(User user) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    public Page<TransactionResponse> getTransactionHistory(User user, Pageable pageable) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(this::mapToTransactionResponse);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .fromCurrency(transaction.getFromCurrency())
                .toCurrency(transaction.getToCurrency())
                .fromAmount(transaction.getFromAmount())
                .toAmount(transaction.getToAmount())
                .exchangeRate(transaction.getExchangeRate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
