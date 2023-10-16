package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.paymentMethods.BoletoModel;
import tech.noetzold.repository.BoletoRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BoletoService {

    @Inject
    BoletoRepository boletoRepository;


    @Transactional
    @CacheResult(cacheName = "boleto")
    public BoletoModel findBoletoModelById(UUID id){
        Optional<BoletoModel> optionalBoletoModel = boletoRepository.findByIdOptional(id);
        return optionalBoletoModel.orElse(new BoletoModel());
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "boleto")
    public BoletoModel saveBoletoModel(BoletoModel boletoModel){
        boletoRepository.persist(boletoModel);
        boletoRepository.flush();
        return boletoModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "boleto")
    public void updateBoletoModel(BoletoModel boletoModel){
        if (boletoModel == null || boletoModel.getBoletoId() == null) {
            throw new WebApplicationException("Invalid data for boletoModel update", Response.Status.BAD_REQUEST);
        }

        BoletoModel existingBoletoModel = findBoletoModelById(boletoModel.getBoletoId());
        if (existingBoletoModel == null) {
            throw new WebApplicationException("boletoModel not found", Response.Status.NOT_FOUND);
        }

        existingBoletoModel.setCodigoDeBarras(boletoModel.getCodigoDeBarras());
        existingBoletoModel.setLinhaDigitavel(boletoModel.getLinhaDigitavel());
        existingBoletoModel.setBeneficiarioNome(boletoModel.getBeneficiarioNome());
        existingBoletoModel.setBeneficiarioCnpjCpf(boletoModel.getBeneficiarioCnpjCpf());
        existingBoletoModel.setBeneficiarioEndereco(boletoModel.getBeneficiarioEndereco());
        existingBoletoModel.setSacadoNome(boletoModel.getSacadoNome());
        existingBoletoModel.setSacadoCnpjCpf(boletoModel.getSacadoCnpjCpf());
        existingBoletoModel.setSacadoEndereco(boletoModel.getSacadoEndereco());
        existingBoletoModel.setValor(boletoModel.getValor());
        existingBoletoModel.setDataVencimento(boletoModel.getDataVencimento());
        existingBoletoModel.setNumeroDocumento(boletoModel.getNumeroDocumento());
        existingBoletoModel.setInstrucoesPagamento(boletoModel.getInstrucoesPagamento());
        existingBoletoModel.setMulta(boletoModel.getMulta());
        existingBoletoModel.setJurosAtraso(boletoModel.getJurosAtraso());
        existingBoletoModel.setLogoBanco(boletoModel.getLogoBanco());
        existingBoletoModel.setCodigoBanco(boletoModel.getCodigoBanco());
        existingBoletoModel.setIdentificacaoBoleto(boletoModel.getIdentificacaoBoleto());
        existingBoletoModel.setDataEmissao(boletoModel.getDataEmissao());
        existingBoletoModel.setLocalPagamento(boletoModel.getLocalPagamento());
        existingBoletoModel.setCodigoAutenticacao(boletoModel.getCodigoAutenticacao());
        existingBoletoModel.setInformacoesContato(boletoModel.getInformacoesContato());

        boletoRepository.persist(existingBoletoModel);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "boleto")
    public void deleteBoletoModelById(UUID id){
        boletoRepository.deleteById(id);
    }
}
