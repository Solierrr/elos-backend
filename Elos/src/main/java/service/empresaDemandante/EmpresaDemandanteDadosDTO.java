package service.empresaDemandante;

import model.EmpresaDemandante;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

public record EmpresaDemandanteDadosDTO(String id, String idUsuario, String cnpj, String razaoSocial, String ehMandante) {

    public EmpresaDemandante construirEmpresaDemandante(){
        long idTratado = id == null || id.isBlank() ? ATRIBUTO_NULL.getCodigo() : Long.parseLong(id);
        long idUsuarioTratado = idUsuario == null || idUsuario.isBlank() ? ATRIBUTO_NULL.getCodigo() : Long.parseLong(idUsuario);

        String cnpjTratado = cnpj.strip().toUpperCase();
        String razaoSocialTratada = razaoSocial.strip();

        boolean ehMandanteTratado = Boolean.parseBoolean(ehMandante);

        return new EmpresaDemandante(idTratado, idUsuarioTratado, cnpjTratado, razaoSocialTratada, ehMandanteTratado);
    }

}
