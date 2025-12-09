package com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO;

import com.Fuad.BankApplicationSimulation.Enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Getter
@Setter
public class CreateAccountRequest {

    @NotNull
    private Currency currency = Currency.AZN;

}
