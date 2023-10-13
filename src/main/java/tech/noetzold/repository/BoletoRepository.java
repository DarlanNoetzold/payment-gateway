package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.paymentMethods.BoletoModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BoletoRepository implements PanacheRepository<BoletoModel> {
    public Optional<BoletoModel> findByIdOptional(UUID id) {
        return find("boletoId", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("boletoId", id);
    }
}
