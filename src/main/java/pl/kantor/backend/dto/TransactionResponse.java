package pl.kantor.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kantor.backend.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal exchangeRate;
    private LocalDateTime createdAt;
}
