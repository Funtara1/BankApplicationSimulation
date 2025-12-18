package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CustomerFilterRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.ResponseDTO.CustomerResponse;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Mapper.CustomerMapper;
import com.Fuad.BankApplicationSimulation.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        Customer customer = customerService.createCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerMapper.toResponse(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerMapper.toResponse(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CreateCustomerRequest request) {

        Customer customer = customerService.updateCustomerById(id, request);
        return ResponseEntity.ok(customerMapper.toResponse(customer));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<CustomerResponse> closeCustomer(@PathVariable Long id) {
        Customer customer = customerService.closeCustomerById(id);
        return ResponseEntity.ok(customerMapper.toResponse(customer));
    }

    @GetMapping
    public Page<CustomerResponse> filter(
            @ParameterObject CustomerFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return customerService.filter(filter, page, size);
    }
}
