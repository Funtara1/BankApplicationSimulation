package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {
    private Long fromAccountId;
    private BigDecimal amount;
}
