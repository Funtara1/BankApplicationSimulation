package com.Fuad.BankApplicationSimulation.Mapper;


import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.ResponseDTO.CustomerResponse;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
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

    public void updateEntity(Customer customer, CreateCustomerRequest dto) {
        customer.setName(dto.getName());
        customer.setSurname(dto.getSurname());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());
        //customer.setFin(dto.getFin());  udalil logiku izmeneniya fin
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

