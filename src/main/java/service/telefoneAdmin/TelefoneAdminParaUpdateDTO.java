package service.telefoneAdmin;

import model.Telefone;
import model.TelefoneAdmin;
import model.TiposTelefone;

public record TelefoneAdminParaUpdateDTO(
    String idUpdate, String telefoneUpdate, String tipoUpdate, String principalUpdate) {

  public TelefoneAdmin construirTelefoneAdmin() {
    String telefoneTratado =
        telefoneUpdate == null || telefoneUpdate.isBlank()
            ? null
            : telefoneUpdate
                .strip()
                .replace("(", "")
                .replace(")", "")
                .replaceAll("\\s", "")
                .replace("-", "");

    TiposTelefone tiposTelefone = TiposTelefone.descobrirTipoTelefone(tipoUpdate);

    Boolean principalTratado =
        principalUpdate == null || principalUpdate.isBlank()
            ? null
            : Boolean.parseBoolean(principalUpdate);

    return new TelefoneAdmin(
        Long.parseLong(idUpdate.strip()), null, telefoneTratado, tiposTelefone, principalTratado);
  }
}
