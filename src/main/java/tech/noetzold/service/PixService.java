package tech.noetzold.service;


import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.paymentMethods.PixModel;
import tech.noetzold.repository.PixRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PixService {

    @Inject
    PixRepository pixRepository;

    @Transactional
    @CacheResult(cacheName = "pix")
    public PixModel findPixModelById(UUID id){
        Optional<PixModel> optionalPixModel = pixRepository.findByIdOptional(id);
        return optionalPixModel.orElse(null);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "pix")
    public PixModel savePixModel(PixModel pixModel){
        pixRepository.persist(pixModel);
        pixRepository.flush();
        return pixModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "pix")
    public void updatePixModel(PixModel pixModel){
        if (pixModel == null || pixModel.getId() == null) {
            throw new WebApplicationException("Invalid data for pixModel update", Response.Status.BAD_REQUEST);
        }

        PixModel existingPixModel = findPixModelById(pixModel.getId());
        if (existingPixModel == null) {
            throw new WebApplicationException("customerModel not found", Response.Status.NOT_FOUND);
        }

        existingPixModel.setChavePix(pixModel.getChavePix());
        existingPixModel.setDescricao(pixModel.getDescricao());
        existingPixModel.setIdentificadorTransacao(pixModel.getIdentificadorTransacao());

        pixRepository.persist(existingPixModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "pix")
    public void deletePixModelById(UUID id){
        pixRepository.deleteById(id);
    }
}
