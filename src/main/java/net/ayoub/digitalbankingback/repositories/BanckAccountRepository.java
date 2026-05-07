package net.ayoub.digitalbankingback.repositories;

import net.ayoub.digitalbankingback.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanckAccountRepository extends JpaRepository<BankAccount,String> {
}
