package com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO;

import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountFilterRequest {

    private Currency currency;
    private AccountStatus status;
    private Long customerId;
}
