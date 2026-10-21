package service.empresaDemandante;

import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.VAZIO;

import dao.AcoesInstrucao;
import java.sql.Types;
import service.GenericEnumCampos;

public enum CamposEmpresaDemandante implements GenericEnumCampos {

    ID("id"),
    ID_USUARIO("id_usuario"),
    TIPO_USUARIO("tipo_usuario"),
    CNPJ("cnpj"),
    RAZAO_SOCIAL("razao_social"),
    EH_MANDANTE("eh_mandante"),
    INVALIDO("campo_do_fornecedor_invalido");

    private final String camposEmpresaDemandante;

    CamposEmpresaDemandante(String camposEmpresaDemandante) {
        this.camposEmpresaDemandante = camposEmpresaDemandante;

    }

    public String getCamposEmpresaDemandante() {
        return camposEmpresaDemandante;
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
