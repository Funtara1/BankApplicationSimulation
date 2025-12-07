    package com.Fuad.BankApplicationSimulation.Service;

    import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
    import com.Fuad.BankApplicationSimulation.Entity.Customer;

    import java.util.List;

    public interface CustomerService {
        Customer getCustomerById(Long id);
        Customer createFromDTO(CreateCustomerRequest createCustomerRequest);
        Customer updateFromDTO(Long id, CreateCustomerRequest createCustomerRequest);
        String delete(Long id);
        List<Customer> getAllCustomers();
    }

