package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Создать аккаунт для конкретного клиента
    @PostMapping("/customer/{fin}")
    public ResponseEntity<Account> createAccount(
            @PathVariable String fin,
            @Valid @RequestBody CreateAccountRequest createAccountRequest) {
        Account createdAccount = accountService.createForCustomer(fin, createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
