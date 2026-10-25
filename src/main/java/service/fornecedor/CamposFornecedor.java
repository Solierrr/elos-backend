package service.fornecedor;

import service.GenericEnumCampos;

public enum CamposFornecedor implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  ID_USUARIO("id_usuario"),
  TIPO_USUARIO("tipo_usuario"),
  TIPO_FORNECEDOR("tipo_fornecedor"),
  CNPJ("cnpj"),
  RAZAO_SOCIAL("razao_social"),
  INVALIDO("campo_do_fornecedor_invalido");

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposFornecedor(String nomeCampo) {
    this.nomeCampo = nomeCampo;
  }

  // Getter
  public String getNomeCampo() {
    return nomeCampo;
  }

  // Método implementado
  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
