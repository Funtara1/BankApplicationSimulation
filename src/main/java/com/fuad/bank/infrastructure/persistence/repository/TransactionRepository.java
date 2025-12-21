package com.fuad.bank.infrastructure.persistence.repository;

import com.fuad.bank.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {


//    @Query("""
// select t from Transaction t
// where (t.fromAccount.id = :accountId or t.toAccount.id = :accountId)
// order by t.timestamp desc
//""")
//    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findAllByFromAccountIdOrToAccountIdOrderByTimestampDesc(Long fromAccountId, Long toAccountId);


}
