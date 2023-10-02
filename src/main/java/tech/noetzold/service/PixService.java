package tech.noetzold.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    public PixModel findPixModelById(UUID id){
        Optional<PixModel> optionalPixModel = pixRepository.findByIdOptional(id);
        return optionalPixModel.orElse(null);
    }

    public void savePixModel(PixModel pixModel){
        pixRepository.persist(pixModel);
    }

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

    public void deletePixModelById(UUID id){
        pixRepository.deleteById(id);
    }
}
