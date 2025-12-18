package com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO;

import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerFilterRequest {

    private String name;
    private String surname;
    private String fin;
    private CustomerStatus status;
}
