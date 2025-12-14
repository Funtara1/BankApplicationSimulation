package com.Fuad.BankApplicationSimulation.Repository;

import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


//    @Query("""
// select t from Transaction t
// where (t.fromAccount.id = :accountId or t.toAccount.id = :accountId)
// order by t.timestamp desc
//""")
//    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findAllByFromAccountIdOrToAccountIdOrderByTimestampDesc(Long fromAccountId, Long toAccountId);


}
