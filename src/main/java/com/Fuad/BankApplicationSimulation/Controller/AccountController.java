package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.ResponseDTO.AccountResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Mapper.AccountMapper;
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
    private final AccountMapper accountMapper;

    @PostMapping("/customer/{fin}")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable String fin,
            @Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createForCustomer(fin, request);
        AccountResponse response = accountMapper.toResponse(account);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponse> responses = new ArrayList<>();

        //TODO mojno ispolzovat stream
        for (Account account : accounts) {
            responses.add(accountMapper.toResponse(account));
        }

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable Long id) {
        Account account = accountService.closeAccount(id);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
}
