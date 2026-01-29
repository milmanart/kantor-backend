package pl.kantor.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.kantor.backend.dto.DepositRequest;
import pl.kantor.backend.dto.WalletResponse;
import pl.kantor.backend.entity.User;
import pl.kantor.backend.service.WalletService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(walletService.getWallet(user));
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponse> deposit(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(user, request));
    }

    @GetMapping("/balance/{currencyCode}")
    public ResponseEntity<BigDecimal> getBalance(
            @AuthenticationPrincipal User user,
            @PathVariable String currencyCode) {
        return ResponseEntity.ok(walletService.getBalance(user, currencyCode));
    }
}
