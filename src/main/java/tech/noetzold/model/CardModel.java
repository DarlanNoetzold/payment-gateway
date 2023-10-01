package tech.noetzold.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.noetzold.model.enums.CardType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CardModel {
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;
    private CardType cardType;
}
