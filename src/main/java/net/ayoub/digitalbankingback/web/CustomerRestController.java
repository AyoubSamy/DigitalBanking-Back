package net.ayoub.digitalbankingback.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ayoub.digitalbankingback.dtos.CustomerDTO;
import net.ayoub.digitalbankingback.entities.Customer;
import net.ayoub.digitalbankingback.exceptions.customerNotfoundException;
import net.ayoub.digitalbankingback.services.BankAccountService;
import net.ayoub.digitalbankingback.services.BankAccountServiceImp;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Name;
import java.util.List;
import java.util.function.LongFunction;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> CustomerList(){
        return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO GetCustomer(@PathVariable(name = "id") Long customerId) throws customerNotfoundException {
        return bankAccountService.getCustomer(customerId);
    }
    @PostMapping("/customers")
    public CustomerDTO SaveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO UpdateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{customerId}")
    public void DeleteCustomer(@PathVariable Long customerId){
         bankAccountService.deletCustomer(customerId);
    }

}
