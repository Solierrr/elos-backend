package service.fornecedor;


import model.Fornecedor;
import model.TiposFornecedor;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

public record FornecedorDadosDTO(String id, String idUsuario, String tipoFornecedor, String cnpj, String razaoSocial) {

    public Fornecedor construirFornecedor(){
        long idTratado = id != null && !id.isBlank() ? Long.parseLong(id) : ATRIBUTO_NULL.getCodigo();
        long idUsuarioTratado = idUsuario != null && !idUsuario.isBlank() ? Long.parseLong(idUsuario) : ATRIBUTO_NULL.getCodigo();

        TiposFornecedor tiposFornecedorTratado = TiposFornecedor.descobrirTipoFornecedor(tipoFornecedor.toUpperCase().strip());
        String cnpjTratado = cnpj.strip();
        String razaoSocialTratada = razaoSocial.strip();

        return new Fornecedor(idTratado, idUsuarioTratado, tiposFornecedorTratado, cnpjTratado, razaoSocialTratada);
    }
}
