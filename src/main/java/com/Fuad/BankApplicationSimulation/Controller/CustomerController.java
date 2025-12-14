package com.Fuad.BankApplicationSimulation.Controller;

import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO.CreateCustomerRequest;
import com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.ResponseDTO.CustomerResponse;
import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        Customer customer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(
                toResponse(customerService.getCustomerById(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        // TODO: Use mapper
        List<CustomerResponse> responses = new ArrayList<>();

        for (Customer customer : customers) {
            responses.add(toResponse(customer));
        }

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CreateCustomerRequest request) {

        Customer customer = customerService.updateCustomerById(id, request);
        return ResponseEntity.ok(toResponse(customer));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<CustomerResponse> closeCustomer(@PathVariable Long id) {
        Customer customer = customerService.closeCustomerById(id);
        return ResponseEntity.ok(toResponse(customer));
    }

    private CustomerResponse toResponse(Customer customer) {
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
