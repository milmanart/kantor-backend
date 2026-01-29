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
public class NbpTable {
    private String table;
    private String no;
    private String effectiveDate;
    private List<NbpTableRate> rates;
}
