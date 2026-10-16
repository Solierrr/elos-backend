package service.usuario;


import model.TiposUsuario;
import model.Usuario;
import util.Criptografia;

public record UsuarioParaUpdateDTO(String idUpdate, String emailUpdate, String senhaUpdate, String nomeUpdate, String raioProcuraKmUpdate) {

  public Usuario construirUsuario(){
    Double raioProcuraKmTratado = raioProcuraKmUpdate == null || raioProcuraKmUpdate.isBlank() ? null : Double.parseDouble(raioProcuraKmUpdate.strip().replace(',', '.'));

    String emailTratado = emailUpdate == null || emailUpdate.isBlank() ? null : emailUpdate.toLowerCase().strip();
    String senhaTratada = senhaUpdate == null || senhaUpdate.isBlank() ? null : senhaUpdate.strip();
    String nomeTratado = nomeUpdate == null || nomeUpdate.isBlank() ? null : nomeUpdate.strip();


    Criptografia criptografia = new Criptografia();
    return new Usuario(Long.parseLong(idUpdate.strip()), emailTratado, senhaUpdate== null ? null: criptografia.colocarHashNaSenha(senhaTratada), nomeTratado, null, raioProcuraKmTratado);
  }

}
