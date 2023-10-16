package tech.noetzold.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    private UUID customerId;

    @NotNull
    private String userId;

    private Date registerDate;

    private Date bornDate;

    @OneToMany
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "transaction_id")
    private List<PaymentModel> transactions;

    @OneToMany
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "card_id")
    private List<CardModel> cards;

}
