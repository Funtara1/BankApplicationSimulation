package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
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

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createForCustomer(String fin, CreateAccountRequest request) {

        Customer customer = customerRepository.findByFinIgnoreCase(fin)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Создаём Account из DTO
        Account account = new Account();
        account.setCurrency(request.getCurrency());
        account.setAccountStatus(AccountStatus.OPEN);
        account.setBalance(BigDecimal.ZERO);

        // Связываем аккаунт с клиентом
        customer.getAccounts().add(account);
        account.setCustomer(customer);

        // Сохраняем каскадно
        customerRepository.save(customer);

        return account;
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
