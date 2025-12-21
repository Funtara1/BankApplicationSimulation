package com.fuad.bank.api.dto.TransactionDTO.RequestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {

    @NotNull
    private Long fromAccountId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
