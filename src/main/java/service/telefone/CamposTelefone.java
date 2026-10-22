package service.telefone;

import service.GenericEnumCampos;

public enum CamposTelefone implements GenericEnumCampos {
  ID("id"),
  ID_USUARIO("id_usuario"),
  TELEFONE("telefone"),
  TIPO("tipo"),
  PRINCIPAL("principal"),
  INVALIDO("campo_invalido");

  private final String campoTelefone;

  CamposTelefone(String campoTelefone) {
    this.campoTelefone = campoTelefone;
  }

  public String getCampoTelefone() {
    return campoTelefone;
  }

  public static CamposTelefone descobrirCampoTelefone(String campoTelefoneEntrada) {
    if (campoTelefoneEntrada == null || campoTelefoneEntrada.isBlank()) {
      return INVALIDO;
    }

    String campoTelefoneEntradaTratada = campoTelefoneEntrada.strip().toLowerCase();
    for (CamposTelefone campoTelefone : CamposTelefone.values()) {
      if (campoTelefone.getCampoTelefone().equalsIgnoreCase(campoTelefoneEntradaTratada)) {
        return campoTelefone;
      }
    }
    return INVALIDO;
  }

  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
