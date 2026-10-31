package model;

public enum TiposFornecedor {

  // Variações do enum
  FABRICANTE("FABRICANTE"),
  DISTRIBUIDOR("DISTRIBUIDOR"),
  REVENDEDOR("REVENDEDOR"),
  INTEGRADOR("INTEGRADOR");

  // Atributo
  private final String tipoFornecedor;

  // Construtor
  TiposFornecedor(String tipoFornecedor) {
    this.tipoFornecedor = tipoFornecedor;
  }

  // Getter
  public String getTipoFornecedor() {
    return tipoFornecedor;
  }

  // Método utilitario
  public static TiposFornecedor descobrirTipoFornecedor(String tipoFornecedorRecebido) {
    if (tipoFornecedorRecebido == null) {
      return null;
    }

    String tipoFornecedorRecebidoTratado = tipoFornecedorRecebido.strip().toUpperCase();
    for (TiposFornecedor tiposFornecedor : TiposFornecedor.values()) {
      if (tiposFornecedor.tipoFornecedor.equalsIgnoreCase(tipoFornecedorRecebidoTratado)) {
        return tiposFornecedor;
      }
    }
    return null;
  }
}
