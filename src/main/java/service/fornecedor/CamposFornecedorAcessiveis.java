package service.fornecedor;


public enum CamposFornecedorAcessiveis {

    ID("id", false),
    ID_USUARIO("id_usuario", false),
    TIPO_FORNECEDOR("tipo_fornecedor", true),
    CNPJ("cnpj", false),
    RAZAO_SOCIAL("razao_social", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_fornecedor_invalido", false);

    private final String campoFornecedor;
    private final boolean multiplosRetornos;

    CamposFornecedorAcessiveis(String campoFornecedor, boolean multiplosRetornos) {
        this.campoFornecedor = campoFornecedor;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoFornecedor() {
        return campoFornecedor;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
    }

    public static CamposFornecedorAcessiveis descobrirCampoFornecedor(String campoFornecedorEntrada){
        if(campoFornecedorEntrada == null){
            return INVALIDO;
        }

        String campoFornecedorEntradaTratado = campoFornecedorEntrada.strip().toLowerCase();
        for(CamposFornecedorAcessiveis campoFornecedor : CamposFornecedorAcessiveis.values()){
            if(campoFornecedor.getCampoFornecedor().equalsIgnoreCase(campoFornecedorEntradaTratado)){
                return campoFornecedor;
            }
        }
        return INVALIDO;
    }

}
