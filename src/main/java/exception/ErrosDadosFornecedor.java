package exception;

public enum ErrosDadosFornecedor implements GenericExceptionEnum {

  // Variações do enum
  TIPO_FORNECEDOR_VAZIO(401, "O tipo do fornecedor não foi inserido", "tipo-fornecedor"),
  TIPO_FORNECEDOR_INVALIDO(402, "O tipo do fornecedor inserido é inválido", "tipo-fornecedor");

  //Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  //Construtor
  ErrosDadosFornecedor(int codigo, String mensagem, String nomeCampoErro) {
    this.codigo = codigo;
    this.mensagem = mensagem;
    this.nomeCampoErro = nomeCampoErro;
  }

  //Getters
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
