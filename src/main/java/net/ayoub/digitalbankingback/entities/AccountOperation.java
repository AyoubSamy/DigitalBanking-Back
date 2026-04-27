package net.ayoub.digitalbankingback.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ayoub.digitalbankingback.enums.OperationType;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;

    private OperationType type;

    @ManyToOne
    private BankAccount bankAccount;

    private String description;

}
