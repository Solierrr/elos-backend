package exception;

public enum ErrosDadosHistoricoAlteracoes implements GenericExceptionEnum {

  ID_ADMIN_INVALIDO(506, "O id do admin inserido é inválido", "id-admin"),
  ID_REGISTRO_INVALIDO(999, "O id do registro inserido é inválido", "id-registro"),
  DATA_INVALIDA( 456, "A data inserida é inválida, siga o padrão dia/mês/ano", "data-alteracao"),
  ACAO_INVALIDA(4565, "A ação inserida é inválida", "acao"),
  ;


  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  ErrosDadosHistoricoAlteracoes(int codigo, String mensagem, String nomeCampoErro) {
    this.codigo = codigo;
    this.mensagem = mensagem;
    this.nomeCampoErro = nomeCampoErro;
  }

  public int getCodigo() {
    return codigo;
  }

  public String getMensagem() {
    return mensagem;
  }

  public String getNomeCampoErro() {
    return nomeCampoErro;
  }

  @Override
  public String exibirMensagem() {
    return getMensagem();
  }

  @Override
  public String nomeCampoErro() {
    return getNomeCampoErro();
  }
}
