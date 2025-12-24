package com.fuad.bank.api.mapper;


import com.fuad.bank.api.dto.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.fuad.bank.api.dto.CustomerDTO.ResponseDTO.CustomerResponse;
import com.fuad.bank.domain.entity.Customer;
import com.fuad.bank.domain.enums.CustomerStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CreateCustomerRequest dto) {
        return Customer.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .fin(dto.getFin())
                .status(CustomerStatus.ACTIVE)
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerResponse dto = new CustomerResponse();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setSurname(customer.getSurname());
        dto.setFin(customer.getFin());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus());
        return dto;
    }
}

