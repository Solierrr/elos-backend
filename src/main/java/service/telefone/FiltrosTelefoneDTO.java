package service.telefone;

import model.TiposTelefone;

public record FiltrosTelefoneDTO(
    String idFiltro,
    String idUsuarioFiltro,
    String telefoneFiltro,
    String tipoFiltro,
    String principalFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdUsuario() {
    return Long.parseLong(idUsuarioFiltro.strip());
  }

  public String formatarTelefone() {
    return telefoneFiltro.strip().replace("(", "").replace(")", "").replaceAll("\\s", "");
  }

  public String formatarTipo() {
    return TiposTelefone.descobrirTipoTelefone(tipoFiltro).getTipoTelefone();
  }

  public Boolean formatarPrincipal() {
    return principalFiltro.equalsIgnoreCase("true");
  }
}
