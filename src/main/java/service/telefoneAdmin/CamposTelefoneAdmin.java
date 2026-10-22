package service.telefoneAdmin;

import service.GenericEnumCampos;

public enum CamposTelefoneAdmin implements GenericEnumCampos {
  ID("id"),
  ID_ADMIN("id_admin"),
  TELEFONE("telefone"),
  TIPO("tipo"),
  PRINCIPAL("principal"),
  INVALIDO("campo_invalido");

  private final String campoTelefone;

  CamposTelefoneAdmin(String campoTelefone) {
    this.campoTelefone = campoTelefone;
  }

  public String getCampoTelefone() {
    return campoTelefone;
  }

  public static CamposTelefoneAdmin descobrirCampoTelefone(String campoTelefoneEntrada) {
    if (campoTelefoneEntrada == null || campoTelefoneEntrada.isBlank()) {
      return INVALIDO;
    }

    String campoTelefoneEntradaTratada = campoTelefoneEntrada.strip().toLowerCase();
    for (CamposTelefoneAdmin campoTelefone : CamposTelefoneAdmin.values()) {
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
