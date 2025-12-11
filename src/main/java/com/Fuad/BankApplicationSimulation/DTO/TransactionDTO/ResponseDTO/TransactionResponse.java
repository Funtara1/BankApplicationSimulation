package com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;

    private BigDecimal oldBalance;
    private BigDecimal newBalance;

    private String fromAccount;
    private String toAccount;
}
