package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.ResponseDTO.AccountResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/customer/{fin}")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable String fin,
            @Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createForCustomer(fin, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);
        return ResponseEntity.ok(toResponse(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {
            responses.add(toResponse(account));
        }

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable Long id) {
        Account account = accountService.closeAccount(id);
        return ResponseEntity.ok(toResponse(account));
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse dto = new AccountResponse();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setStatus(account.getAccountStatus());
        return dto;
    }
}
