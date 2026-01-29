package pl.kantor.backend.dto.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpExchangeRateTable {
    private String table;
    private String currency;
    private String code;
    private List<NbpRate> rates;
}
