package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import tech.noetzold.model.InvoiceModel;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.service.InvoiceService;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/invoice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceController {

    @Inject
    InvoiceService invoiceService;

    @Channel("invoicers-out")
    Emitter<PaymentModel> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(InvoiceController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response findInvoiceModelById(@PathParam("id") String id) {
        UUID uuid = UUID.fromString(id);

        InvoiceModel invoiceModel = invoiceService.findInvoiceById(uuid);

        if (invoiceModel == null) {
            logger.error("There is no invoice with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(invoiceModel).build();
    }

    @GET
    @Path("/payment/{paymentId}")
    @RolesAllowed("admin")
    public Response findInvoiceModelByPayment(@PathParam("paymentId") String paymentId){
        List<InvoiceModel> invoicesModels = invoiceService.findInvoicesByPaymentId(UUID.fromString(paymentId));

        if (invoicesModels.isEmpty()) {
            logger.error("There is no invoice for the payment: " + paymentId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(invoicesModels).build();
    }

}
