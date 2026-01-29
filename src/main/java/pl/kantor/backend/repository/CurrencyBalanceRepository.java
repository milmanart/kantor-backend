package pl.kantor.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantor.backend.entity.CurrencyBalance;

import java.util.Optional;

@Repository
public interface CurrencyBalanceRepository extends JpaRepository<CurrencyBalance, Long> {
    Optional<CurrencyBalance> findByWalletIdAndCurrencyCode(Long walletId, String currencyCode);
}
