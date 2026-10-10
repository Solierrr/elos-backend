package service.empresaDemandante;

import model.EmpresaDemandante;
import model.TiposUsuario;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

public record EmpresaDemandanteParaInsertDTO(String tipoUsuario, String cnpj, String razaoSocial, String ehMandante) {

    public EmpresaDemandante construirEmpresaDemandante(){
        TiposUsuario tiposUsuarioTratado = TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip());

        String cnpjTratado = cnpj.strip().toUpperCase()
                .replace(".", "")
                .replace("-", "")
                .replace("/", "");        String razaoSocialTratada = razaoSocial.strip();

        Boolean ehMandanteTratado = Boolean.parseBoolean(ehMandante);

        return new EmpresaDemandante(null, null, tiposUsuarioTratado, cnpjTratado, razaoSocialTratada, ehMandanteTratado);
    }

}
