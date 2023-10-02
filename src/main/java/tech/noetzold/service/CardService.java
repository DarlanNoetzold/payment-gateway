package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.model.paymentMethods.CardModel;
import tech.noetzold.repository.CardRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CardService {

    @Inject
    CardRepository cardRepository;

    public CardModel findCardModelById(UUID id){
        Optional<CardModel> optionalCardModel = cardRepository.findByIdOptional(id);
        return optionalCardModel.orElse(null);
    }

    public void saveCardModel(CardModel cardModel){
        cardRepository.persist(cardModel);
    }

    public void updateCardModel(CardModel cardModel){
        if (cardModel == null || cardModel.getId() == null) {
            throw new WebApplicationException("Invalid data for cardModel update", Response.Status.BAD_REQUEST);
        }

        CardModel existingCardModel = findCardModelById(cardModel.getId());
        if (existingCardModel == null) {
            throw new WebApplicationException("cardModel not found", Response.Status.NOT_FOUND);
        }

        existingCardModel.setCardType(cardModel.getCardType());
        existingCardModel.setCardNumber(cardModel.getCardNumber());
        existingCardModel.setCardHolderName(cardModel.getCardNumber());
        existingCardModel.setCvv(cardModel.getCvv());
        existingCardModel.setExpirationDate(cardModel.getExpirationDate());

        cardRepository.persist(existingCardModel);
    }

    public void deleteCardModelById(UUID id){
        cardRepository.deleteById(id);
    }

}
