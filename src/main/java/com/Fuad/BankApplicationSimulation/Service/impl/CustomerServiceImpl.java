package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import com.Fuad.BankApplicationSimulation.Exception.BusinessException;
import com.Fuad.BankApplicationSimulation.Exception.InvalidOperationException;
import com.Fuad.BankApplicationSimulation.Exception.NotFoundException;
import com.Fuad.BankApplicationSimulation.Repository.CustomerRepository;
import com.Fuad.BankApplicationSimulation.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    @Override
    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByFinIgnoreCase(request.getFin())) {
            throw new BusinessException("Customer with this FIN already exists");
        }

        // TODO: MAPPER
        Customer customer = Customer.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .fin(request.getFin())
                .status(CustomerStatus.ACTIVE)
                .build();

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer updateCustomerById(Long id, CreateCustomerRequest request) {
        Customer customer = getCustomerById(id);

        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException("Customer is closed");
        }

        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());
        customer.setFin(request.getFin());

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer closeCustomerById(Long id) {
        Customer customer = getCustomerById(id);

        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException("Customer already closed");
        }

        List<Account> accounts = customer.getAccounts();

        // TODO: OPTIMIZATION dva for obyedenit
        for (Account account : accounts) {
            if (account.getAccountStatus() == AccountStatus.OPEN) {
                if (account.getBalance() != null && account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                    throw new InvalidOperationException("Customer has open account with non-zero balance");
                }
            }
        }

        for (Account account : accounts) {
            if (account.getAccountStatus() == AccountStatus.OPEN) {
                account.setAccountStatus(AccountStatus.CLOSED);
            }
        }

        customer.setStatus(CustomerStatus.CLOSED);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
