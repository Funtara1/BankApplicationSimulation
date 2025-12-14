package com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.ResponseDTO;

import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomerResponse {

    private Long id;
    private String name;
    private String surname;
    private String fin;
    private String phoneNumber;
    private String address;
    private CustomerStatus status;
}
