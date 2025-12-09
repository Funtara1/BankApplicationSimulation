package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequest {
    private Long toAccountId;
    private BigDecimal amount;
}
