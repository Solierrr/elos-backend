package service.logAcessosAdmin;

import service.GenericEnumCampos;

public enum CamposLogAcessosAdmin implements GenericEnumCampos {

  // Variações enum
  ID("id"),
  ID_ADMIN("id_admin"),
  DATA_ACESSO("data_acesso"),
  SUCESSO("sucesso"),
  ENDERECO_IP("endereco_ip"),
  MOTIVO_FALHA("motivo_falha"),
  NAVEGADOR("navegador"),
  SISTEMA_OPERACIONAL("sistema_operacional"),
  INVALIDO("campo_do_acesso_invalido");

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposLogAcessosAdmin(String nomeCampo) {
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
