package com.fuad.bank.api.dto.CustomerDTO.RequestDTO;

import com.fuad.bank.domain.enums.CustomerStatus;
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
