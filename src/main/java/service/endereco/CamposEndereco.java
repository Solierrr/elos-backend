package service.endereco;

import service.GenericEnumCampos;

public enum CamposEndereco implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  ID_USUARIO("id_usuario"),
  ESTADO("estado"),
  CIDADE("cidade"),
  BAIRRO("bairro"),
  CEP("cep"),
  LOGRADOURO("logradouro"),
  NUMERO("numero"),
  COMPLEMENTO("complemento"),
  INVALIDO("campo_do_fornecedor_invalido");

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposEndereco(String nomeCampo) {
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
