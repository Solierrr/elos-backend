package service.empresaDemandante;

import service.GenericEnumCampos;

public enum CamposEmpresaDemandante implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  ID_USUARIO("id_usuario"),
  TIPO_USUARIO("tipo_usuario"),
  CNPJ("cnpj"),
  RAZAO_SOCIAL("razao_social"),
  INVALIDO(null);

  // Atributo
  private final String nomeCampo;

  //Construtor
  CamposEmpresaDemandante(String nomeCampo) {
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
