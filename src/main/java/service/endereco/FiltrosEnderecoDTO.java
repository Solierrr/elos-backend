package service.endereco;

public record FiltrosEnderecoDTO(
    String idFiltro,
    String idUsuarioFiltro,
    String estadoFiltro,
    String cidadeFiltro,
    String bairroFiltro,
    String cepFiltro,
    String logradouroFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdUsuario() {
    return Long.parseLong(idUsuarioFiltro.strip());
  }

  public String formatarEstado() {
    return estadoFiltro.strip().toUpperCase();
  }

  public String formatarCep() {
    return cepFiltro.strip().replace("-", "");
  }

  public String formatarCidade() {
    return cidadeFiltro.strip();
  }

  public String formatarBairro() {
    return bairroFiltro.strip();
  }

  public String formatarLogradouro() {
    return logradouroFiltro.strip();
  }
}
