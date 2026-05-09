package net.ayoub.digitalbankingback.services;

import net.ayoub.digitalbankingback.entities.BankAccount;
import net.ayoub.digitalbankingback.entities.CurrentAccount;
import net.ayoub.digitalbankingback.entities.Customer;
import net.ayoub.digitalbankingback.entities.SavingAccount;
import net.ayoub.digitalbankingback.exceptions.BankAccountNotFoundException;
import net.ayoub.digitalbankingback.exceptions.EnoughAmountException;
import net.ayoub.digitalbankingback.exceptions.customerNotfoundException;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(Double initialBalance , Double OverDraft, Long CustomerId) throws customerNotfoundException;

    SavingAccount saveSavingBankAccount(Double initialBalance , Double InterstRate, Long CustomerId) throws customerNotfoundException;

    List<Customer> listCustomer();

    BankAccount getBankAccount(String BankAccountID) throws BankAccountNotFoundException;

    void debit(String accountId , double amount , String Description) throws BankAccountNotFoundException, EnoughAmountException;

    void credit(String accountId , double amount , String Description) throws BankAccountNotFoundException, EnoughAmountException;

    void transfert(String accountIdSource ,String accountIdDestination , double amount) throws BankAccountNotFoundException, EnoughAmountException;

    List<BankAccount> listAccounts();
}
