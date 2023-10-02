package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.CustomerModel;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<CustomerModel> {
}
