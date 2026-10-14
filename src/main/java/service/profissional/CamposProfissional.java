package service.profissional;


import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.VAZIO;

import dao.AcoesInstrucao;
import java.sql.Types;
import service.GenericEnumCampos;

public enum CamposProfissional implements GenericEnumCampos {

    ID("id", true, IGUAL, Types.BIGINT),
    ID_USUARIO("id_usuario", true, IGUAL, Types.BIGINT),
    TIPO_USUARIO("tipo_usuario", true, IGUAL, Types.BIGINT),
    PROFISSAO("profissao", true, ILIKE, Types.VARCHAR),
    CPF_AES("cpf_aes", true, VAZIO, Types.BINARY),
    CPF_HMAC("cpf_hmac", true, IGUAL, Types.VARCHAR),
    ID_FORNECEDOR("id_fornecedor", true, IGUAL, Types.BIGINT),
    DOCUMENTO_CONTRATO("documento_contrato", true, VAZIO, Types.VARCHAR),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true, VAZIO, null),
    INVALIDO("campo_do_profissional_invalido", true, VAZIO, null);

    private final String campoProfissional;
    private final boolean acessivel;
    private final AcoesInstrucao acao;
    private final Integer dataType;

    CamposProfissional(String campoProfissional, boolean acessivel, AcoesInstrucao acao, Integer dataType) {
        this.campoProfissional = campoProfissional;
        this.acessivel = acessivel;
        this.acao = acao;
        this.dataType = dataType;
    }

    public String getCampoProfissional() {
        return campoProfissional;
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

    public static CamposProfissional descobrirCampoProfissional(String campoProfissionalEntrada){
        if(campoProfissionalEntrada == null || campoProfissionalEntrada.isBlank()){
            return INVALIDO;
        }

        String campoProfissionalEntradaTratada = campoProfissionalEntrada.strip().toLowerCase();
        for(CamposProfissional campoProfissional : CamposProfissional.values()){
            if(campoProfissional.getCampoProfissional().equalsIgnoreCase(campoProfissionalEntradaTratada)){
                return campoProfissional;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
