package tech.noetzold.controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import tech.noetzold.model.CustomerModel;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.service.PaymentService;

import java.util.Date;
import java.util.UUID;

@Path("/api/v1/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @Channel("payments")
    Emitter<PaymentModel> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(CustomerController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response findPaymentModelById(@PathParam("id") String id) {
        UUID uuid = UUID.fromString(id);

        PaymentModel paymentModel = paymentService.findPaymentModelById(uuid);

        if (paymentModel == null) {
            logger.error("There is no payment with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paymentModel).build();
    }

    @GET
    @Path("/user/{user}")
    @RolesAllowed("admin")
    public Response findPaymentModelUserById(@PathParam("userId") String userId){
        PaymentModel paymentModel = paymentService.findPaymentModelByUserId(userId);

        if (paymentModel == null) {
            logger.error("There is no payment with user id: " + userId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paymentModel).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response savePaymentModel(PaymentModel paymentModel){

        paymentModel.setId(UUID.randomUUID());
        paymentModel.setRegisterDate(new Date());

        quoteRequestEmitter.send(paymentModel);
        logger.info("Payment message sended");

        return Response.ok(paymentModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") String id, PaymentModel paymentModel) {
        if (id.isBlank() || paymentModel == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        PaymentModel existingPaymentModel = paymentService.findPaymentModelById(UUID.fromString(id));
        if (existingPaymentModel == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        paymentService.updatePaymentModel(paymentModel);

        return Response.ok(paymentModel).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deletePaymentModel(@PathParam("id") String id) {
        if (id.isBlank()) {
            logger.warn("Error to delete Payment: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        paymentService.deletePaymentModelById(UUID.fromString(id));
        logger.info("Remove Payment: " + id);
        return Response.status(Response.Status.ACCEPTED).entity("Payment removed").build();
    }

}
