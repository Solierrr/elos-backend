package service.telefone;

import service.GenericEnumCampos;

public enum CamposTelefone implements GenericEnumCampos {

  //Variações do enum
  ID("id"),
  ID_USUARIO("id_usuario"),
  TELEFONE("telefone"),
  TIPO("tipo"),
  PRINCIPAL("principal"),
  INVALIDO("campo_invalido");

  //Atributo
  private final String nomeCampo;

  //Construtor
  CamposTelefone(String nomeCampo) {
    this.nomeCampo = nomeCampo;
  }

  //Getter
  public String getNomeCampo() {
    return nomeCampo;
  }

  // Método implementado
  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
