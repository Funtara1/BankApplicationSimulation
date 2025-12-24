package com.fuad.bank.api.mapper;

import com.fuad.bank.api.dto.AccountDTO.RequestDTO.CreateAccountRequest;
import com.fuad.bank.api.dto.AccountDTO.ResponseDTO.AccountResponse;
import com.fuad.bank.domain.entity.Account;
import com.fuad.bank.domain.entity.Customer;
import com.fuad.bank.domain.enums.AccountStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountMapper {

    public Account toEntity(Customer customer, CreateAccountRequest request) {
        Account account = new Account();
        account.setCustomer(customer);
        account.setCurrency(request.getCurrency());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountStatus(AccountStatus.OPEN);
        return account;
    }

    public AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }

        AccountResponse dto = new AccountResponse();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setStatus(account.getAccountStatus());
        return dto;
    }
}

