package service.fornecedor;

import model.Fornecedor;
import model.TiposFornecedor;

public record FornecedorParaUpdateDTO(
    String idUpdate, String tipoFornecedorUpdate, String razaoSocialUpdate) {

  public Fornecedor construirFornecedor() {

    TiposFornecedor tiposFornecedorTratado =
        TiposFornecedor.descobrirTipoFornecedor(tipoFornecedorUpdate().toUpperCase().strip());

    String razaoSocialTratada =
        razaoSocialUpdate() == null || razaoSocialUpdate().isBlank()
            ? null
            : razaoSocialUpdate().strip();

    return new Fornecedor(
        Long.parseLong(idUpdate()), null, null, tiposFornecedorTratado, null, razaoSocialTratada);
  }
}
