package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.Currency;
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
        // 1. Находим клиента
        Customer customer = customerRepository.findByFinIgnoreCase(fin)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Создаём аккаунт и связываем с клиентом
        Account account = new Account();
        account.setCurrency(request.getCurrency());
        account.setAccountStatus(AccountStatus.OPEN);
        account.setBalance(BigDecimal.ZERO);
        account.setCustomer(customer);

        // Добавляем в список аккаунтов клиента для каскадного сохранения
        customer.getAccounts().add(account);

        // 3. Сохраняем аккаунт (или клиента, если каскад настроен)
        accountRepository.save(account); // теперь account.getId() != null

        // 4. Генерируем accountNumber на основе id и валюты
        String generatedNumber = generateAccountNumber(account.getCurrency(), account.getId());
        account.setAccountNumber(generatedNumber);

        // 5. Сохраняем снова, чтобы записать accountNumber
        return accountRepository.save(account);
    }

    private String generateAccountNumber(Currency currency, Long id) {
        String paddedId = String.format("%08d", id); // 8 цифр с ведущими нулями
        return currency.name() + paddedId;
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
