package service.empresaDemandante;

public record FiltrosEmpresaDemandanteDTO(
    String idFiltro,
    String idUsuarioFiltro,
    String cnpjFiltro,
    String razaoSocialFiltro,
    String ehMandanteFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdUsuario() {
    return Long.parseLong(idUsuarioFiltro.strip());
  }

  public String formatarCnpj() {
    return cnpjFiltro
        .strip()
        .toUpperCase()
        .replaceAll("\\.", "")
        .replaceAll("-", "")
        .replaceAll("/", "");
  }

  public String formatarRazaoSocial() {
    return razaoSocialFiltro.strip();
  }

  public Boolean formatarEhMandante() {
    return ehMandanteFiltro.equalsIgnoreCase("true");
  }

}
