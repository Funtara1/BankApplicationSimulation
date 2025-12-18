package com.Fuad.BankApplicationSimulation.Specification;

import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import com.Fuad.BankApplicationSimulation.Enums.TransactionStatus;
import com.Fuad.BankApplicationSimulation.Enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {

    public static Specification<Transaction> byAccount(Long accountId) {
        return (root, query, cb) ->
                accountId == null ? null :
                        cb.or(
                                cb.equal(root.get("fromAccount").get("id"), accountId),
                                cb.equal(root.get("toAccount").get("id"), accountId)
                        );
    }

    public static Specification<Transaction> typeEquals(TransactionType type) {
        return (root, query, cb) ->
                type == null ? null :
                        cb.equal(root.get("transactionType"), type);
    }

    public static Specification<Transaction> statusEquals(TransactionStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("transactionStatus"), status);
    }
}
