package service.empresaDemandante;

import model.EmpresaDemandante;

public record EmpresaDemandanteParaUpdateDTO(
    String idUpdate, String razaoSocialUpdate, String ehMandanteUpdate) {

  public EmpresaDemandante construirEmpresaDemandante() {

    String razaoSocialTratada =
        razaoSocialUpdate == null || razaoSocialUpdate.isBlank() ? null : razaoSocialUpdate.strip();
    Boolean ehMandanteTratado =
        ehMandanteUpdate == null || ehMandanteUpdate.isBlank()
            ? null
            : Boolean.parseBoolean(ehMandanteUpdate);

    return new EmpresaDemandante(
        Long.parseLong(idUpdate.strip()), null, null, null, razaoSocialTratada, ehMandanteTratado);
  }
}
