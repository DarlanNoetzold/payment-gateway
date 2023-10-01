package tech.noetzold.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BoletoModel {

    private String codigoDeBarras;
    private String linhaDigitavel;
    private String beneficiarioNome;
    private String beneficiarioCnpjCpf;
    private String beneficiarioEndereco;
    private String sacadoNome;
    private String sacadoCnpjCpf;
    private String sacadoEndereco;
    private double valor;
    private Date dataVencimento;
    private String numeroDocumento;
    private String instrucoesPagamento;
    private double multa;
    private double jurosAtraso;
    private String logoBanco;
    private String codigoBanco;
    private String identificacaoBoleto;
    private Date dataEmissao;
    private String localPagamento;
    private String codigoAutenticacao;
    private String informacoesContato;
}
