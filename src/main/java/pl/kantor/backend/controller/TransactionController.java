package pl.kantor.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.kantor.backend.dto.ExchangeRequest;
import pl.kantor.backend.dto.TransactionResponse;
import pl.kantor.backend.entity.User;
import pl.kantor.backend.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/exchange")
    public ResponseEntity<TransactionResponse> exchange(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ExchangeRequest request) {
        return ResponseEntity.ok(transactionService.exchange(user, request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(user));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<TransactionResponse>> getTransactionHistoryPaged(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(user, pageable));
    }
}
