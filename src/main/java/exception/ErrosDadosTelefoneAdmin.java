package exception;

public enum ErrosDadosTelefoneAdmin implements GenericExceptionEnum {

  // Variações do enum
  TELEFONE_VAZIO(901, "Nenhum telefone foi inserido", "telefone"),
  TELEFONE_INVALIDO(902, "O telefone inserido é inválido", "telefone"),
  TIPO_VAZIO(903, "O tipo do telefone não foi inserido", "tipo"),
  TIPO_INVALIDO(904, "O tipo do telefone inserido é inválido", "tipo"),
  PRINCIPAL_INVALIDO(905, "Não foi informado se o telefone é ou não o seu principalFiltro", "principalFiltro"),
  ID_ADMIN_INVALIDO(906, "O id do admin inserido é inválido", "id-admin");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosTelefoneAdmin(int codigo, String mensagem, String nomeCampoErro) {
    this.codigo = codigo;
    this.mensagem = mensagem;
    this.nomeCampoErro = nomeCampoErro;
  }

  // Getters
  public int getCodigo() {
    return codigo;
  }

  public String getMensagem() {
    return mensagem;
  }

  public String getNomeCampoErro() {
    return nomeCampoErro;
  }

  // Métodos implementados
  @Override
  public String exibirMensagem() {
    return getMensagem();
  }

  @Override
  public String nomeCampoErro() {
    return getNomeCampoErro();
  }
}