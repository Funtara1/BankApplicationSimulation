package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.RequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {
    private Long fromAccountId;
    private BigDecimal amount;
}
