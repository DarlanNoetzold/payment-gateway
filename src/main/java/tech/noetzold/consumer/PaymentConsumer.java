package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.CustomerModel;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.service.CustomerService;
import tech.noetzold.service.PaymentService;

import java.util.Date;

public class PaymentConsumer {

    @Inject
    PaymentService paymentService;


    private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

    @Incoming("payments")
    @Blocking
    public PaymentModel process(PaymentModel incomingPaymentMode) {
        incomingPaymentMode.setRegisterDate(new Date());
        paymentService.savePaymentModel(incomingPaymentMode);
        logger.info("Create Customer " + incomingPaymentMode.getId() + " for user " + incomingPaymentMode.getCustomer().getUserId() + ".");

        return incomingPaymentMode;
    }

}