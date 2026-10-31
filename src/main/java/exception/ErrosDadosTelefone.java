package exception;

public enum ErrosDadosTelefone implements GenericExceptionEnum {

  // Variações do enum
  TELEFONE_VAZIO(801, "Nenhum telefone foi inserido", "telefone"),
  TELEFONE_INVALIDO(802, "O telefone inserido é inválido", "telefone"),
  TIPO_VAZIO(803, "O tipo do telefone não foi inserido", "tipo"),
  TIPO_INVALIDO(804, "O tipo do telefone inserido é inválido", "tipo"),
  PRINCIPAL_INVALIDO(805, "Não foi informado se o telefone é ou não o seu principal", "principal");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosTelefone(int codigo, String mensagem, String nomeCampoErro) {
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
