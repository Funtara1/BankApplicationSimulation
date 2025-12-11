package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO.TransferRequest;
import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO.DepositRequest;
import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO.WithdrawRequest;
import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.ResponseDTO.TransactionResponse;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import com.Fuad.BankApplicationSimulation.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody DepositRequest request) {
        Transaction result = transactionService.deposit(
                request.getToAccountId(),
                request.getAmount()
        );
        return ResponseEntity.ok(result);
    }


    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody WithdrawRequest request) {
        Transaction result = transactionService.withdraw(
                request.getFromAccountId(),
                request.getAmount()
        );
        return ResponseEntity.ok(result);
    }


    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest request) {
        Transaction result = transactionService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );
        return ResponseEntity.ok(result);
    }


    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByAccountId(accountId)
        );
    }



    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                transactionService.getTransactionById(id)
        );
    }
}
