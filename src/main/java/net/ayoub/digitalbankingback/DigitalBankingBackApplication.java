package net.ayoub.digitalbankingback;

import net.ayoub.digitalbankingback.dtos.CustomerDTO;
import net.ayoub.digitalbankingback.entities.*;
import net.ayoub.digitalbankingback.enums.AccountStatus;
import net.ayoub.digitalbankingback.enums.OperationType;
import net.ayoub.digitalbankingback.exceptions.BankAccountNotFoundException;
import net.ayoub.digitalbankingback.exceptions.EnoughAmountException;
import net.ayoub.digitalbankingback.exceptions.customerNotfoundException;
import net.ayoub.digitalbankingback.repositories.AccountOperationRepository;
import net.ayoub.digitalbankingback.repositories.BanckAccountRepository;
import net.ayoub.digitalbankingback.repositories.CustomerRepository;
import net.ayoub.digitalbankingback.services.BankAccountServiceImp;
import net.ayoub.digitalbankingback.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountService iBankAccountService, BankAccountServiceImp bankAccountServiceImp){
        return  args -> {
            Stream.of("Hassan","Ali","Ibrahim","Ahmed").forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                iBankAccountService.saveCustomer(customer);
            });

            iBankAccountService.listCustomer().forEach(customer -> {
                try {
                    iBankAccountService.saveCurrentBankAccount(Math.random()*1500000,900.0,customer.getId());
                    iBankAccountService.saveSavingBankAccount(Math.random()*10000,5.5,customer.getId());
                    List<BankAccount> bankAccounts = iBankAccountService.listAccounts();

                    for (BankAccount bankAccount : bankAccounts){
                        for (int i = 0; i < 10; i++) {
                            iBankAccountService.credit(bankAccount.getId(),1000+Math.random()*90000,"CREDIT");
                            iBankAccountService.debit(bankAccount.getId(),1000+Math.random()*1000,"DEBIT");
                        }
                    }
                } catch (customerNotfoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | EnoughAmountException e) {
                    e.printStackTrace();
                }
            });



        };
    }



    //@Bean
    CommandLineRunner start(BanckAccountRepository banckAccountRepository){
        return args -> {
            BankAccount bankAccount = banckAccountRepository.findById("502c0dc1-f067-47c4-a98f-5d9f2825df8e").orElse(null);
            if (bankAccount != null ){
                System.out.println("****************************************** Bank Account ******************************* ");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                System.out.println(bankAccount.getClass().getName());
                if(bankAccount instanceof CurrentAccount){
                    System.out.println("Over-Draft => " + ((CurrentAccount)bankAccount).getOverDraft());
                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println("InterestRate => " + ((SavingAccount)bankAccount).getInterestRate());
                }
                System.out.println("****************************************** Operations sur L'account " +bankAccount.getId()+ " ******************************* ");
                bankAccount.getAccountOperations().forEach(accountOperation -> {
                    System.out.println("********************************");
                    System.out.println(accountOperation.getOperationDate() + "\t" + accountOperation.getType() + "\t" +accountOperation.getAmount());
                });
            }
        };
    }

    //@Bean
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
