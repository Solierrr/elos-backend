package exception;

public enum ErrosDadosUsuario implements GenericExceptionEnum {

  // Variações do enum
  RAIO_PROCURA_KM_NAO_NUMERICO(1001, "O raio de procura em km inserido não é um valor numérico", "raio-procura-km"),
  RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO(1002, "Insira raios de procura em km maiores que zero", "raio-procura-km"),
  TIPO_USUARIO_VAZIO(1003, "Nenhum tipo de usuário foi inserido", "tipo-usuario"),
  TIPO_USUARIO_INVALIDO(1004, "O tipo de usuário que foi inserido é inválido", "tipo-usuario"),
  NOME_VAZIO(1005, "Nenhum nome foi inserido", "nome"),
  NOME_TAMANHO_INVALIDO(1006, "O tamanho do nome inserido é inválido, insira um nome com até 150 caracteres", "nome"),
  NOME_INVALIDO(1007, "O nome inserido é inválido", "nome"),
  IMPOSSIVEL_VALIDAR_NOME(1008, "Não foi possível validar o nome pois o tipo de usuário inserido é inválido", "nome"),
  RAIOS_PROCURA_KM_NAO_NUMERICO(1009, "Um ou mais raios de procura em km inseridos não são valores numéricos", "raio-procura-km"),
  RAIO_PROCURA_KM_TAMANHO_INVALIDO(1010, "O raio de procura em km inserido excede o limite aceito de 999km", "raio-procura-km");

  // Atributos
  private final int codigo;
  private final String mensagem;
  private final String nomeCampoErro;

  // Construtor
  ErrosDadosUsuario(int codigo, String mensagem, String nomeCampoErro) {
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
  ;
}
