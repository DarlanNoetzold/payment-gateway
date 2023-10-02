package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.PaymentModel;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<PaymentModel> {
}
