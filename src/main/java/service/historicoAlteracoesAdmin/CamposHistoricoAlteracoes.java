package service.historicoAlteracoesAdmin;

import service.GenericEnumCampos;

public enum CamposHistoricoAlteracoes implements GenericEnumCampos {

  // Variações do enum
  ID("id"),
  ID_ADMIN("id_admin"),
  ID_REGISTRO("id_registro"),
  TABELA_MODIFICADA("tabela_modificada"),
  ACAO("acao"),
  DADOS_ANTIGOS("dados_antigos"),
  DADOS_ATUALIZADOS("dados_atualizar"),
  DATA_ALTERACAO("data_alteracao"),
  INVALIDO("campo_do_acesso_invalido");

  // Atributo
  private final String nomeCampo;

  // Construtor
  CamposHistoricoAlteracoes(String nomeCampo) {
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
