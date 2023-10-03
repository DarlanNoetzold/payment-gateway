package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.CustomerModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<CustomerModel> {
    public Optional<CustomerModel> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }

    public Optional<CustomerModel> findByUserIdOptional(String userId) {
        return find("userId", userId).firstResultOptional();
    }
}
