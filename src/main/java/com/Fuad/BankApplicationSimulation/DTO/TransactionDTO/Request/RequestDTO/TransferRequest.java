package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.Request.RequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
