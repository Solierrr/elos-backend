package service.historicoAlteracoesAdmin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record FiltrosHistoricoAlteracoesDTO(
    String idFiltro,
    String idAdminFiltro,
    String idRegistroFiltro,
    String acaoFiltro,
    String dataAlteracaoFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdAdmin() {
    return Long.parseLong(idAdminFiltro.strip());
  }

  public Long formatarIdRegistro() {
    return Long.parseLong(idRegistroFiltro.strip());
  }

  public String formatarAcao() {
    return acaoFiltro.toUpperCase().strip();
  }

  public LocalDate formatarDataAlteracao() {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    return LocalDate.parse(dataAlteracaoFiltro.strip(), formatador);
  }
}
