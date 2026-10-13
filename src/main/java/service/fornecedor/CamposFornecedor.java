package service.fornecedor;


import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.VAZIO;

import dao.AcoesInstrucao;
import java.sql.Types;
import service.GenericEnumCampos;

public enum CamposFornecedor implements GenericEnumCampos {

    ID("id", true, IGUAL, Types.BIGINT),
    ID_USUARIO("id_usuario", true, IGUAL, Types.BIGINT),
    TIPO_USUARIO("tipo_usuario", true, IGUAL, Types.VARCHAR),
    TIPO_FORNECEDOR("tipo_fornecedor", true, IGUAL, Types.VARCHAR),
    CNPJ("cnpj", true, IGUAL, Types.VARCHAR),
    RAZAO_SOCIAL("razao_social", true, ILIKE, Types.VARCHAR),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true, VAZIO, null),
    INVALIDO("campo_do_fornecedor_invalido", true, VAZIO, null);

    private final String campoFornecedor;
    private final boolean acessivel;
    private final AcoesInstrucao acao;
    private final Integer dataType;

    CamposFornecedor(String campoFornecedor, boolean acessivel, AcoesInstrucao acao, Integer dataType) {
        this.campoFornecedor = campoFornecedor;
        this.acessivel = acessivel;
        this.acao = acao;
        this.dataType = dataType;
    }


    public String getCampoFornecedor() {
        return campoFornecedor;
    }

    public boolean isAcessivel() {
        return acessivel;
    }

    public AcoesInstrucao getAcao() {
        return acao;
    }

    public Integer getDataType() {
        return dataType;
    }

    public static CamposFornecedor descobrirCampoFornecedor(String campoFornecedorEntrada){
        if(campoFornecedorEntrada == null || campoFornecedorEntrada.isBlank()){
            return INVALIDO;
        }

        String campoFornecedorEntradaTratado = campoFornecedorEntrada.strip().toLowerCase();
        for(CamposFornecedor campoFornecedor : CamposFornecedor.values()){
            if(campoFornecedor.getCampoFornecedor().equalsIgnoreCase(campoFornecedorEntradaTratado)){
                return campoFornecedor;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
