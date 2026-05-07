package net.ayoub.digitalbankingback;

import net.ayoub.digitalbankingback.entities.AccountOperation;
import net.ayoub.digitalbankingback.entities.CurrentAccount;
import net.ayoub.digitalbankingback.entities.Customer;
import net.ayoub.digitalbankingback.entities.SavingAccount;
import net.ayoub.digitalbankingback.enums.AccountStatus;
import net.ayoub.digitalbankingback.enums.OperationType;
import net.ayoub.digitalbankingback.repositories.AccountOperationRepository;
import net.ayoub.digitalbankingback.repositories.BanckAccountRepository;
import net.ayoub.digitalbankingback.repositories.CustomerRepository;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            BanckAccountRepository banckAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Ali","Ibrahim","Ahmed").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {

                CurrentAccount currentAccount = new CurrentAccount();

                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(55000);
                banckAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();

                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                banckAccountRepository.save(savingAccount);
            });

            banckAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random()*11200);
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setBankAccount(bankAccount);
                    accountOperation.setType(Math.random()>0.5 ? OperationType.CREDIT : OperationType.DEBIT );
                    accountOperationRepository.save(accountOperation);
                 }
            });

        };
    }

}
