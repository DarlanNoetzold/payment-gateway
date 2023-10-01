package tech.noetzold.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wildfly.common.annotation.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String userId;

    private Date registerDate;

    private Date bornDate;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<MoneyTransaction> transactions;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<MoneyTransaction> transactionsWithError;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<CardModel> cards;

}
