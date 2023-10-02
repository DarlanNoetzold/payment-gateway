package tech.noetzold.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.paymentMethods.PaypalModel;
import tech.noetzold.repository.PaypalRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaypalService {

    @Inject
    PaypalRepository paypalRepository;

    public PaypalModel findPaypalModelById(UUID id){
        Optional<PaypalModel> optionalPaypalModel = paypalRepository.findByIdOptional(id);
        return optionalPaypalModel.orElse(null);
    }
    
    public void savePaypalModel(PaypalModel paypalModel){
        paypalRepository.persist(paypalModel);
    }

    public void updatePaypalModel(PaypalModel paypalModel){
        if (paypalModel == null || paypalModel.getId() == null) {
            throw new WebApplicationException("Invalid data for paypalModel update", Response.Status.BAD_REQUEST);
        }

        PaypalModel existingPaypalModel = findPaypalModelById(paypalModel.getId());
        if (existingPaypalModel == null) {
            throw new WebApplicationException("customerModel not found", Response.Status.NOT_FOUND);
        }

        existingPaypalModel.setDescricao(paypalModel.getDescricao());
        existingPaypalModel.setEmail(paypalModel.getEmail());
        existingPaypalModel.setIdentificadorTransacao(paypalModel.getIdentificadorTransacao());

        paypalRepository.persist(existingPaypalModel);
    }

    public void deletePaypalModelById(UUID id){
        paypalRepository.deleteById(id);
    }
}
