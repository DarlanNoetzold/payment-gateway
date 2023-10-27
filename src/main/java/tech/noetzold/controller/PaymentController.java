package tech.noetzold.controller;


import io.vertx.core.json.JsonObject;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import tech.noetzold.model.PaymentModel;
import tech.noetzold.model.enums.PaymentMethod;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.model.paymentMethods.CardModel;
import tech.noetzold.model.paymentMethods.PaypalModel;
import tech.noetzold.model.paymentMethods.PixModel;
import tech.noetzold.service.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path("/api/payment/v1/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @Inject
    PaypalService paypalService;

    @Inject
    CardService cardService;

    @Inject
    PixService pixService;

    @Inject
    BoletoService boletoService;

    @Inject
    CustomerService customerService;

    @Channel("payments-out")
    Emitter<JsonObject> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(PaymentController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response findPaymentModelById(@PathParam("id") String id) {
        UUID uuid = UUID.fromString(id);

        PaymentModel paymentModel = paymentService.findPaymentModelById(uuid);

        if (paymentModel.getPaymentId() == null) {
            logger.error("There is no payment with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paymentModel).build();
    }

    @GET
    @Path("/order/{orderId}")
    @RolesAllowed("admin")
    public Response findPaymentModelByOrderId(@PathParam("orderId") String orderId){
        List<PaymentModel> paymentModels = paymentService.findPaymentModelByOrderId(orderId);

        if (paymentModels.isEmpty()) {
            logger.error("There is no payment with order id: " + orderId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paymentModels).build();
    }

    @GET
    @Path("/user/{userId}")
    @RolesAllowed("admin")
    public Response findPaymentModelUserById (@PathParam("userId") String userId){
        List<PaymentModel> paymentModels = paymentService.findPaymentModelByUserId(userId);

        if (paymentModels.isEmpty()) {
            logger.error("There is no payment with user id: " + userId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paymentModels).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response savePaymentModel(PaymentModel paymentModel){

        if(paymentModel.getPaymentMethod() == null){
            logger.error("PaymentMethod is null");
            return Response.status(Response.Status.BAD_REQUEST).entity("PaymentMethod needs to be filled.").build();
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
            if(paymentModel.getPaypalModel() == null) {
                logger.error("Paypal PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("PAYPAL needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setCardModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Paypal");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.BOLETO)) {
            if(paymentModel.getBoletoModel() == null) {
                logger.error("Boleto PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("BOLETO needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setCardModel(null);
            paymentModel.setPaypalModel(null);
            logger.info("Clean all payments, less Boleto");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.PIX)) {
            if(paymentModel.getPixModel() == null) {
                logger.error("Pix PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("PIX needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPaypalModel(null);
            paymentModel.setCardModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Pix");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            if(paymentModel.getCardModel() == null) {
                logger.error("Card PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("CARD needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setPaypalModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Card");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.DEBIT_CARD)) {
            if(paymentModel.getCardModel() == null) {
                logger.error("Card PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("CARD needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setPaypalModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Card");
        }

        paymentModel.setRegisterDate(new Date());

        quoteRequestEmitter.send(JsonObject.mapFrom(paymentModel));
        logger.info("Payment message sended");

        return Response.status(Response.Status.CREATED).entity(paymentModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") String id, PaymentModel paymentModel) {
        if (id.isBlank() || paymentModel == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        PaymentModel existingPaymentModel = paymentService.findPaymentModelById(UUID.fromString(id));
        if (existingPaymentModel.getPaymentId() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(paymentModel.getPaymentMethod() == null){
            logger.error("PaymentMethod is null");
            return Response.status(Response.Status.BAD_REQUEST).entity("PaymentMethod needs to be filled.").build();
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
            if(paymentModel.getPaypalModel() == null) {
                logger.error("Paypal PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("PAYPAL needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setCardModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Paypal");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.BOLETO)) {
            if(paymentModel.getBoletoModel() == null) {
                logger.error("Boleto PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("BOLETO needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setCardModel(null);
            paymentModel.setPaypalModel(null);
            logger.info("Clean all payments, less Boleto");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.PIX)) {
            if(paymentModel.getPixModel() == null) {
                logger.error("Pix PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("PIX needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPaypalModel(null);
            paymentModel.setCardModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Pix");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            if(paymentModel.getCardModel() == null) {
                logger.error("Card PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("CARD needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setPaypalModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Card");
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.DEBIT_CARD)) {
            if(paymentModel.getCardModel() == null) {
                logger.error("Card PaymentMethod is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("CARD needs to be filled for this PaymentMethod.").build();
            }
            paymentModel.setPixModel(null);
            paymentModel.setPaypalModel(null);
            paymentModel.setBoletoModel(null);
            logger.info("Clean all payments, less Card");
        }

        if (paymentModel.getPaymentMethod().equals(PaymentMethod.PAYPAL)){
            PaypalModel paypalModel = paypalService.updatePaypalModel(paymentModel.getPaypalModel());
            paymentModel.setPaypalModel(paypalModel);
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.DEBIT_CARD) || paymentModel.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            CardModel cardModel = cardService.updateCardModel(paymentModel.getCardModel());
            paymentModel.setCardModel(cardModel);
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.PIX)) {
            PixModel pixModel = pixService.updatePixModel(paymentModel.getPixModel());
            paymentModel.setPixModel(pixModel);
        } else if (paymentModel.getPaymentMethod().equals(PaymentMethod.BOLETO)) {
            BoletoModel boletoModel = boletoService.updateBoletoModel(paymentModel.getBoletoModel());
            paymentModel.setBoletoModel(boletoModel);
        } else {
            logger.error("Lost message info.");
            return null;
        }

        customerService.updateCustomerModel(customerService.findCustomerModelById(paymentModel.getCustomer().getCustomerId()));

        paymentModel.setPaymentId(existingPaymentModel.getPaymentId());

        paymentService.updatePaymentModel(paymentModel);

        return Response.status(Response.Status.CREATED).entity(paymentModel).build();
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
