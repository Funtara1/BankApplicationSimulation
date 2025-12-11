package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.Request.ResponseDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private LocalDateTime date;
    private String type;
    private BigDecimal amount;

    private BigDecimal oldBalance;
    private BigDecimal newBalance;

    private String fromAccount;
    private String toAccount;
}
