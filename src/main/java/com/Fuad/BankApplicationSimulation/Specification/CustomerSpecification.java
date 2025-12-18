package com.Fuad.BankApplicationSimulation.Specification;

import com.Fuad.BankApplicationSimulation.Entity.Customer;
import com.Fuad.BankApplicationSimulation.Enums.CustomerStatus;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    public static Specification<Customer> nameLike(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.trim().toLowerCase() + "%"
                );
    }

    public static Specification<Customer> surnameLike(String surname) {
        return (root, query, cb) ->
                (surname == null || surname.isBlank())
                        ? null
                        : cb.like(
                        cb.lower(root.get("surname")),
                        "%" + surname.trim().toLowerCase() + "%"
                );
    }

    public static Specification<Customer> finEquals(String fin) {
        return (root, query, cb) ->
                (fin == null || fin.isBlank())
                        ? null
                        : cb.equal(
                        cb.upper(root.get("fin")),
                        fin.trim().toUpperCase()
                );
    }

    public static Specification<Customer> statusEquals(CustomerStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }
}
