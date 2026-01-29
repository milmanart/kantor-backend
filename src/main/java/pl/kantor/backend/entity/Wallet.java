package pl.kantor.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CurrencyBalance> balances = new ArrayList<>();

    public CurrencyBalance getBalance(String currencyCode) {
        return balances.stream()
                .filter(b -> b.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .orElse(null);
    }

    public void addBalance(String currencyCode, BigDecimal amount) {
        CurrencyBalance balance = getBalance(currencyCode);
        if (balance == null) {
            balance = CurrencyBalance.builder()
                    .wallet(this)
                    .currencyCode(currencyCode)
                    .amount(amount.setScale(2, RoundingMode.HALF_UP))
                    .build();
            balances.add(balance);
        } else {
            balance.setAmount(balance.getAmount().add(amount).setScale(2, RoundingMode.HALF_UP));
        }
    }

    public boolean subtractBalance(String currencyCode, BigDecimal amount) {
        CurrencyBalance balance = getBalance(currencyCode);
        if (balance == null || balance.getAmount().compareTo(amount) < 0) {
            return false;
        }
        balance.setAmount(balance.getAmount().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        return true;
    }
}
