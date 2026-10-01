package service.profissional;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

import model.Profissional;

public record ProfissionalDadosDTO(String id, String idUsuario, String profissao, String cpf, String idFornecedor) {

    public Profissional construirProfissional(){
        long idTratado = id != null && !id.isBlank() ? Long.parseLong(id.strip()) : ATRIBUTO_NULL.getCodigo();
        long idUsuarioTratado = idUsuario != null && !idUsuario.isBlank() ? Long.parseLong(idUsuario.strip()) : ATRIBUTO_NULL.getCodigo();
        long idFornecedorTratado = idFornecedor != null && !idFornecedor.isBlank() ? Long.parseLong(idFornecedor.strip()) : ATRIBUTO_NULL.getCodigo();

        String profissaoTratada = profissao.strip();
        String cpfTratado = cpf.strip();

        return new Profissional(idTratado, idUsuarioTratado, profissaoTratada, cpfTratado, idFornecedorTratado);
    }
}
