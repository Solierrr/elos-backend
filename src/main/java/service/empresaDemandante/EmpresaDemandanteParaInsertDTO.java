package service.empresaDemandante;

import model.EmpresaDemandante;
import model.TiposUsuario;

public record EmpresaDemandanteParaInsertDTO(String tipoUsuario, String cnpj, String razaoSocial,
                                             String ehMandante) {

  public EmpresaDemandante construirEmpresaDemandante() {
    TiposUsuario tiposUsuarioTratado = TiposUsuario.descobrirTipoUsuario(
        tipoUsuario.toUpperCase().strip());

    String cnpjTratado = cnpj.strip().toUpperCase()
        .replaceAll("\\.", "")
        .replaceAll("-", "")
        .replaceAll("/", "");

    String razaoSocialTratada = razaoSocial.strip();

    Boolean ehMandanteTratado = Boolean.parseBoolean(ehMandante);

    return new EmpresaDemandante(null, null, tiposUsuarioTratado, cnpjTratado, razaoSocialTratada,
        ehMandanteTratado);
  }

}
