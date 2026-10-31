package service.logAcessosAdmin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.NavegadoresAdmin;
import model.SistemasOperacionaisAdmin;

public record FiltrosLogAcessosAdminDTO(
    String idFiltro,
    String idAdminFiltro,
    String dataAcessoFiltro,
    String navegadorFiltro,
    String sistemaOperacionalFiltro,
    String joinFiltro,
    String filtroRapido) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdAdmin() {
    return Long.parseLong(idAdminFiltro.strip());
  }

  public LocalDate formatarDataAcesso() {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    return LocalDate.parse(dataAcessoFiltro.strip(), formatador);
  }

  public String formatarNavegador() {
    return NavegadoresAdmin.valueOf(navegadorFiltro.toUpperCase().strip()).getNavegador();
  }

  public String formatarSistemaOperacional() {
    return SistemasOperacionaisAdmin.valueOf(sistemaOperacionalFiltro.toUpperCase().strip()).getSistemaOperacional();
  }
}
