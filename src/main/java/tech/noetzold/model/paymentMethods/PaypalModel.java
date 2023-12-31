package tech.noetzold.model.paymentMethods;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaypalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paypalId;
    private String email;
    private String descricao;
    private String identificadorTransacao;
}
