package tech.noetzold.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class InvoiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID invoiceId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentId")
    private PaymentModel payment;

    @NotNull
    private String invoiceNumber;

    @NotNull
    private Date invoiceDate;

    @NotNull
    private double totalAmount;

    private double discountAmount;

    private String sellerName;

    private String sellerAddress;

    private String buyerName;

    private String buyerAddress;

    private List<String> itemsId;

}
