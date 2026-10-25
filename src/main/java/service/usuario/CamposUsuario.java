package service.usuario;

import service.GenericEnumCampos;

public enum CamposUsuario implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  TIPO_USUARIO("tipo_usuario"),
  SENHA("senha"),
  EMAIL("email"),
  NOME("nome"),
  RAIO_PROCURA_KM("raio_procura_km"),
  INVALIDO("campo_do_usuario_invalido");

  // Atributo
  private final String campoUsuario;

  // Construtor
  CamposUsuario(String campoUsuario) {
    this.campoUsuario = campoUsuario;
  }

  // Getter
  public String getCampoUsuario() {
    return campoUsuario;
  }

  // Método implementado
  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
