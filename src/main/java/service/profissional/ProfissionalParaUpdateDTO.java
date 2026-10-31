package service.profissional;

import model.Profissional;

public record ProfissionalParaUpdateDTO(
    String idUpdate,
    String profissaoUpdate,
    String idFornecedorUpdate,
    String documentoContratoUpdate) {

  public Profissional construirProfissional() {
    Long idFornecedorTratado =
        idFornecedorUpdate != null && !idFornecedorUpdate.isBlank()
            ? Long.parseLong(idFornecedorUpdate.strip())
            : null;

    String profissaoTratada =
        profissaoUpdate == null || profissaoUpdate.isBlank() ? null : profissaoUpdate.strip();

    String documentoContratoTratado =
        documentoContratoUpdate != null && !documentoContratoUpdate.isBlank()
            ? documentoContratoUpdate.strip()
            : null;

    return new Profissional(
        Long.parseLong(idUpdate.strip()),
        null,
        null,
        profissaoTratada,
        null,
        null,
        idFornecedorTratado,
        documentoContratoTratado);
  }
}
