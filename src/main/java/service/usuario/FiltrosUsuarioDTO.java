package service.usuario;

public record FiltrosUsuarioDTO(String idFiltro, String emailFiltro, String nomeFiltro, String tipoUsuarioFiltro, String raioProcuraKmFiltro, String joinFiltro, String filtroRapido){

  public Long formatarId(){
    return Long.parseLong(idFiltro.strip());
  }

  public String formatarEmail(){
    return emailFiltro.toLowerCase().strip();
  }

  public String formatarNome(){
    return nomeFiltro.strip();
  }

  public String formatarTipoUsuario(){
    return tipoUsuarioFiltro.toUpperCase().strip();
  }

  public Double formatarRaioProcura(){
    return Double.parseDouble(raioProcuraKmFiltro.strip().replace(',', '.'));
  }

}
