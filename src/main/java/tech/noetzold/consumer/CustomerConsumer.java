package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.CustomerModel;
import tech.noetzold.service.CustomerService;

import java.util.Date;

public class CustomerConsumer {

    @Inject
    CustomerService customerService;


    private static final Logger logger = LoggerFactory.getLogger(CustomerConsumer.class);

    @Incoming("alerts")
    @Blocking
    public CustomerModel process(CustomerModel incomingCustomerModel) {
        incomingCustomerModel.setRegisterDate(new Date());
        customerService.saveCustomerModel(incomingCustomerModel);
        logger.info("Create Customer " + incomingCustomerModel.getId() + " for user " + incomingCustomerModel.getUserId() + ".");

        return incomingCustomerModel;
    }

}
