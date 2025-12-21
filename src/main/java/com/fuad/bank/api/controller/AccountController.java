package com.fuad.bank.api.controller;

import com.fuad.bank.api.dto.AccountDTO.RequestDTO.AccountFilterRequest;
import com.fuad.bank.api.dto.AccountDTO.RequestDTO.CreateAccountRequest;
import com.fuad.bank.api.dto.AccountDTO.ResponseDTO.AccountResponse;
import com.fuad.bank.domain.entity.Account;
import com.fuad.bank.api.mapper.AccountMapper;
import com.fuad.bank.application.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //TODO mojno udalit tak kak est filter metod
//    @GetMapping
//    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
//        List<Account> accounts = accountService.getAllAccounts();
//        List<AccountResponse> responses = new ArrayList<>();
//
//        //TODO mojno ispolzovat stream
//        for (Account account : accounts) {
//            responses.add(accountMapper.toResponse(account));
//        }
//
//        return ResponseEntity.ok(responses);
//    }

    @PostMapping("/{id}/close")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable Long id) {
        Account account = accountService.closeAccount(id);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }

    @GetMapping
    public Page<AccountResponse> filter(
            @ParameterObject AccountFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return accountService.filter(filter, page, size);
    }

}
