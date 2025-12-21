package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CustomerFilterRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.ResponseDTO.CustomerResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.AccountStatus;
import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import com.Fuad.BankApplicationSimulation.Exception.DuplicateException;
import com.Fuad.BankApplicationSimulation.Exception.InvalidOperationException;
import com.Fuad.BankApplicationSimulation.Exception.NotFoundException;
import com.Fuad.BankApplicationSimulation.Exception.ValidationException;
import com.Fuad.BankApplicationSimulation.Mapper.CustomerMapper;
import com.Fuad.BankApplicationSimulation.Repository.CustomerRepository;
import com.Fuad.BankApplicationSimulation.Service.CustomerService;
import com.Fuad.BankApplicationSimulation.Specification.CustomerSpecification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "customers", key = "#id")
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Customer not found",
                        Map.of("customerId", id)
                ));
    }

    @Override
    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByFinIgnoreCase(request.getFin())) {
            throw new DuplicateException(
                    "Customer with this FIN already exists",
                    Map.of("fin", request.getFin())
            );
        }

        Customer customer = customerMapper.toEntity(request);

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    @CacheEvict(value = "customers", key = "#id")
    public Customer updateCustomerById(Long id, CreateCustomerRequest request) {

        Customer customer = getCustomerById(id);

        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Cannot update closed customer",
                    Map.of("customerId", id)
            );
        }

        customerMapper.updateEntity(customer, request);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    @CacheEvict(value = "customers", key = "#id")
    public Customer closeCustomerById(Long id) {

        Customer customer = getCustomerById(id);

        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Customer already closed",
                    Map.of("customerId", id)
            );
        }

        for (Account account : customer.getAccounts()) {

            if (account.getAccountStatus() != AccountStatus.OPEN) {
                continue;
            }

            BigDecimal balance = account.getBalance();
            if (balance != null && balance.compareTo(BigDecimal.ZERO) != 0) {
                throw new ValidationException(
                        "Customer has open account with non-zero balance",
                        Map.of(
                                "customerId", id,
                                "accountId", account.getId(),
                                "balance", balance
                        )
                );
            }

            account.setAccountStatus(AccountStatus.CLOSED);
        }

        customer.setStatus(CustomerStatus.CLOSED);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Page<CustomerResponse> filter(CustomerFilterRequest filter, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Customer> specification =
                Specification.where(CustomerSpecification.nameLike(filter.getName()))
                        .and(CustomerSpecification.surnameLike(filter.getSurname()))
                        .and(CustomerSpecification.finEquals(filter.getFin()))
                        .and(CustomerSpecification.statusEquals(filter.getStatus()));

        Page<Customer> customers = customerRepository.findAll(specification, pageable);

        List<CustomerResponse> responses = new ArrayList<>();

        for (Customer customer : customers.getContent()) {
            responses.add(customerMapper.toResponse(customer));
        }

        return new PageImpl<>(
                responses,
                pageable,
                customers.getTotalElements()
        );
    }
}
