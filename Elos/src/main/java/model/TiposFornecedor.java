package model;

public enum TiposFornecedor {

    FABRICANTE("FABRICANTE"),
    DISTRIBUIDOR("MAC"),
    REVENDEDOR("ANDROID"),
    INTEGRADOR("INTEGRADOR");

    private final String tipoFornecedor;

    TiposFornecedor(String tipoFornecedor) {
        this.tipoFornecedor = tipoFornecedor;
    }

    public String getTipoFornecedor() {
        return tipoFornecedor;
    }

    public static TiposFornecedor descobrirTipoFornecedor(String tipoFornecedorRecebido){
        for(TiposFornecedor tiposFornecedor : TiposFornecedor.values()){
            if(tiposFornecedor.tipoFornecedor.equalsIgnoreCase(tipoFornecedorRecebido)){
                return tiposFornecedor;
            }
        }
        return null;
    }
}
