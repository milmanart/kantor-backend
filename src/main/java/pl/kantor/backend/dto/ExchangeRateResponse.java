package pl.kantor.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private String currency;
    private String code;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal mid;
    private LocalDate effectiveDate;
}
