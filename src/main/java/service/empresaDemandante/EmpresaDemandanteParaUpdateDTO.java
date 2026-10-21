package service.empresaDemandante;

import model.EmpresaDemandante;

public record EmpresaDemandanteParaUpdateDTO(
    String idUpdate, String razaoSocialUpdate, String ehMandanteUpdate) {

  public EmpresaDemandante construirEmpresaDemandante() {

    String razaoSocialTratada = razaoSocialUpdate.strip();
    Boolean ehMandanteTratado = Boolean.parseBoolean(ehMandanteUpdate);

    return new EmpresaDemandante(
        Long.parseLong(idUpdate.strip()), null, null, null, razaoSocialTratada, ehMandanteTratado);
  }
}
