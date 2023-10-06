package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.PaymentModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<PaymentModel> {
    public Optional<PaymentModel> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public Optional<PaymentModel> findByUserIdOptional(String userId) {
        return find("customer_id", userId).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }
}
