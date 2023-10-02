package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.BoletoRepository;

@ApplicationScoped
public class BoletoService {

    @Inject
    BoletoRepository boletoRepository;
}
