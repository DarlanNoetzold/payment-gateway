package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.CustomerModel;
import tech.noetzold.model.paymentMethods.CardModel;
import tech.noetzold.repository.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @CacheResult(cacheName = "customer")
    @Transactional
    public CustomerModel findCustomerModelById(UUID id){
        Optional<CustomerModel> optionalCustomerModel = customerRepository.findByIdOptional(id);
        return optionalCustomerModel.orElse(null);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "customer")
    public void saveCustomerModel(CustomerModel customerModel){
        customerRepository.persist(customerModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "customer")
    public void updateCustomerModel(CustomerModel customerModel){
        if (customerModel == null || customerModel.getId() == null) {
            throw new WebApplicationException("Invalid data for customerModel update", Response.Status.BAD_REQUEST);
        }

        CustomerModel existingCustomerModel = findCustomerModelById(customerModel.getId());
        if (existingCustomerModel == null) {
            throw new WebApplicationException("customerModel not found", Response.Status.NOT_FOUND);
        }

        existingCustomerModel.setBornDate(customerModel.getBornDate());
        existingCustomerModel.setCards(customerModel.getCards());
        existingCustomerModel.setTransactions(customerModel.getTransactions());
        existingCustomerModel.setUserId(customerModel.getUserId());
        existingCustomerModel.setRegisterDate(customerModel.getRegisterDate());
        existingCustomerModel.setTransactionsWithError(customerModel.getTransactionsWithError());

        customerRepository.persist(existingCustomerModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "customer")
    public void deleteCustomerModelById(UUID id){
        customerRepository.deleteById(id);
    }

}
