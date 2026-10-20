package exception;

public enum ErrosDadosEndereco implements GenericExceptionEnum {

  CIDADE_VAZIA(901, "Nenhuma cidade foi inserida", "cidade"),
  CIDADE_INVALIDA(902, "Cidade inválida", "cidade"),
  BAIRRO_VAZIO(903, "Nenhum bairro foi inserido", "barrio"),
  BAIRRO_INVALIDO(904, "O bairro inserido foi inválido", "bairro"),
  CEP_VAZIO(905, "Nenhum CEP foi inserido", "cep"),
  CEP_INVALIDO(906, "O CEP inserido é inválido", "cep"),
  LOGRADOURO_VAZIO(907, "Nenhum logradouro foi inserido", "logradouro"),
  LOGRADOURO_INVALIDO(908, "O logradouro inserido é inválido", "logradouro"),
  NUMERO_VAZIO(909, "Nenhum número foi inserido", "numero"),
  NUMERO_INVALIDO(910, "O número inserido é inválido", "numero"),

  ;


  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  ErrosDadosEndereco(int codigo, String mensagem, String nomeCampoErro) {
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