package pl.kantor.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantor.backend.dto.ExchangeRateResponse;
import pl.kantor.backend.service.NbpService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final NbpService nbpService;

    @GetMapping
    public ResponseEntity<List<ExchangeRateResponse>> getCurrentRates() {
        return ResponseEntity.ok(nbpService.getCurrentRates());
    }

    @GetMapping("/{code}")
    public ResponseEntity<ExchangeRateResponse> getRateForCurrency(@PathVariable String code) {
        return ResponseEntity.ok(nbpService.getRateForCurrency(code.toUpperCase()));
    }

    @GetMapping("/{code}/history")
    public ResponseEntity<List<ExchangeRateResponse>> getHistoricalRates(
            @PathVariable String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(nbpService.getHistoricalRates(code.toUpperCase(), startDate, endDate));
    }
}
