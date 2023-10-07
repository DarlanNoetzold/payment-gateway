package tech.noetzold.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wildfly.common.annotation.NotNull;
import tech.noetzold.model.enums.PaymentMethod;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.model.paymentMethods.CardModel;
import tech.noetzold.model.paymentMethods.PaypalModel;
import tech.noetzold.model.paymentMethods.PixModel;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;
    @NotNull
    private PaymentMethod paymentMethod;
    @NotNull
    private boolean hasErrors;
    @NotNull
    private double totalAmount;
    private Date registerDate;
    private double dicountAmount;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private BoletoModel boletoModel;
    @ManyToOne
    @JoinColumn(name = "card_model_id")
    private CardModel cardModel;
    @ManyToOne
    @JoinColumn(name = "pix_model_id")
    private PixModel pixModel;
    @ManyToOne
    @JoinColumn(name = "paypal_model_id")
    private PaypalModel paypalModel;


}
