package com.fuad.bank.application.service;

import com.fuad.bank.api.dto.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.fuad.bank.api.dto.CustomerDTO.RequestDTO.CustomerFilterRequest;
import com.fuad.bank.api.dto.CustomerDTO.ResponseDTO.CustomerResponse;
import com.fuad.bank.domain.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    Customer getCustomerById(Long id);

    Customer createCustomer(CreateCustomerRequest createCustomerRequest);

    Customer updateCustomerById(Long id, CreateCustomerRequest createCustomerRequest);

    Customer closeCustomerById(Long id);

    List<Customer> getAllCustomers();

    Page<CustomerResponse> filter(
            CustomerFilterRequest filter,
            int page,
            int size
    );
}
