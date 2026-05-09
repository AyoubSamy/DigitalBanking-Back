package net.ayoub.digitalbankingback.mappers;

import net.ayoub.digitalbankingback.dtos.CustomerDTO;
import net.ayoub.digitalbankingback.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);

//        customerDTO.setId(customer.getId());
//        customerDTO.setEmail(customer.getEmail());
//        customerDTO.setName(customer.getName());

        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);

//        customer.setId(customerDTO.getId());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setName(customerDTO.getName());

        return customer;
    }
}
