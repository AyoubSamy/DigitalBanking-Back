package net.ayoub.digitalbankingback.services;

import net.ayoub.digitalbankingback.dtos.*;
import net.ayoub.digitalbankingback.entities.BankAccount;
import net.ayoub.digitalbankingback.entities.CurrentAccount;
import net.ayoub.digitalbankingback.entities.Customer;
import net.ayoub.digitalbankingback.entities.SavingAccount;
import net.ayoub.digitalbankingback.exceptions.BankAccountNotFoundException;
import net.ayoub.digitalbankingback.exceptions.EnoughAmountException;
import net.ayoub.digitalbankingback.exceptions.customerNotfoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDto saveCurrentBankAccount(Double initialBalance , Double OverDraft, Long CustomerId) throws customerNotfoundException;

    SavingBankAccountDto saveSavingBankAccount(Double initialBalance , Double InterstRate, Long CustomerId) throws customerNotfoundException;

    List<CustomerDTO> listCustomer();

    BankAccountDTO getBankAccount(String BankAccountID) throws BankAccountNotFoundException;

    void debit(String accountId , double amount , String Description) throws BankAccountNotFoundException, EnoughAmountException;

    void credit(String accountId , double amount , String Description) throws BankAccountNotFoundException, EnoughAmountException;

    void transfer(String accountIdSource ,String accountIdDestination , double amount) throws BankAccountNotFoundException, EnoughAmountException;

    List<BankAccountDTO> listAccounts();

    CustomerDTO getCustomer(Long CustomerId) throws customerNotfoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deletCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
