package service.telefone;

import model.Telefone;
import model.TiposTelefone;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

public record TelefoneDadosDTO(String id, String idUsuario, String telefone, String tipo, String principal) {

    public Telefone construirTelefone(){
        long idConvertido = id != null && !id.isBlank() ? Long.parseLong(id.strip()) : ATRIBUTO_NULL.getCodigo();
        long idUsuarioConvertido = idUsuario != null && !idUsuario.isBlank() ? Long.parseLong(idUsuario.strip()) : ATRIBUTO_NULL.getCodigo();

        String telefoneTratado = telefone.strip();

        TiposTelefone tipoConvertido = TiposTelefone.descobrirTipoTelefone(tipo());

        boolean principalConvertido = Boolean.parseBoolean(principal);

        return new Telefone(idConvertido, idUsuarioConvertido, telefoneTratado, tipoConvertido, principalConvertido);
    }
}
