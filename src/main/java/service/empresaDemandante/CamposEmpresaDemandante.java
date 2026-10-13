package service.empresaDemandante;

import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.VAZIO;

import dao.AcoesInstrucao;
import java.sql.Types;
import service.GenericEnumCampos;

public enum CamposEmpresaDemandante implements GenericEnumCampos {

    ID("id", true, IGUAL, Types.BIGINT),
    ID_USUARIO("id_usuario", true, IGUAL, Types.BIGINT),
    TIPO_USUARIO("tipo_usuario", true, IGUAL, Types.VARCHAR),
    CNPJ("cnpj", true,  IGUAL, Types.VARCHAR),
    RAZAO_SOCIAL("razao_social", true, ILIKE, Types.VARCHAR),
    EH_MANDANTE("eh_mandante", true, IGUAL, Types.BOOLEAN),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true, VAZIO, null),
    INVALIDO("campo_do_fornecedor_invalido", true, VAZIO, null);

    private final String camposEmpresaDemandante;
    private final boolean acessivel;
    private final AcoesInstrucao acao;
    private final Integer dataType;

    CamposEmpresaDemandante(String camposEmpresaDemandante, boolean acessivel, AcoesInstrucao acao, Integer dataType) {
        this.camposEmpresaDemandante = camposEmpresaDemandante;
        this.acessivel = acessivel;
        this.acao = acao;
        this.dataType = dataType;
    }

    public String getCamposEmpresaDemandante() {
        return camposEmpresaDemandante;
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

    public static CamposEmpresaDemandante descobrirCampoEmpresaDemandante(String campsEmpresaDemandanteEntrada){
        if(campsEmpresaDemandanteEntrada == null || campsEmpresaDemandanteEntrada.isBlank()){
            return INVALIDO;
        }

        String campsEmpresaDemandanteEntradaTratado = campsEmpresaDemandanteEntrada.strip().toLowerCase();
        for(CamposEmpresaDemandante campoEmpresaDemandante : CamposEmpresaDemandante.values()){
            if(campoEmpresaDemandante.getCamposEmpresaDemandante().equalsIgnoreCase(campsEmpresaDemandanteEntradaTratado)){
                return campoEmpresaDemandante;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
