package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.paymentMethods.PixModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PixRepository implements PanacheRepository<PixModel> {

    public Optional<PixModel> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }
}
