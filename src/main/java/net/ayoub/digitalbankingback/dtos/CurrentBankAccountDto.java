package net.ayoub.digitalbankingback.dtos;


import lombok.Data;

import net.ayoub.digitalbankingback.enums.AccountStatus;

import java.util.Date;


@Data
public class CurrentBankAccountDto extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;

    private AccountStatus status;

    private CustomerDTO customerDTO;
    private double Overdraft ;
}
