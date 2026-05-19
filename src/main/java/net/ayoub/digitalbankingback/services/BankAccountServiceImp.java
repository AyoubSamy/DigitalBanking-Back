package net.ayoub.digitalbankingback.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ayoub.digitalbankingback.dtos.*;
import net.ayoub.digitalbankingback.entities.*;
import net.ayoub.digitalbankingback.enums.AccountStatus;
import net.ayoub.digitalbankingback.enums.OperationType;
import net.ayoub.digitalbankingback.exceptions.BankAccountNotFoundException;
import net.ayoub.digitalbankingback.exceptions.EnoughAmountException;
import net.ayoub.digitalbankingback.exceptions.customerNotfoundException;
import net.ayoub.digitalbankingback.mappers.BankAccountMapperImpl;
import net.ayoub.digitalbankingback.repositories.AccountOperationRepository;
import net.ayoub.digitalbankingback.repositories.BanckAccountRepository;
import net.ayoub.digitalbankingback.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImp implements BankAccountService {


    private AccountOperationRepository accountOperationRepository;

    private BanckAccountRepository banckAccountRepository;

    private CustomerRepository customerRepository;

    private BankAccountMapperImpl DtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new customer");
        Customer customer = DtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer  = customerRepository.save(customer);

        return DtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDto saveCurrentBankAccount(Double initialBalance, Double OverDraft, Long CustomerId) throws customerNotfoundException {

        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer == null)
            throw new customerNotfoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount() ;
        currentAccount.setId(UUID.randomUUID().toString());

        currentAccount.setCreatedAt(new Date());

        currentAccount.setBalance(initialBalance);

        currentAccount.setStatus(AccountStatus.CREATED);

        currentAccount.setCustomer(customer);

        currentAccount.setOverDraft(OverDraft);

        CurrentAccount savedCurrentAccount =  banckAccountRepository.save(currentAccount);
        return DtoMapper.fromCurrentBankAccount(savedCurrentAccount);
    }

    @Override
    public SavingBankAccountDto saveSavingBankAccount(Double initialBalance, Double InterstRate, Long CustomerId) throws customerNotfoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer == null)
            throw new customerNotfoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());

        savingAccount.setCreatedAt(new Date());

        savingAccount.setBalance(initialBalance);

        savingAccount.setStatus(AccountStatus.CREATED);

        savingAccount.setCustomer(customer);

        savingAccount.setInterestRate(InterstRate);

        SavingAccount savedSavingAccount =  banckAccountRepository.save(savingAccount);
        return DtoMapper.fromSavingBankAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> DtoMapper.fromCustomer(customer)).collect(Collectors.toList());

        /*
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO = DtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }*/
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String BankAccountID) throws BankAccountNotFoundException {
        BankAccount bankAccount = banckAccountRepository.findById(BankAccountID)
                .orElseThrow(()->new BankAccountNotFoundException("le compte n'est pas trouver"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount  = (SavingAccount) bankAccount;
            return DtoMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return DtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String Description) throws BankAccountNotFoundException, EnoughAmountException {
        BankAccount bankAccount = banckAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("le compte n'est pas trouver"));
        if ( bankAccount.getBalance() < amount )
            throw new EnoughAmountException("Balance Not Sufficient");

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(Description);
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance( bankAccount.getBalance() - amount );

        banckAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String Description) throws BankAccountNotFoundException, EnoughAmountException {
        BankAccount bankAccount = banckAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("le compte n'est pas trouver"));

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(Description);
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance( bankAccount.getBalance() + amount );

        banckAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, EnoughAmountException {
        debit(accountIdSource,amount,"transfer to " + accountIdDestination);
        credit(accountIdDestination,amount,"transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> listAccounts(){
        List<BankAccount> BankAccounts = banckAccountRepository.findAll();
        List<BankAccountDTO> BankaccountsDTO = BankAccounts.stream().map(bankAccount -> {
           if (bankAccount instanceof SavingAccount){
               SavingAccount savingAccount = (SavingAccount) bankAccount;
               return DtoMapper.fromSavingBankAccount(savingAccount);
           }else {
               CurrentAccount currentAccount = (CurrentAccount) bankAccount;
               return DtoMapper.fromCurrentBankAccount(currentAccount);
           }
        }).collect(Collectors.toList());
        return BankaccountsDTO;
    }

    @Override
    public CustomerDTO getCustomer(Long CustomerId) throws customerNotfoundException {
        Customer customer = customerRepository.findById(CustomerId)
                .orElseThrow( () -> new customerNotfoundException("Customer Not Found"));

        return DtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new customer");
        Customer customer = DtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer  = customerRepository.save(customer);

        return DtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deletCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->DtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=banckAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> DtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> DtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }
}
