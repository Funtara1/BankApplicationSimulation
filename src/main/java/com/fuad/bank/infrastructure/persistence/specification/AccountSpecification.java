package com.fuad.bank.infrastructure.persistence.specification;

import com.fuad.bank.domain.entity.Account;
import com.fuad.bank.domain.enums.AccountStatus;
import com.fuad.bank.domain.enums.Currency;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {

    public static Specification<Account> currencyEquals(Currency currency) {
        return (root, query, cb) ->
                currency == null ? null :
                        cb.equal(root.get("currency"), currency);
    }

    public static Specification<Account> statusEquals(AccountStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("accountStatus"), status);
    }

    public static Specification<Account> customerEquals(Long customerId) {
        return (root, query, cb) ->
                customerId == null ? null :
                        cb.equal(root.get("customer").get("id"), customerId);
    }
}
