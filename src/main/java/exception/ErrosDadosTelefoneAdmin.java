package exception;

public enum ErrosDadosTelefoneAdmin implements GenericExceptionEnum {

  TELEFONE_VAZIO(501, "Nenhum telefone foi inserido", "telefone"),
  TELEFONE_INVALIDO(502, "O telefone inserido é inválido", "telefone"),
  TIPO_VAZIO(503, "O tipo do telefone não foi inserido", "tipo"),
  TIPO_INVALIDO(504, "O tipo do telefone inserido é inválido", "tipo"),
  PRINCIPAL_INVALIDO(505, "Não foi informado se o telefone é ou não o seu principalFiltro", "principalFiltro"),
  ID_ADMIN_INVALIDO(506, "O id do admin inserido é inválido", "id-admin")
  ;


  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  ErrosDadosTelefoneAdmin(int codigo, String mensagem, String nomeCampoErro) {
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