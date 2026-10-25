package exception;

public enum ErrosDadosEmpresaDemandante implements GenericExceptionEnum {

  // Variações do enum
  EH_MANDANTE_INVALIDO(201, "Não foi informado se a conta é ou não mandante", "eh-mandante");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosEmpresaDemandante(int codigo, String mensagem, String nomeCampoErro) {
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
