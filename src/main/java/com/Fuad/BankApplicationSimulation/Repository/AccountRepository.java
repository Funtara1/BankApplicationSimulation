package com.Fuad.BankApplicationSimulation.Repository;

import com.Fuad.BankApplicationSimulation.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
