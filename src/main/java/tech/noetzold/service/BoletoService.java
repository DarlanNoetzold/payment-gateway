package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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


    private BoletoModel findById(UUID id){
        Optional<BoletoModel> optionalAlert = boletoRepository.findByIdOptional(id);
        return optionalAlert.orElse(null);
    }

    private void saveBoletoModel(BoletoModel boletoModel){
        boletoRepository.persist(boletoModel);
    }

    private void updateBoletoModel(BoletoModel boletoModel){
        if (boletoModel == null || boletoModel.getId() == null) {
            throw new WebApplicationException("Invalid data for boletoModel update", Response.Status.BAD_REQUEST);
        }

        BoletoModel existingboletoModel = findById(boletoModel.getId());
        if (existingboletoModel == null) {
            throw new WebApplicationException("boletoModel not found", Response.Status.NOT_FOUND);
        }

        existingboletoModel.setCodigoDeBarras(boletoModel.getCodigoDeBarras());
        existingboletoModel.setLinhaDigitavel(boletoModel.getLinhaDigitavel());
        existingboletoModel.setBeneficiarioNome(boletoModel.getBeneficiarioNome());
        existingboletoModel.setBeneficiarioCnpjCpf(boletoModel.getBeneficiarioCnpjCpf());
        existingboletoModel.setBeneficiarioEndereco(boletoModel.getBeneficiarioEndereco());
        existingboletoModel.setSacadoNome(boletoModel.getSacadoNome());
        existingboletoModel.setSacadoCnpjCpf(boletoModel.getSacadoCnpjCpf());
        existingboletoModel.setSacadoEndereco(boletoModel.getSacadoEndereco());
        existingboletoModel.setValor(boletoModel.getValor());
        existingboletoModel.setDataVencimento(boletoModel.getDataVencimento());
        existingboletoModel.setNumeroDocumento(boletoModel.getNumeroDocumento());
        existingboletoModel.setInstrucoesPagamento(boletoModel.getInstrucoesPagamento());
        existingboletoModel.setMulta(boletoModel.getMulta());
        existingboletoModel.setJurosAtraso(boletoModel.getJurosAtraso());
        existingboletoModel.setLogoBanco(boletoModel.getLogoBanco());
        existingboletoModel.setCodigoBanco(boletoModel.getCodigoBanco());
        existingboletoModel.setIdentificacaoBoleto(boletoModel.getIdentificacaoBoleto());
        existingboletoModel.setDataEmissao(boletoModel.getDataEmissao());
        existingboletoModel.setLocalPagamento(boletoModel.getLocalPagamento());
        existingboletoModel.setCodigoAutenticacao(boletoModel.getCodigoAutenticacao());
        existingboletoModel.setInformacoesContato(boletoModel.getInformacoesContato());

        boletoRepository.persist(existingboletoModel);
    }

    private void deleteBoletoModel(UUID id){
        boletoRepository.deleteById(id);
    }
}
