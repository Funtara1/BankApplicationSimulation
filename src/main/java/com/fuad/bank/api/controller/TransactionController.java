package com.fuad.bank.api.controller;

import com.fuad.bank.api.dto.TransactionDTO.RequestDTO.DepositRequest;
import com.fuad.bank.api.dto.TransactionDTO.RequestDTO.TransactionFilterRequest;
import com.fuad.bank.api.dto.TransactionDTO.RequestDTO.TransferRequest;
import com.fuad.bank.api.dto.TransactionDTO.RequestDTO.WithdrawRequest;
import com.fuad.bank.api.dto.TransactionDTO.ResponseDTO.TransactionResponse;
import com.fuad.bank.domain.entity.Transaction;
import com.fuad.bank.api.mapper.TransactionMapper;
import com.fuad.bank.application.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @Valid @RequestBody DepositRequest request) {

        Transaction tx = transactionService.deposit(
                request.getToAccountId(),
                request.getAmount()
        );

        return ResponseEntity.ok(transactionMapper.toResponse(tx));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @Valid @RequestBody WithdrawRequest request) {

        Transaction tx = transactionService.withdraw(
                request.getFromAccountId(),
                request.getAmount()
        );

        return ResponseEntity.ok(transactionMapper.toResponse(tx));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request) {

        Transaction tx = transactionService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );

        return ResponseEntity.ok(transactionMapper.toResponse(tx));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @PathVariable Long accountId) {

        List<TransactionResponse> responses =
                transactionService.getTransactionsByAccountId(accountId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        Transaction tx = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transactionMapper.toResponse(tx));
    }

    @GetMapping
    public Page<TransactionResponse> filter(
            @ParameterObject TransactionFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return transactionService.filter(filter, page, size);
    }
}
