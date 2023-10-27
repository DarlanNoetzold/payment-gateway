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
import tech.noetzold.model.CustomerModel;
import tech.noetzold.service.CustomerService;

import java.util.UUID;

@Path("/api/payment/v1/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    CustomerService customerService;

    @Channel("customers")
    Emitter<JsonObject> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(CustomerController.class);

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getCustomerModelByUserId(@PathParam("id") String id){

        CustomerModel customerModel = customerService.findCustomerModelByUserId(id);

        if(customerModel.getCustomerId() == null){
            logger.error("There is no customer with userId: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(customerModel).build();
    }

    @GET
    @Path("/paymentId/{id}")
    @RolesAllowed("admin")
    public Response getCustomerModelById(@PathParam("id") String id){
        UUID uuid = UUID.fromString(id);

        CustomerModel customerModel = customerService.findCustomerModelById(uuid);

        if(customerModel.getCustomerId() == null){
            logger.error("There is no customer with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(customerModel).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response saveCustomerModel(CustomerModel customerModel){
        try {
            if (customerModel.getUserId() == null) {
                logger.error("Error to create Customer withou userId: " + customerModel.getCustomerId());
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            customerModel.setCustomerId(null);
            quoteRequestEmitter.send(JsonObject.mapFrom(customerModel));
            logger.info("Create " + customerModel.getCustomerId());
            return Response.status(Response.Status.CREATED).entity(customerModel).build();
        } catch (Exception e) {
            logger.error("Error to create customerModel: " + customerModel.getCustomerId());
            e.printStackTrace();
        }
        logger.error("Error to create customerModel: " + customerModel.getCustomerId());
        return Response.status(Response.Status.BAD_REQUEST).entity(customerModel).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response updateCustomerModel(@PathParam("id") String id, CustomerModel updatedCustomerModel) {
        if (id.isBlank()) {
            logger.warn("Error to update customerModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        CustomerModel existingCustomerModel = customerService.findCustomerModelById(UUID.fromString(id));
        if (existingCustomerModel.getCustomerId() == null) {
            logger.warn("Error to update customerModel: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        updatedCustomerModel.setCustomerId(existingCustomerModel.getCustomerId());

        customerService.updateCustomerModel(updatedCustomerModel);

        return Response.ok(updatedCustomerModel).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteCustomerModel(@PathParam("id") String id){
        if (id.isBlank()) {
            logger.warn("Error to delete customerModel: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UUID uuid = UUID.fromString(id);

        customerService.deleteCustomerModelById(uuid);

        return Response.ok().build();
    }

}
