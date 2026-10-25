package model;

public enum TiposTelefone {

  // Variações do enum
  PESSOAL("PESSOAL"),
  EMPRESARIAL("EMPRESARIAL");

  // Atributo
  private final String tipoTelefone;

  // Construtor
  TiposTelefone(String tipoTelefone) {
    this.tipoTelefone = tipoTelefone;
  }

  // Getter
  public String getTipoTelefone() {
    return tipoTelefone;
  }

  // Método utilitario
  public static TiposTelefone descobrirTipoTelefone(String tipoTelefoneRecebido) {
    if (tipoTelefoneRecebido == null) {
      return null;
    }

    String tipoTelefoneRecebidoTratado = tipoTelefoneRecebido.toUpperCase().strip();
    for (TiposTelefone tiposTelefone : TiposTelefone.values()) {
      if (tiposTelefone.tipoTelefone.equalsIgnoreCase(tipoTelefoneRecebidoTratado)) {
        return tiposTelefone;
      }
    }
    return null;
  }
}
