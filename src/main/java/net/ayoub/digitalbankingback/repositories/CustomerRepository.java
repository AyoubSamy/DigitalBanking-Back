package net.ayoub.digitalbankingback.repositories;

import net.ayoub.digitalbankingback.entities.BankAccount;
import net.ayoub.digitalbankingback.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
