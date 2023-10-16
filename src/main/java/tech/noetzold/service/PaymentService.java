package tech.noetzold.service;


import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.repository.PaymentRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    @Transactional
    @CacheResult(cacheName = "payment")
    public PaymentModel findPaymentModelById(UUID id){
        Optional<PaymentModel> optionalPaymentModel = paymentRepository.findByIdOptional(id);
        return optionalPaymentModel.orElse(new PaymentModel());
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "payment")
    public PaymentModel savePaymentModel(PaymentModel paymentModel){
        paymentRepository.persist(paymentModel);
        paymentRepository.flush();
        return paymentModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "payment")
    public void updatePaymentModel(PaymentModel paymentModel){
        if (paymentModel == null || paymentModel.getPaymentId() == null) {
            throw new WebApplicationException("Invalid data for paymentModel update", Response.Status.BAD_REQUEST);
        }

        PaymentModel existingPaymentModel = findPaymentModelById(paymentModel.getPaymentId());
        if (existingPaymentModel == null) {
            throw new WebApplicationException("customerModel not found", Response.Status.NOT_FOUND);
        }

        existingPaymentModel.setBoletoModel(paymentModel.getBoletoModel());
        existingPaymentModel.setCardModel(paymentModel.getCardModel());
        existingPaymentModel.setPaymentMethod(paymentModel.getPaymentMethod());
        existingPaymentModel.setPaypalModel(paymentModel.getPaypalModel());
        existingPaymentModel.setPixModel(paymentModel.getPixModel());
        existingPaymentModel.setCustomer(paymentModel.getCustomer());
        existingPaymentModel.setDicountAmount(paymentModel.getDicountAmount());
        existingPaymentModel.setHasErrors(paymentModel.isHasErrors());
        existingPaymentModel.setTotalAmount(paymentModel.getTotalAmount());

        paymentRepository.persist(existingPaymentModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "payment")
    public void deletePaymentModelById(UUID id){
        paymentRepository.deleteById(id);
    }

    @Transactional
    @CacheResult(cacheName = "payment")
    public PaymentModel findPaymentModelByUserId(String userId) {
        Optional<PaymentModel> optionalPaymentModel = paymentRepository.findByUserIdOptional(userId);
        return optionalPaymentModel.orElse(new PaymentModel());
    }
}
