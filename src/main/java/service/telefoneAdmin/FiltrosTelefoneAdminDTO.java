package service.telefoneAdmin;

import model.TiposTelefone;

public record FiltrosTelefoneAdminDTO(
    String idFiltro,
    String idAdminFiltro,
    String telefoneFiltro,
    String tipoFiltro,
    String principalFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdAdmin() {
    return Long.parseLong(idAdminFiltro.strip());
  }

  public String formatarTelefone() {
    return telefoneFiltro.strip().replace("(", "").replace(")", "").replaceAll("\\s", "");
  }

  public TiposTelefone formatarTipo() {
    return TiposTelefone.descobrirTipoTelefone(tipoFiltro);
  }

  public Boolean formatarPrincipal() {
    return principalFiltro.equalsIgnoreCase("true");
  }
}
