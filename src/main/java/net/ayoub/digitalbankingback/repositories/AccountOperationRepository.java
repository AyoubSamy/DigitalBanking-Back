package net.ayoub.digitalbankingback.repositories;

import net.ayoub.digitalbankingback.entities.AccountOperation;
import net.ayoub.digitalbankingback.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
