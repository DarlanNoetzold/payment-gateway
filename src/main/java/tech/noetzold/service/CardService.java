package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.CardRepository;

@ApplicationScoped
public class CardService {

    @Inject
    CardRepository cardRepository;
}
