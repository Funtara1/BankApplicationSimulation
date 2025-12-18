package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.AccountFilterRequest;
import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.ResponseDTO.AccountResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import com.Fuad.BankApplicationSimulation.Exception.InvalidOperationException;
import com.Fuad.BankApplicationSimulation.Exception.NotFoundException;
import com.Fuad.BankApplicationSimulation.Exception.ValidationException;
import com.Fuad.BankApplicationSimulation.Mapper.AccountMapper;
import com.Fuad.BankApplicationSimulation.Repository.AccountRepository;
import com.Fuad.BankApplicationSimulation.Repository.CustomerRepository;
import com.Fuad.BankApplicationSimulation.Service.AccountService;
import com.Fuad.BankApplicationSimulation.Specification.AccountSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public Account createForCustomer(String fin, CreateAccountRequest request) {

        Customer customer = customerRepository.findByFinIgnoreCase(fin)
                .orElseThrow(() -> new NotFoundException(
                        "Customer not found",
                        Map.of("fin", fin)
                ));

        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Cannot create account for closed customer",
                    Map.of("customerStatus", customer.getStatus())
            );
        }

        if (request.getCurrency() == null) {
            throw new ValidationException(
                    "Currency must be provided",
                    Map.of("fin", fin)
            );
        }

        Account account = accountMapper.toEntity(customer, request);

        account = accountRepository.save(account);

        String accountNumber = generateAccountNumber(account);
        account.setAccountNumber(accountNumber);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account closeAccount(Long accountId) {

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new NotFoundException(
                        "Account not found",
                        Map.of("accountId", accountId)
                ));

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Account is already closed",
                    Map.of("accountId", accountId)
            );
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException(
                    "Account balance must be zero to close",
                    Map.of(
                            "accountId", accountId,
                            "balance", account.getBalance()
                    )
            );
        }

        accountMapper.close(account);
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> filter(AccountFilterRequest filter, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Account> specification =
                Specification.where(AccountSpecification.currencyEquals(filter.getCurrency()))
                        .and(AccountSpecification.statusEquals(filter.getStatus()))
                        .and(AccountSpecification.customerEquals(filter.getCustomerId()));

        Page<Account> accounts = accountRepository.findAll(specification, pageable);

        List<AccountResponse> responses = new ArrayList<>();
        for (Account account : accounts.getContent()) {
            responses.add(accountMapper.toResponse(account));
        }

        return new PageImpl<>(
                responses,
                pageable,
                accounts.getTotalElements()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Account not found",
                        Map.of("accountId", id)
                ));
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
