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
import tech.noetzold.service.CustomerService;

import java.util.Date;

@ApplicationScoped
public class CustomerConsumer {

    @Inject
    CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerConsumer.class);

    @Incoming("customers")
    @Merge
    @Blocking
    public CustomerModel process(JsonObject incomingCustomerModelInJson) {

        CustomerModel incomingCustomerModel = incomingCustomerModelInJson.mapTo(CustomerModel.class);

        incomingCustomerModel.setRegisterDate(new Date());
        customerService.saveCustomerModel(incomingCustomerModel);
        logger.info("Create Customer " + incomingCustomerModel.getCustomerId() + " for user " + incomingCustomerModel.getUserId() + ".");

        return incomingCustomerModel;
    }

}
