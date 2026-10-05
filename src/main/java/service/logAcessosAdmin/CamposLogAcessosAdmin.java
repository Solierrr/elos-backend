package service.logAcessosAdmin;

import service.GenericEnumCampos;

public enum CamposLogAcessosAdmin  implements GenericEnumCampos {

    ID("id"),
    ID_ADMIN("id_admin"),
    DATA_ACESSO("data_acesso"),
    SUCESSO("sucesso"),
    ENDERECO_IP("endereco_ip"),
    MOTIVO_FALHA("motivo_falha"),
    NAVEGADOR("navegador"),
    SISTEMA_OPERACIONAL("sistema_operacional"),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao"),
    INVALIDO("campo_do_fornecedor_invalido");

    private final String campoLogAcessosAdmin;

    CamposLogAcessosAdmin(String campoLogAcessosAdmin) {
        this.campoLogAcessosAdmin = campoLogAcessosAdmin;
    }

    public String getCampoLogAcessosAdmin() {
        return campoLogAcessosAdmin;
    }

    public static CamposLogAcessosAdmin descobrirCampoFornecedor(String campoLogAcessosAdminEntrada){
        if(campoLogAcessosAdminEntrada == null || campoLogAcessosAdminEntrada.isBlank()){
            return INVALIDO;
        }

        String campoLogAcessosAdminEntradaTratado = campoLogAcessosAdminEntrada.strip().toLowerCase();
        for(CamposLogAcessosAdmin campoLogAcessosAdmin : CamposLogAcessosAdmin.values()){
            if(campoLogAcessosAdmin.getCampoLogAcessosAdmin().equalsIgnoreCase(campoLogAcessosAdminEntradaTratado)){
                return campoLogAcessosAdmin;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
