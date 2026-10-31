package service.empresaDemandante;

import model.EmpresaDemandante;

public record EmpresaDemandanteParaUpdateDTO(
    String idUpdate, String razaoSocialUpdate) {

  public EmpresaDemandante construirEmpresaDemandante() {

    String razaoSocialTratada =
        razaoSocialUpdate == null || razaoSocialUpdate.isBlank() ? null : razaoSocialUpdate.strip();


    return new EmpresaDemandante(
        Long.parseLong(idUpdate.strip()), null, null, null, razaoSocialTratada);
  }
}
