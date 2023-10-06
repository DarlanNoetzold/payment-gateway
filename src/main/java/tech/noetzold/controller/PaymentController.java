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

import java.util.UUID;

@Path("/api/v1/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @Channel("customers")
    Emitter<CustomerModel> quoteRequestEmitter;

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

}
