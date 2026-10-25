package exception;

public enum ErrosDadosAdmin implements GenericExceptionEnum {

  // Variações do enum
  NOME_VAZIO(101, "Nenhum nomeUpdate foi inserido", "nome"),
  NOME_TAMANHO_INVALIDO(102, "O tamanho do nome inserido é inválido, insira um nome com até 150 caracteres", "nome"),
  NOME_INVALIDO(103, "O nome inserido é inválido", "nome"),
  FUNCAO_INVALIDA(104, "A função inserida é inválida", "funcao"),
  EM_ATIVIDADE_INVALIDO(105, "A opção escolhida é inválida", "em-atividade");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosAdmin(int codigo, String mensagem, String nomeCampoErro) {
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
