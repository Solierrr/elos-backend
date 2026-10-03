package service.profissional;

import model.Profissional;
import model.TiposUsuario;
import util.Criptografia;

public record ProfissionalParaInsertDTO(String id, String idUsuario, String tipoUsuario, String profissao, String cpf, String idFornecedor) {

    public Profissional construirProfissional(){
        Long idTratado = id != null && !id.isBlank() ? Long.parseLong(id.strip()) : null;
        Long idUsuarioTratado = idUsuario != null && !idUsuario.isBlank() ? Long.parseLong(idUsuario.strip()) : null;
        Long idFornecedorTratado = idFornecedor != null && !idFornecedor.isBlank() ? Long.parseLong(idFornecedor.strip()) : null;

        String tipoUsuarioTratado = tipoUsuario.strip().toUpperCase();
        String profissaoTratada = profissao.strip();
        String cpfTratado = cpf.strip();

        Criptografia criptografia = new Criptografia();


        return new Profissional(idTratado, idUsuarioTratado, TiposUsuario.descobrirTipoUsuario(tipoUsuarioTratado), profissaoTratada, criptografia.criptografarHmac(cpfTratado), criptografia.criptografarCpfAes(cpfTratado), idFornecedorTratado);
    }
}
