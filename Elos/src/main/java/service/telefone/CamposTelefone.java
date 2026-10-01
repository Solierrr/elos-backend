package service.telefone;

import service.GenericEnumCampos;


public enum CamposTelefone implements GenericEnumCampos {

    ID("id", false),
    ID_USUARIO("id_usuario", false),
    TELEFONE("telefone", false),
    TIPO("tipo", true),
    PRINCIPAL("principal", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_profissional_invalido", false);

    private final String campoTelefone;
    private final boolean multiplosRetornos;

    private CamposTelefone(String campoTelefone, boolean multiplosRetornos) {
        this.campoTelefone = campoTelefone;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoTelefone() {
        return campoTelefone;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
    }

    public static CamposTelefone descobrirCampoTelefone(String campoTelefoneEntrada){
        if(campoTelefoneEntrada == null || campoTelefoneEntrada.isBlank()){
            return INVALIDO;
        }

        String campoTelefoneEntradaTratada = campoTelefoneEntrada.strip().toLowerCase();
        for(CamposTelefone camposTelefone : CamposTelefone.values()){
            if(camposTelefone.getCampoTelefone().equalsIgnoreCase(campoTelefoneEntradaTratada)){
                return camposTelefone;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }
}
