package pl.kantor.backend.dto.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpRate {
    private String no;
    private String effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal mid;
}
