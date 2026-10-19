package service.profissional;

import util.Criptografia;

public record FiltrosProfissionalDTO(String idFiltro, String idUsuarioFiltro, String profissaoFiltro,
                                     String cpfFiltro, String idFornecedorFiltro,
                                     String joinFiltro) {

  public Long formatarId() {
    return Long.parseLong(idFiltro.strip());
  }

  public Long formatarIdUsuario() {
    return Long.parseLong(idUsuarioFiltro.strip());
  }

  public String formatarProfissao() {
    return profissaoFiltro.strip();
  }

  public String formatarCpf() {
    Criptografia criptografia = new Criptografia();

    return criptografia.criptografarHmac(cpfFiltro.strip()
                    .replaceAll("\\.", "")
                    .replaceAll("-", ""));
  }

  public Long formatarIdFornecedor() {
    return Long.parseLong(idFornecedorFiltro.strip());
  }

}