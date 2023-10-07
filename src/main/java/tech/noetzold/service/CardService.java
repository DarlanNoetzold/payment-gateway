package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    @Transactional
    @CacheResult(cacheName = "card")
    public CardModel findCardModelById(UUID id){
        Optional<CardModel> optionalCardModel = cardRepository.findByIdOptional(id);
        return optionalCardModel.orElse(null);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "card")
    public CardModel saveCardModel(CardModel cardModel){
        cardModel.setId(UUID.randomUUID());
        cardRepository.persist(cardModel);
        return cardModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "card")
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

    @Transactional
    @CacheInvalidateAll(cacheName = "card")
    public void deleteCardModelById(UUID id){
        cardRepository.deleteById(id);
    }

}
