package service.profissional;

import service.GenericEnumCampos;

public enum CamposProfissional implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  ID_USUARIO("id_usuario"),
  TIPO_USUARIO("tipo_usuario"),
  PROFISSAO("profissao"),
  CPF_AES("cpf_aes"),
  CPF_HMAC("cpf_hmac"),
  ID_FORNECEDOR("id_fornecedor"),
  DOCUMENTO_CONTRATO("documento_contrato"),
  INVALIDO("campo_invalido");

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposProfissional(String nomeCampo) {
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
