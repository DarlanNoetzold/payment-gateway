package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Merge;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.InvoiceModel;
import tech.noetzold.service.InvoiceService;

@ApplicationScoped
public class InvoiceConsumer {

    @Inject
    InvoiceService invoiceService;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceConsumer.class);

    @Incoming("invoicers")
    @Merge
    @Blocking
    public InvoiceModel process(JsonObject incomingInvoiceModelInJson) {

        InvoiceModel incomingInvoiceModel = incomingInvoiceModelInJson.mapTo(InvoiceModel.class);

        invoiceService.saveInvoice(incomingInvoiceModel);
        logger.info("Create Invoice " + incomingInvoiceModel.getInvoiceId() + " for payment " + incomingInvoiceModel.getPayment().getPaymentId() + ".");

        return incomingInvoiceModel;
    }
}
