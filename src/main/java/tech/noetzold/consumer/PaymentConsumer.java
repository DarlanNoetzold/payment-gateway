package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Merge;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.service.PaymentService;

import java.util.Date;

@ApplicationScoped
public class PaymentConsumer {

    @Inject
    PaymentService paymentService;


    private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

    @Incoming("payments")
    @Merge
    @Blocking
    public PaymentModel process(PaymentModel incomingPaymentMode) {
        incomingPaymentMode.setRegisterDate(new Date());
        paymentService.savePaymentModel(incomingPaymentMode);
        logger.info("Create payments " + incomingPaymentMode.getId() + " for user " + incomingPaymentMode.getCustomer().getUserId() + ".");

        return incomingPaymentMode;
    }

}