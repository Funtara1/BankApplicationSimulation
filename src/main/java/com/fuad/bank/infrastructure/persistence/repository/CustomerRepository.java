package com.fuad.bank.infrastructure.persistence.repository;

import com.fuad.bank.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByFinIgnoreCase(String fin);
    boolean existsByFinIgnoreCase(String fin);
}
