package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO;

import com.Fuad.BankApplicationSimulation.Enums.TransactionStatus;
import com.Fuad.BankApplicationSimulation.Enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionFilterRequest {

    private Long accountId;
    private TransactionType type;
    private TransactionStatus status;
}
