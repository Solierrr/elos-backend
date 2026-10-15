package service.admin;

import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.VAZIO;

import dao.AcoesInstrucao;
import java.sql.Types;
import service.GenericEnumCampos;
import service.fornecedor.CamposFornecedor;

public enum CamposAdmin implements GenericEnumCampos {

    ID("id", true, IGUAL, Types.BIGINT),
    EMAIL("email", true, IGUAL, Types.VARCHAR),
    NOME("nome", true, ILIKE, Types.VARCHAR),
    SENHA("senha", false, VAZIO, Types.VARCHAR),
    FUNCAO("funcao", true, ILIKE, Types.VARCHAR),
    EM_ATIVIDADE("em_atividade", true, IGUAL, Types.BOOLEAN),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true, VAZIO, null),
    INVALIDO("campo_do_usuario_invalido", true, VAZIO, null);

    private final String campoAdmin;
    private final boolean acessivel;
    private final AcoesInstrucao acao;
    private final Integer dataType;

    CamposAdmin(String campoAdmin, boolean acessivel, AcoesInstrucao acao, Integer dataType) {
        this.campoAdmin = campoAdmin;
        this.acessivel = acessivel;
        this.acao = acao;
        this.dataType = dataType;
    }


    public String getCampoAdmin () {
        return campoAdmin;
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

    public static CamposAdmin descobrirCampoAdmin(String camposAdminEntrada){
        if(camposAdminEntrada == null || camposAdminEntrada.isBlank()){
            return INVALIDO;
        }

        String camposAdminEntradaTratado = camposAdminEntrada.strip().toLowerCase();
        for(CamposAdmin camposAdmin : CamposAdmin.values()){
            if(camposAdmin.getCampoAdmin().equalsIgnoreCase(camposAdminEntradaTratado)){
                return camposAdmin;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
