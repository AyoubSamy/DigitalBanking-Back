package net.ayoub.digitalbankingback.web;

import lombok.AllArgsConstructor;
import net.ayoub.digitalbankingback.dtos.AccountHistoryDTO;
import net.ayoub.digitalbankingback.dtos.AccountOperationDTO;
import net.ayoub.digitalbankingback.dtos.BankAccountDTO;
import net.ayoub.digitalbankingback.exceptions.BankAccountNotFoundException;
import net.ayoub.digitalbankingback.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {
    public BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountID}")
    public BankAccountDTO getBankAccount(@PathVariable String accountID) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountID);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.listAccounts();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }



}
