package model;

public enum TiposFornecedor {

    FABRICANTE("FABRICANTE"),
    DISTRIBUIDOR("DISTRIBUIDOR"),
    REVENDEDOR("REVENDEDOR"),
    INTEGRADOR("INTEGRADOR");

    private final String tipoFornecedor;

    TiposFornecedor(String tipoFornecedor) {
        this.tipoFornecedor = tipoFornecedor;
    }

    public String getTipoFornecedor() {
        return tipoFornecedor;
    }

    public static TiposFornecedor descobrirTipoFornecedor(String tipoFornecedorRecebido){
        if(tipoFornecedorRecebido == null){
            return null;
        }

        String tipoFornecedorRecebidoTratado = tipoFornecedorRecebido.strip().toUpperCase();
        for(TiposFornecedor tiposFornecedor : TiposFornecedor.values()){
            if(tiposFornecedor.tipoFornecedor.equalsIgnoreCase(tipoFornecedorRecebidoTratado)){
                return tiposFornecedor;
            }
        }
        return null;
    }
}
