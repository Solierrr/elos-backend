package service.empresaDemandante;

import model.EmpresaDemandante;
import model.TiposUsuario;

public record EmpresaDemandanteParaInsertDTO(
    String tipoUsuario, String cnpj, String razaoSocial) {

  public EmpresaDemandante construirEmpresaDemandante() {
    TiposUsuario tiposUsuarioTratado =
        TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip());

    String cnpjTratado =
        cnpj.strip().toUpperCase().replace(".", "").replace("-", "").replace("/", "");

    String razaoSocialTratada = razaoSocial.strip();


    return new EmpresaDemandante(
        null, null, tiposUsuarioTratado, cnpjTratado, razaoSocialTratada);
  }
}
