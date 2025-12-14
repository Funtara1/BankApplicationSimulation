package com.Fuad.BankApplicationSimulation.DTO.AccountDTO.ResponseDTO;

import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@Setter
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private AccountStatus status;

}
