package exception;

public enum ErrosDadosProfissional implements GenericExceptionEnum {

  // Variações do enum
  ID_FORNECEDOR_INVALIDO(701, "O ID do fornecedor inserido é inválido", "id-fornecedor"),
  ID_FORNECEDOR_NAO_REGISTRADO(702, "O ID do fornecedor inserido não foi encontrado", "id-fornecedor"),
  PROFISSAO_VAZIA(703, "Nenhuma profissão foi inserida", "profissao"),
  PROFISSAO_TAMANHO_INVALIDO(
      704,
      "A profissão inserida possui um tamanho inválido, insira uma profissão com até 100 caracteres",
      "profissao"),
  PROFISSAO_INVALIDA(705, "A profissão inserida possui caracteres especiais", "profissao"),
  CPF_VAZIO(706, "Nenhum CPF foi inserido", "cpf"),
  CPF_TAMANHO_INVALIDO(707, "O CPF inserido não possui 11 dígitos", "cpf"),
  CPF_INVALIDO(708, "O CPF inserido é inválido", "cpf"),
  CPF_NAO_NUMERICO(709, "O CPF inserido não possuí apenas valores numéricos", "cpf"),
  DOCUMENTO_CONTRATO_VAZIO(
      710, "O documento de contrato não pode ser vazio!", "documento-contrato");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosProfissional(int codigo, String mensagem, String nomeCampoErro) {
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
