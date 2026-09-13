package service.fornecedor;


import service.GenericEnumCampos;

public enum CamposFornecedor implements GenericEnumCampos {

    ID("id", false),
    ID_USUARIO("id_usuario", false),
    TIPO_FORNECEDOR("tipo_fornecedor", true),
    CNPJ("cnpj", false),
    RAZAO_SOCIAL("razao_social", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_fornecedor_invalido", false);

    private final String campoFornecedor;
    private final boolean multiplosRetornos;

    CamposFornecedor(String campoFornecedor, boolean multiplosRetornos) {
        this.campoFornecedor = campoFornecedor;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoFornecedor() {
        return campoFornecedor;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
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
