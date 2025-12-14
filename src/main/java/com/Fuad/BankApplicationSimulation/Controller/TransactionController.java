package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO.DepositRequest;
import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO.TransferRequest;
import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO.WithdrawRequest;
import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.ResponseDTO.TransactionResponse;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import com.Fuad.BankApplicationSimulation.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(toResponse(
                transactionService.deposit(request.getToAccountId(), request.getAmount())
        ));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(toResponse(
                transactionService.withdraw(request.getFromAccountId(), request.getAmount())
        ));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(toResponse(
                transactionService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount())
        ));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(transactionService.getTransactionById(id)));
    }

    // TODO: Use mapper
    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .date(t.getTimestamp())
                .type(t.getTransactionType().getDescription())
                .amount(t.getAmount())
                .oldBalance(t.getOldBalance())
                .newBalance(t.getNewBalance())
                .oldToBalance(t.getOldToBalance())
                .newToBalance(t.getNewToBalance())
                .fromAccount(t.getFromAccount() != null ? t.getFromAccount().getAccountNumber() : null)
                .toAccount(t.getToAccount() != null ? t.getToAccount().getAccountNumber() : null)
                .status(t.getTransactionStatus())
                .reason(t.getErrorMessage())
                .build();
    }
}
