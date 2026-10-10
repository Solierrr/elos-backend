package service.profissional;

import model.Profissional;
import model.TiposUsuario;
import util.Criptografia;

public record ProfissionalParaInsertDTO(String tipoUsuario, String profissao, String cpf, String idFornecedor, String documentoContrato) {

    public Profissional construirProfissional(){
        Long idFornecedorTratado = idFornecedor != null && !idFornecedor.isBlank() ? Long.parseLong(idFornecedor.strip()) : null;

        String tipoUsuarioTratado = tipoUsuario.strip().toUpperCase();
        String profissaoTratada = profissao.strip();
        String cpfTratado = cpf.strip()
                    .replace(".", "")
                    .replace("-", "");

        String documentoContratoTratado = documentoContrato != null && !documentoContrato.isBlank() ? documentoContrato.strip() : null;

        Criptografia criptografia = new Criptografia();

        return new Profissional(null, null, TiposUsuario.descobrirTipoUsuario(tipoUsuarioTratado),
                profissaoTratada, criptografia.criptografarHmac(cpfTratado),
                criptografia.criptografarCpfAes(cpfTratado), idFornecedorTratado, documentoContratoTratado);
    }
}
