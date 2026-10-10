package service.empresaDemandante;

import service.GenericEnumCampos;

public enum CamposEmpresaDemandante implements GenericEnumCampos {

    ID("id", false),
    ID_USUARIO("id_usuario", false),
    CNPJ("cnpj", false),
    RAZAO_SOCIAL("razao_social", true),
    EH_MANDANTE("eh_mandante", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_fornecedor_invalido", false);

    private final String camposEmpresaDemandante;
    private final boolean multiplosRetornos;

    CamposEmpresaDemandante(String camposEmpresaDemandante, boolean multiplosRetornos) {
        this.camposEmpresaDemandante = camposEmpresaDemandante;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoFornecedor() {
        return camposEmpresaDemandante;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
    }

    public static CamposEmpresaDemandante descobrirCampoEmpresaDemandante(String campsEmpresaDemandanteEntrada){
        if(campsEmpresaDemandanteEntrada == null || campsEmpresaDemandanteEntrada.isBlank()){
            return INVALIDO;
        }

        String campsEmpresaDemandanteEntradaTratado = campsEmpresaDemandanteEntrada.strip().toLowerCase();
        for(CamposEmpresaDemandante campoFornecedor : CamposEmpresaDemandante.values()){
            if(campoFornecedor.getCampoFornecedor().equalsIgnoreCase(campsEmpresaDemandanteEntradaTratado)){
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
