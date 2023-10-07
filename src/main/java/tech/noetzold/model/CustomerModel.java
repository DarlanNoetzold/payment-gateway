package tech.noetzold.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wildfly.common.annotation.NotNull;
import tech.noetzold.model.paymentMethods.CardModel;

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
    @JoinColumn(name = "transaction_id")
    private List<PaymentModel> transactions;

    @OneToMany
    @JoinColumn(name = "card_id")
    private List<CardModel> cards;

}
