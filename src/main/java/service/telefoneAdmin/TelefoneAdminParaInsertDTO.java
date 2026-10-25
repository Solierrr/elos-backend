package service.telefoneAdmin;

import model.Telefone;
import model.TelefoneAdmin;
import model.TiposTelefone;

public record TelefoneAdminParaInsertDTO(
    String telefone, String idAdmin, String tipo, String principal) {

  public TelefoneAdmin construirTelefoneAdmin() {
    String telefoneTratado =
        telefone.strip().replace("(", "").replace(")", "").replaceAll("\\s", "").replace("-", "");

    Long idAdminTratado = Long.parseLong(idAdmin);

    TiposTelefone tiposTelefone = TiposTelefone.descobrirTipoTelefone(tipo);

    Boolean principalTratado = Boolean.parseBoolean(principal);

    return new TelefoneAdmin(
        null, idAdminTratado, telefoneTratado, tiposTelefone, principalTratado);
  }
}
