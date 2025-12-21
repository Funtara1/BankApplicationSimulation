package com.fuad.bank.api.dto.AccountDTO.RequestDTO;

import com.fuad.bank.domain.enums.AccountStatus;
import com.fuad.bank.domain.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountFilterRequest {

    private Currency currency;
    private AccountStatus status;
    private Long customerId;
}
