package com.Fuad.BankApplicationSimulation.Repository;

import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        select t from Transaction t 
        where t.fromAccount.id = :accountId
           or t.toAccount.id = :accountId
    """)
    List<Transaction> findByAccountId(Long accountId);
}
