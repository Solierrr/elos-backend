package service.usuario;

import static exception.ErrosGeraisDados.ATRIBUTO_NULL;

import model.TiposUsuario;
import model.Usuario;

public record UsuarioParaInsertDTO(String id, String tipoUsuario, String email, String senha, String nome, String raioProcuraKm){

    public Usuario construirUsuario(){
        Long idTratado = id != null && !id.isBlank() ? Long.parseLong(id.strip()) : null;
        Double raioProcuraKmTratado = raioProcuraKm == null || raioProcuraKm.isEmpty() ? null : Double.parseDouble(raioProcuraKm.strip());

        String emailTratado = email.toLowerCase().strip();
        String senhaTratada = senha == null || senha.isBlank() ? null : senha.strip();
        String nomeTratado = nome.strip();

        TiposUsuario tipoUsuarioTratado = TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip());

        return new Usuario(idTratado, emailTratado, senhaTratada, nomeTratado, tipoUsuarioTratado, raioProcuraKmTratado);
    }
}
