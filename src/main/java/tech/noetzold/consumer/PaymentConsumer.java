package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Merge;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.CustomerModel;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.model.enums.PaymentMethod;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.model.paymentMethods.CardModel;
import tech.noetzold.model.paymentMethods.PaypalModel;
import tech.noetzold.model.paymentMethods.PixModel;
import tech.noetzold.service.*;

import java.util.Date;

@ApplicationScoped
public class PaymentConsumer {

    @Inject
    PaymentService paymentService;

    @Inject
    CustomerService customerService;

    @Inject
    PaypalService paypalService;

    @Inject
    CardService cardService;

    @Inject
    PixService pixService;

    @Inject
    BoletoService boletoService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

    @Incoming("payments")
    @Merge
    @Blocking
    public PaymentModel process(PaymentModel incomingPaymentMode) {

        CustomerModel customerModel = customerService.findCustomerModelByUserId(incomingPaymentMode.getCustomer().getUserId());

        if (customerModel == null){
            customerModel = customerService.saveCustomerModel(incomingPaymentMode.getCustomer());
        }

        if (incomingPaymentMode.getPaymentMethod().equals(PaymentMethod.PAYPAL)){
            PaypalModel paypalModel = paypalService.savePaypalModel(incomingPaymentMode.getPaypalModel());
            incomingPaymentMode.setPaypalModel(paypalModel);
        } else if (incomingPaymentMode.getPaymentMethod().equals(PaymentMethod.DEBIT_CARD) || incomingPaymentMode.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            CardModel cardModel = cardService.saveCardModel(incomingPaymentMode.getCardModel());
            incomingPaymentMode.setCardModel(cardModel);
        } else if (incomingPaymentMode.getPaymentMethod().equals(PaymentMethod.PIX)) {
            PixModel pixModel = pixService.savePixModel(incomingPaymentMode.getPixModel());
            incomingPaymentMode.setPixModel(pixModel);
        } else if (incomingPaymentMode.getPaymentMethod().equals(PaymentMethod.BOLETO)) {
            BoletoModel boletoModel = boletoService.saveBoletoModel(incomingPaymentMode.getBoletoModel());
            incomingPaymentMode.setBoletoModel(boletoModel);
        } else {
            logger.error("Lost message info.");
            return null;
        }

        incomingPaymentMode.setCustomer(customerModel);
        incomingPaymentMode.setRegisterDate(new Date());
        paymentService.savePaymentModel(incomingPaymentMode);
        logger.info("Create payments " + incomingPaymentMode.getId() + " for user " + incomingPaymentMode.getCustomer().getUserId() + ".");

        return incomingPaymentMode;
    }

}