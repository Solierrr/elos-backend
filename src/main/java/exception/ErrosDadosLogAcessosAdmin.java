package exception;

public enum ErrosDadosLogAcessosAdmin implements GenericExceptionEnum {

  // Variações do enum
  SISTEMA_OPERACIONAL_INVALIDO(601, "O sistema operacional utilizado não é aceito", "sistema-operacional"),
  DATA_INVALIDA(602, "A data inserida é inválida, siga o padrão dia/mês/ano", "data-acesso"),
  NAVEGADOR_INVALIDO(603, "O navegador utilizado não é aceito", "navegador"),;

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampo;

  // Construtor
  ErrosDadosLogAcessosAdmin(int codigo, String mensagem, String nomeCampo) {
    this.codigo = codigo;
    this.mensagem = mensagem;
    this.nomeCampo = nomeCampo;
  }

  // Getters
  public int getCodigo() {
    return codigo;
  }

  public String getMensagem() {
    return mensagem;
  }

  public String getNomeCampo() {
    return nomeCampo;
  }

  // Métodos implementados
  @Override
  public String exibirMensagem() {
    return getMensagem();
  }

  @Override
  public String nomeCampoErro() {
    return getNomeCampo();
  }
}
