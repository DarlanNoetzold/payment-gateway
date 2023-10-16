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
import tech.noetzold.service.InvoiceService;

import java.util.List;
import java.util.UUID;

@Path("/api/payment/v1/invoice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceController {

    @Inject
    InvoiceService invoiceService;

    @Channel("invoicers-out")
    Emitter<InvoiceModel> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(InvoiceController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response findInvoiceModelById(@PathParam("id") String id) {
        UUID uuid = UUID.fromString(id);

        InvoiceModel invoiceModel = invoiceService.findInvoiceById(uuid);

        if (invoiceModel.getInvoiceId() == null) {
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

    @POST
    @RolesAllowed("admin")
    public Response saveInvoiceModel(InvoiceModel invoiceModel){
        try {
            if (invoiceModel.getInvoiceNumber() == null) {
                logger.error("Error to create Customer without invoice number.");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            invoiceModel.setInvoiceId(null);
            quoteRequestEmitter.send(invoiceModel);
            logger.info("Create " + invoiceModel.getInvoiceNumber());
            return Response.status(Response.Status.CREATED).entity(invoiceModel).build();
        } catch (Exception e) {
            logger.error("Error to create invoiceModel: " + invoiceModel.getInvoiceNumber());
            e.printStackTrace();
        }
        logger.error("Error to create invoiceModel: " + invoiceModel.getInvoiceNumber());
        return Response.status(Response.Status.BAD_REQUEST).entity(invoiceModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response updateCustomerModel(@PathParam("id") String id, InvoiceModel invoiceModel) {
        if (id.isBlank() || invoiceModel == null) {
            logger.warn("Error to update invoiceModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        InvoiceModel existingInvoiceModel = invoiceService.findInvoiceById(UUID.fromString(id));
        if (existingInvoiceModel.getInvoiceId() == null) {
            logger.warn("Error to update invoiceModel: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        invoiceService.updateInvoice(invoiceModel);

        return Response.ok(invoiceModel).build();
    }

    @DELETE
    @RolesAllowed("admin")
    public Response deleteCustomerModel(@PathParam("id") String id){
        if (id.isBlank()) {
            logger.warn("Error to delete invoiceModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UUID uuid = UUID.fromString(id);

        invoiceService.deleteInvoiceById(uuid);

        return Response.ok().build();
    }

}
