package service.admin;

import service.GenericEnumCampos;

public enum CamposAdmin implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  EMAIL("email"),
  NOME("nome"),
  SENHA("senha"),
  FUNCAO("funcao"),
  EM_ATIVIDADE("em_atividade"),
  INVALIDO(null);

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposAdmin(String nomeCampo) {
    this.nomeCampo = nomeCampo;
  }

  // Getter
  public String getNomeCampo() {
    return nomeCampo;
  }

  // Método implementados
  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
