package exception;

public enum ErrosDadosHistoricoAlteracoes implements GenericExceptionEnum {

  // Variações do enum
  ID_ADMIN_INVALIDO(501, "O id do admin inserido é inválido", "id-admin"),
  ID_REGISTRO_INVALIDO(502, "O id do registro inserido é inválido", "id-registro"),
  DATA_INVALIDA(503, "A data inserida é inválida, siga o padrão dia/mês/ano", "data-alteracao"),
  ACAO_INVALIDA(504, "A ação inserida é inválida", "acao");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosHistoricoAlteracoes(int codigo, String mensagem, String nomeCampoErro) {
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
