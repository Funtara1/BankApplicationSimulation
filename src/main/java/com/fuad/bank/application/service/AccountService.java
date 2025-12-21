package com.fuad.bank.application.service;

import com.fuad.bank.api.dto.AccountDTO.RequestDTO.AccountFilterRequest;
import com.fuad.bank.api.dto.AccountDTO.RequestDTO.CreateAccountRequest;
import com.fuad.bank.api.dto.AccountDTO.ResponseDTO.AccountResponse;
import com.fuad.bank.domain.entity.Account;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {
    Account createForCustomer(String fin, CreateAccountRequest request);
    Account getById(Long id);
    List<Account> getAllAccounts();
    Account closeAccount(Long accountId);

    Page<AccountResponse> filter(
            AccountFilterRequest filter,
            int page,
            int size
    );
}
