package service.endereco;

import service.GenericEnumCampos;

public enum CamposEndereco implements GenericEnumCampos {

    ID("id", false),
    ID_USUARIO("id_usuario", false),
    ESTADO("estado", true),
    CIDADE("cidade", true),
    BAIRRO("bairro", true),
    CEP("cep", true),
    LOGRADOURO("logradouro", true),
    NUMERO("numero", true),
    COMPLEMENTO("complemento", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_profissional_invalido", false);

    private final String campoEndereco;
    private final boolean multiplosRetornos;

    private CamposEndereco(String campoEndereco, boolean multiplosRetornos) {
        this.campoEndereco = campoEndereco;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoEndereco() {
        return campoEndereco;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
    }

    public static CamposEndereco descobrirCampoEndereco(String campoEnderecoEntrada){
        if(campoEnderecoEntrada == null || campoEnderecoEntrada.isBlank()){
            return INVALIDO;
        }

        String campoEnderecoEntradaTratada = campoEnderecoEntrada.strip().toLowerCase();
        for(CamposEndereco camposEndereco : CamposEndereco.values()){
            if(camposEndereco.getCampoEndereco().equalsIgnoreCase(campoEnderecoEntradaTratada)){
                return camposEndereco;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }
}