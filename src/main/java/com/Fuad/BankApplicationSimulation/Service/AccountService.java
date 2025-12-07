package com.Fuad.BankApplicationSimulation.Service;

import com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO.CreateAccountRequest;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;

import java.util.List;

public interface AccountService {

    Account createForCustomer(String fin, CreateAccountRequest request);

    Account getById(Long id);

    List<Account> getAllAccounts();


    void delete(Long id);
}
