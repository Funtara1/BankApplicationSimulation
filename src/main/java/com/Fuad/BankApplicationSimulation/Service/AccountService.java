package com.Fuad.BankApplicationSimulation.Service;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.AccountFilterRequest;
import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.ResponseDTO.AccountResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
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
