package com.fuad.bank.api.dto.CustomerDTO.ResponseDTO;

import com.fuad.bank.domain.enums.CustomerStatus;
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
