package service.usuario;

import model.TiposUsuario;
import model.Usuario;
import util.Criptografia;

public record UsuarioParaInsertDTO(String tipoUsuario, String email, String senha, String nome, String raioProcuraKm){

    public Usuario construirUsuario(){
        Double raioProcuraKmTratado = raioProcuraKm == null || raioProcuraKm.isBlank() ? null : Double.parseDouble(raioProcuraKm.strip().replace(',', '.'));

        String emailTratado = email.toLowerCase().strip();
        String senhaTratada = senha == null || senha.isBlank() ? null : senha.strip();
        String nomeTratado = nome.strip();

        TiposUsuario tipoUsuarioTratado = TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip());

        Criptografia criptografia = new Criptografia();
        return new Usuario(null, emailTratado, criptografia.colocarHashNaSenha(senhaTratada), nomeTratado, tipoUsuarioTratado, raioProcuraKmTratado);
    }
}
