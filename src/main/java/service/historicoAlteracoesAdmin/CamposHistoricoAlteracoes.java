package service.historicoAlteracoesAdmin;

import service.GenericEnumCampos;
import service.logAcessosAdmin.CamposLogAcessosAdmin;

public enum CamposHistoricoAlteracoes   implements GenericEnumCampos {

  ID("id"),
  ID_ADMIN("id_admin"),
  ID_REGISTRO("id_registro"),
  TABELA_MODIFICADA("tabela_modificada"),
  ACAO("acao"),
  DADOS_ANTIGOS("dados_antigos"),
  DADOS_ATUALIZADOS("dados_atualizar"),
  DATA_ALTERACAO("data_alteracao"),
  INVALIDO("campo_do_acesso_invalido");

  private final String campoHistoricoAlteracoes;

  CamposHistoricoAlteracoes(String campoHistoricoAlteracoes) {
    this.campoHistoricoAlteracoes = campoHistoricoAlteracoes;
  }

  public String getCampoHistoricoAlteracoes() {
    return campoHistoricoAlteracoes;
  }

  public static CamposHistoricoAlteracoes descobrirCampoHistoricoAlteracoes(String campoHistoricoAlteracoesEntrada){
    if(campoHistoricoAlteracoesEntrada == null || campoHistoricoAlteracoesEntrada.isBlank()){
      return INVALIDO;
    }

    String campoLogAcessosAdminEntradaTratado = campoHistoricoAlteracoesEntrada.strip().toLowerCase();
    for(CamposHistoricoAlteracoes campoLogAcessosAdmin : CamposHistoricoAlteracoes.values()){
      if(campoLogAcessosAdmin.getCampoHistoricoAlteracoes().equalsIgnoreCase(campoLogAcessosAdminEntradaTratado)){
        return campoLogAcessosAdmin;
      }
    }
    return INVALIDO;
  }

  @Override
  public boolean isValido(){
    return this != INVALIDO;
  }

}