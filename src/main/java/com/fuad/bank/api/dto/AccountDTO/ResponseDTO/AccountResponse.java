package com.fuad.bank.api.dto.AccountDTO.ResponseDTO;

import com.fuad.bank.domain.enums.AccountStatus;
import com.fuad.bank.domain.enums.Currency;
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
