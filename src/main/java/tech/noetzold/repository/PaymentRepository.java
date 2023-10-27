package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.PaymentModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<PaymentModel> {
    public Optional<PaymentModel> findByIdOptional(UUID id) {
        return find("paymentId", id).firstResultOptional();
    }

    public List<PaymentModel> findByUserId(String userId) {
        return list("customer", userId);
    }

    public List<PaymentModel> findByOrderId(String orderId) {
        return list("orderId", orderId);
    }

    public void deleteById(UUID id) {
        delete("paymentId", id);
    }
}
