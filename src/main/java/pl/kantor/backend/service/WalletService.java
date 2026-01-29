package pl.kantor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantor.backend.dto.BalanceResponse;
import pl.kantor.backend.dto.DepositRequest;
import pl.kantor.backend.dto.WalletResponse;
import pl.kantor.backend.entity.Transaction;
import pl.kantor.backend.entity.TransactionType;
import pl.kantor.backend.entity.User;
import pl.kantor.backend.entity.Wallet;
import pl.kantor.backend.exception.ResourceNotFoundException;
import pl.kantor.backend.repository.TransactionRepository;
import pl.kantor.backend.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletResponse getWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        return mapToWalletResponse(wallet);
    }

    @Transactional
    public WalletResponse deposit(User user, DepositRequest request) {
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        String currencyCode = request.getCurrencyCode().toUpperCase();
        wallet.addBalance(currencyCode, request.getAmount());
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .user(user)
                .type(TransactionType.DEPOSIT)
                .fromCurrency(currencyCode)
                .toCurrency(currencyCode)
                .fromAmount(request.getAmount())
                .toAmount(request.getAmount())
                .exchangeRate(BigDecimal.ONE)
                .build();
        transactionRepository.save(transaction);

        return mapToWalletResponse(wallet);
    }

    public BigDecimal getBalance(User user, String currencyCode) {
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        var balance = wallet.getBalance(currencyCode.toUpperCase());
        return balance != null ? balance.getAmount() : BigDecimal.ZERO;
    }

    private WalletResponse mapToWalletResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .balances(wallet.getBalances().stream()
                        .map(balance -> BalanceResponse.builder()
                                .currencyCode(balance.getCurrencyCode())
                                .amount(balance.getAmount())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
