package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequest {
    private Long toAccountId;
    private BigDecimal amount;
}
