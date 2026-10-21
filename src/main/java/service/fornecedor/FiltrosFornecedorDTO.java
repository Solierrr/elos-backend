package service.fornecedor;

import util.Criptografia;

public record FiltrosFornecedorDTO(String idFiltro, String idUsuarioFiltro, String tipoFornecedorFiltro,
                                   String cnpjFiltro, String razaoSocialFiltro,
                                   String joinFiltro,
                                   String filtroRapido) {

    public Long formatarId() {
        return Long.parseLong(idFiltro.strip());
    }

    public Long formatarIdUsuario() {
        return Long.parseLong(idUsuarioFiltro.strip());
    }

    public String formatarTipoFornecedor() {
        return tipoFornecedorFiltro.strip().toUpperCase();
    }

    public String formatarCnpj() {
        return cnpjFiltro.strip().toUpperCase().replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
    }

    public Long formatarRazaoSocial() {
        return Long.parseLong(razaoSocialFiltro.strip());
    }

}
