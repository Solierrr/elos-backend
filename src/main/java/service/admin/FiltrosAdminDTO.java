package service.admin;

public record FiltrosAdminDTO(
    String idFiltro,
    String emailFiltro,
    String nomeFiltro,
    String funcaoFiltro,
    String emAtividadeFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public String formatarEmail() {
    return emailFiltro.toLowerCase().strip();
  }

  public String formatarNome() {
    return nomeFiltro.strip();
  }

  public String formatarFuncao() {
    return funcaoFiltro.toUpperCase().strip();
  }

  public Boolean formatarEmAtividade() {
    return Boolean.parseBoolean(emAtividadeFiltro.strip());
  }
}
