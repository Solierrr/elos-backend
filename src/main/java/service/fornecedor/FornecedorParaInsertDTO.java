package service.fornecedor;


import model.Fornecedor;
import model.TiposFornecedor;
import model.TiposUsuario;

public record FornecedorParaInsertDTO(String tipoUsuario, String tipoFornecedor, String cnpj, String razaoSocial) {

    public Fornecedor construirFornecedor(){
        TiposUsuario tiposUsuarioTratado = TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip());
        TiposFornecedor tiposFornecedorTratado = TiposFornecedor.descobrirTipoFornecedor(tipoFornecedor.toUpperCase().strip());

        String cnpjTratado = cnpj.strip().toUpperCase()
                .replaceAll("\\.", "")
                .replaceAll("-", "")
                .replaceAll("/", "");

        String razaoSocialTratada = razaoSocial.strip();

        return new Fornecedor(null, null, tiposUsuarioTratado, tiposFornecedorTratado, cnpjTratado, razaoSocialTratada);
    }
}
