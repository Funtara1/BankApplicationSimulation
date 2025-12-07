package com.Fuad.BankApplicationSimulation.DTO.AccountDTO.RequestDTO;

import com.Fuad.BankApplicationSimulation.Enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank
    private Currency currency = Currency.AZN;

}
