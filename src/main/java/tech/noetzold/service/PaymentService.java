package tech.noetzold.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.PaymentRepository;

@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentRepository paymentRepository;
}
