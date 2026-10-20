package service.fornecedor;

import service.GenericEnumCampos;

public enum CamposFornecedor implements GenericEnumCampos {
  ID("id"),
  ID_USUARIO("id_usuario"),
  TIPO_USUARIO("tipo_usuario"),
  TIPO_FORNECEDOR("tipo_fornecedor"),
  CNPJ("cnpj"),
  RAZAO_SOCIAL("razao_social"),
  INVALIDO("campo_do_fornecedor_invalido");

  private final String campoFornecedor;

  CamposFornecedor(String campoFornecedor) {
    this.campoFornecedor = campoFornecedor;
  }

  public String getCampoFornecedor() {
    return campoFornecedor;
  }

  public static CamposFornecedor descobrirCampoFornecedor(String campoFornecedorEntrada) {
    if (campoFornecedorEntrada == null || campoFornecedorEntrada.isBlank()) {
      return INVALIDO;
    }

    String campoFornecedorEntradaTratado = campoFornecedorEntrada.strip().toLowerCase();
    for (CamposFornecedor campoFornecedor : CamposFornecedor.values()) {
      if (campoFornecedor.getCampoFornecedor().equalsIgnoreCase(campoFornecedorEntradaTratado)) {
        return campoFornecedor;
      }
    }
    return INVALIDO;
  }

  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
