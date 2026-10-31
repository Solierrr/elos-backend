package exception;

public enum ErrosGerais implements GenericExceptionEnum {

  // Variações do enum
  SUCESSO(0, "Sucesso na operação!", null),
  ERRO_GENERICO(-1, "Algo inesperado aconteceu, tente novamente!", "erro"),
  SENHA_INVALIDA(-2, "A senha inserida pelo usuário não corresponde com a registrada no banco", null),
  ADMIN_INATIVO(-3, "O administrador está inativo", null);

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosGerais(int codigo, String mensagem, String nomeCampoErro) {
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
