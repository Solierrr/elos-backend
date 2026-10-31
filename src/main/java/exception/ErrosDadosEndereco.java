package exception;

public enum ErrosDadosEndereco implements GenericExceptionEnum {

  // Variações do enum
  CIDADE_VAZIA(301, "Nenhuma cidade foi inserida", "cidade"),
  CIDADE_INVALIDA(302, "Cidade inválida", "cidade"),
  BAIRRO_VAZIO(303, "Nenhum bairro foi inserido", "barrio"),
  BAIRRO_INVALIDO(304, "O bairro inserido foi inválido", "bairro"),
  CEP_VAZIO(305, "Nenhum CEP foi inserido", "cep"),
  CEP_INVALIDO(306, "O CEP inserido é inválido", "cep"),
  LOGRADOURO_VAZIO(307, "Nenhum logradouro foi inserido", "logradouro"),
  LOGRADOURO_INVALIDO(308, "O logradouro inserido é inválido", "logradouro"),
  NUMERO_VAZIO(309, "Nenhum número foi inserido", "numero"),
  NUMERO_INVALIDO(310, "O número inserido é inválido", "numero"),
  ESTADO_VAZIO(311, "Nenhum estado foi escolhido", "estado"),
  ESTADO_INVALIDO(312, "O estado não é um estado brasileiroo", "estado"),
  COMPLEMENTO_INVALIDO(313,"O complemento inseerido é inválido", "complemento");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosEndereco(int codigo, String mensagem, String nomeCampoErro) {
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