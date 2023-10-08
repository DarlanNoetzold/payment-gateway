package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Merge;
import io.vertx.core.json.JsonObject;
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
    public PaymentModel process(JsonObject incomingPaymentModeInJson) {

        PaymentModel incomingPaymentModel = incomingPaymentModeInJson.mapTo(PaymentModel.class);

        CustomerModel customerModel = customerService.findCustomerModelByUserId(incomingPaymentModel.getCustomer().getUserId());

        if (customerModel == null){
            customerModel = customerService.saveCustomerModel(incomingPaymentModel.getCustomer());
        }

        if (incomingPaymentModel.getPaymentMethod().equals(PaymentMethod.PAYPAL)){
            PaypalModel paypalModel = paypalService.savePaypalModel(incomingPaymentModel.getPaypalModel());
            incomingPaymentModel.setPaypalModel(paypalModel);
        } else if (incomingPaymentModel.getPaymentMethod().equals(PaymentMethod.DEBIT_CARD) || incomingPaymentModel.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            CardModel cardModel = cardService.saveCardModel(incomingPaymentModel.getCardModel());
            incomingPaymentModel.setCardModel(cardModel);
        } else if (incomingPaymentModel.getPaymentMethod().equals(PaymentMethod.PIX)) {
            PixModel pixModel = pixService.savePixModel(incomingPaymentModel.getPixModel());
            incomingPaymentModel.setPixModel(pixModel);
        } else if (incomingPaymentModel.getPaymentMethod().equals(PaymentMethod.BOLETO)) {
            BoletoModel boletoModel = boletoService.saveBoletoModel(incomingPaymentModel.getBoletoModel());
            incomingPaymentModel.setBoletoModel(boletoModel);
        } else {
            logger.error("Lost message info.");
            return null;
        }

        incomingPaymentModel.setCustomer(customerModel);
        incomingPaymentModel.setRegisterDate(new Date());
        paymentService.savePaymentModel(incomingPaymentModel);
        logger.info("Create payments " + incomingPaymentModel.getPaymentId() + " for user " + incomingPaymentModel.getCustomer().getUserId() + ".");

        return incomingPaymentModel;
    }

}