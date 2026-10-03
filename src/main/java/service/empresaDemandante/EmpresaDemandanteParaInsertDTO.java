package service.empresaDemandante;

import model.EmpresaDemandante;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

public record EmpresaDemandanteParaInsertDTO(String id, String idUsuario, String cnpj, String razaoSocial, String ehMandante) {

    public EmpresaDemandante construirEmpresaDemandante(){
        Long idTratado = id == null || id.isBlank() ? null : Long.parseLong(id);
        Long idUsuarioTratado = idUsuario == null || idUsuario.isBlank() ? null : Long.parseLong(idUsuario);

        String cnpjTratado = cnpj.strip().toUpperCase();
        String razaoSocialTratada = razaoSocial.strip();

        Boolean ehMandanteTratado = Boolean.parseBoolean(ehMandante);

        return new EmpresaDemandante(idTratado, idUsuarioTratado, cnpjTratado, razaoSocialTratada, ehMandanteTratado);
    }

}
