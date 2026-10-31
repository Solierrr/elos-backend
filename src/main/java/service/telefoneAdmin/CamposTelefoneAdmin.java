package service.telefoneAdmin;

import service.GenericEnumCampos;

public enum CamposTelefoneAdmin implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  ID_ADMIN("id_admin"),
  TELEFONE("telefone"),
  TIPO("tipo"),
  PRINCIPAL("principal"),
  INVALIDO("campo_invalido");

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposTelefoneAdmin(String nomeCampo) {
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
