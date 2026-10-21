package service.profissional;

import service.GenericEnumCampos;

public enum CamposProfissional implements GenericEnumCampos {
  ID("id"),
  ID_USUARIO("id_usuario"),
  TIPO_USUARIO("tipo_usuario"),
  PROFISSAO("profissao"),
  CPF_AES("cpf_aes"),
  CPF_HMAC("cpf_hmac"),
  ID_FORNECEDOR("id_fornecedor"),
  DOCUMENTO_CONTRATO("documento_contrato"),
  INVALIDO("campo_invalido");

  private final String campoProfissional;

  CamposProfissional(String campoProfissional) {
    this.campoProfissional = campoProfissional;
  }

  public String getCampoProfissional() {
    return campoProfissional;
  }

  public static CamposProfissional descobrirCampoProfissional(String campoProfissionalEntrada) {
    if (campoProfissionalEntrada == null || campoProfissionalEntrada.isBlank()) {
      return INVALIDO;
    }

    String campoProfissionalEntradaTratada = campoProfissionalEntrada.strip().toLowerCase();
    for (CamposProfissional campoProfissional : CamposProfissional.values()) {
      if (campoProfissional
          .getCampoProfissional()
          .equalsIgnoreCase(campoProfissionalEntradaTratada)) {
        return campoProfissional;
      }
    }
    return INVALIDO;
  }

  @Override
  public boolean isValido() {
    return this != INVALIDO;
  }
}
