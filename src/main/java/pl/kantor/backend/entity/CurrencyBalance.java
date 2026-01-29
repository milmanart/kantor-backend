package pl.kantor.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "currency_balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false, length = 3)
    private String currencyCode;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
}
