package com.Fuad.BankApplicationSimulation.Service;

import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CustomerFilterRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.ResponseDTO.CustomerResponse;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
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
