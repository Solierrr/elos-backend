package model;

public enum TiposUsuario {

  // Variações do enum
  FORNECEDOR("FORNECEDOR"),
  EMPRESA_DEMANDANTE("EMPRESA_DEMANDANTE"),
  PROFISSIONAL("PROFISSIONAL");

  // Atributo
  private final String tipoDoUsuario;

  // Construtor
  TiposUsuario(String tipoUsuario) {
    this.tipoDoUsuario = tipoUsuario;
  }

  // Getter
  public String getTipoDoUsuario() {
    return tipoDoUsuario;
  }

  // Método utilitario
  public static TiposUsuario descobrirTipoUsuario(String tipoUsuarioRecebido) {
    if (tipoUsuarioRecebido == null) {
      return null;
    }

    String tipoUsuarioRecebidoTratado = tipoUsuarioRecebido.toUpperCase().strip();
    for (TiposUsuario tiposUsuario : TiposUsuario.values()) {
      if (tiposUsuario.getTipoDoUsuario().equalsIgnoreCase(tipoUsuarioRecebidoTratado)) {
        return tiposUsuario;
      }
    }
    return null;
  }
}
