package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.paymentMethods.PaypalModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaypalRepository implements PanacheRepository<PaypalModel> {
    public Optional<PaypalModel> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }
}
