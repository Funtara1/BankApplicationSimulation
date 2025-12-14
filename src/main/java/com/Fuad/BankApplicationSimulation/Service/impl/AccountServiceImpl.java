package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import com.Fuad.BankApplicationSimulation.Exception.InvalidOperationException;
import com.Fuad.BankApplicationSimulation.Exception.NotFoundException;
import com.Fuad.BankApplicationSimulation.Repository.AccountRepository;
import com.Fuad.BankApplicationSimulation.Repository.CustomerRepository;
import com.Fuad.BankApplicationSimulation.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Account createForCustomer(String fin, CreateAccountRequest request) {
        Customer customer = customerRepository.findByFinIgnoreCase(fin)
                .orElseThrow(() -> new NotFoundException("Customer not found")); // TODO: fix this to duplicate

        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException("Customer is closed");
        }

        Account account = new Account();
        account.setCustomer(customer);
        account.setCurrency(request.getCurrency());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountStatus(AccountStatus.OPEN);

        account = accountRepository.save(account);

        String accountNumber = generateAccountNumber(account);
        account.setAccountNumber(accountNumber);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account closeAccount(Long accountId) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new InvalidOperationException("Account is already closed");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidOperationException("Account balance must be zero to close");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private String generateAccountNumber(Account account) {
        String currency = account.getCurrency().name();
        String paddedId = String.format("%08d", account.getId());
        return currency + paddedId;
    }
}
