package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Repository.CustomerRepository;
import com.Fuad.BankApplicationSimulation.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {

        // Проверяем: существует ли клиент
        if (customerRepository.findByFinIgnoreCase(createCustomerRequest.getFin()).isPresent()) {
            throw new RuntimeException("Customer with FIN '" + createCustomerRequest.getFin() + "' already exists!");
        }

        Customer customer = Customer.builder()
                .name(createCustomerRequest.getName())
                .surname(createCustomerRequest.getSurname())
                .phoneNumber(createCustomerRequest.getPhoneNumber())
                .address(createCustomerRequest.getAddress())
                .fin(createCustomerRequest.getFin())
                .build();

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomerById(Long id, CreateCustomerRequest createCustomerRequest) {
        Customer existing = getCustomerById(id);
        existing.setName(createCustomerRequest.getName());
        existing.setSurname(createCustomerRequest.getSurname());
        existing.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        existing.setAddress(createCustomerRequest.getAddress());
        existing.setFin(createCustomerRequest.getFin());

        return customerRepository.save(existing);
    }

    @Override
    public String deleteCustomerById(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);

        return "Customer " + customer.getName() + " has been deleted";
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
