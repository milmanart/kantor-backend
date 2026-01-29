package pl.kantor.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kantor.backend.dto.ExchangeRateResponse;
import pl.kantor.backend.dto.nbp.NbpExchangeRateTable;
import pl.kantor.backend.dto.nbp.NbpTable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NbpService {

    private final WebClient nbpWebClient;

    public List<ExchangeRateResponse> getCurrentRates() {
        NbpTable[] tables = nbpWebClient.get()
                .uri("/exchangerates/tables/C?format=json")
                .retrieve()
                .bodyToMono(NbpTable[].class)
                .block();

        if (tables == null || tables.length == 0) {
            return List.of();
        }

        NbpTable table = tables[0];
        return table.getRates().stream()
                .map(rate -> ExchangeRateResponse.builder()
                        .currency(rate.getCurrency())
                        .code(rate.getCode())
                        .bid(rate.getBid())
                        .ask(rate.getAsk())
                        .mid(rate.getBid().add(rate.getAsk()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP))
                        .effectiveDate(LocalDate.parse(table.getEffectiveDate()))
                        .build())
                .collect(Collectors.toList());
    }

    public ExchangeRateResponse getRateForCurrency(String currencyCode) {
        NbpExchangeRateTable table = nbpWebClient.get()
                .uri("/exchangerates/rates/C/{code}?format=json", currencyCode)
                .retrieve()
                .bodyToMono(NbpExchangeRateTable.class)
                .block();

        if (table == null || table.getRates() == null || table.getRates().isEmpty()) {
            throw new RuntimeException("Exchange rate not found for currency: " + currencyCode);
        }

        var rate = table.getRates().get(0);
        return ExchangeRateResponse.builder()
                .currency(table.getCurrency())
                .code(table.getCode())
                .bid(rate.getBid())
                .ask(rate.getAsk())
                .mid(rate.getBid().add(rate.getAsk()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP))
                .effectiveDate(LocalDate.parse(rate.getEffectiveDate()))
                .build();
    }

    public List<ExchangeRateResponse> getHistoricalRates(String currencyCode, LocalDate startDate, LocalDate endDate) {
        NbpExchangeRateTable table = nbpWebClient.get()
                .uri("/exchangerates/rates/C/{code}/{startDate}/{endDate}?format=json",
                        currencyCode, startDate, endDate)
                .retrieve()
                .bodyToMono(NbpExchangeRateTable.class)
                .block();

        if (table == null || table.getRates() == null) {
            return List.of();
        }

        return table.getRates().stream()
                .map(rate -> ExchangeRateResponse.builder()
                        .currency(table.getCurrency())
                        .code(table.getCode())
                        .bid(rate.getBid())
                        .ask(rate.getAsk())
                        .mid(rate.getBid().add(rate.getAsk()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP))
                        .effectiveDate(LocalDate.parse(rate.getEffectiveDate()))
                        .build())
                .collect(Collectors.toList());
    }

    public BigDecimal getBuyRate(String currencyCode) {
        if ("PLN".equals(currencyCode)) {
            return BigDecimal.ONE;
        }
        ExchangeRateResponse rate = getRateForCurrency(currencyCode);
        return rate.getAsk();
    }

    public BigDecimal getSellRate(String currencyCode) {
        if ("PLN".equals(currencyCode)) {
            return BigDecimal.ONE;
        }
        ExchangeRateResponse rate = getRateForCurrency(currencyCode);
        return rate.getBid();
    }
}
