package service.telefone;

import model.Telefone;
import model.TiposTelefone;

public record TelefoneParaInsertDTO(
    String telefone, String idUsuario, String tipo, String principal) {

  public Telefone construirTelefone() {
    String telefoneTratado =
        telefone.strip().replace("(", "").replace(")", "").replaceAll("\\s", "").replace("-", "");

    Long idUsuarioTratado = Long.parseLong(idUsuario);

    TiposTelefone tiposTelefone = TiposTelefone.descobrirTipoTelefone(tipo);

    Boolean principalTratado = Boolean.parseBoolean(principal);

    return new Telefone(null, idUsuarioTratado, telefoneTratado, tiposTelefone, principalTratado);
  }
}
