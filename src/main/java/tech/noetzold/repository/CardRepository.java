package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.model.paymentMethods.CardModel;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CardRepository implements PanacheRepository<CardModel> {
    public Optional<CardModel> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }
}
