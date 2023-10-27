package tech.noetzold.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.wildfly.common.annotation.NotNull;
import tech.noetzold.model.enums.PaymentMethod;
import tech.noetzold.model.enums.PaymentState;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.model.paymentMethods.CardModel;
import tech.noetzold.model.paymentMethods.PaypalModel;
import tech.noetzold.model.paymentMethods.PixModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @NotNull
    @ManyToOne(cascade=CascadeType.PERSIST)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private PaymentState paymentState;

    @NotNull
    private boolean hasErrors;

    @NotNull
    private double totalAmount;

    @NotNull
    private String orderId;

    private Date registerDate;

    private double dicountAmount;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "boleto_model_id")
    private BoletoModel boletoModel;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "card_model_id")
    private CardModel cardModel;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "pix_model_id")
    private PixModel pixModel;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "paypal_model_id")
    private PaypalModel paypalModel;

    @OneToMany(mappedBy = "payment")
    @Fetch(FetchMode.JOIN)
    private List<InvoiceModel> invoices;


}
