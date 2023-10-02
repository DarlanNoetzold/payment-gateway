package tech.noetzold.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.repository.PixRepository;

@ApplicationScoped
public class PixService {

    @Inject
    PixRepository pixRepository;
}
