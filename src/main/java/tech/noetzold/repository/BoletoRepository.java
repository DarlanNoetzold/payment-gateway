package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.paymentMethods.BoletoModel;

@ApplicationScoped
public class BoletoRepository implements PanacheRepository<BoletoModel> {
}
