package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.CustomerRepository;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;
}
