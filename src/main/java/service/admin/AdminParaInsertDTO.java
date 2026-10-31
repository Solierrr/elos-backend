package service.admin;

import model.Admin;
import model.FuncoesAdmin;
import util.Criptografia;

public record AdminParaInsertDTO(
    String funcao, String email, String senha, String nome, String emAtividade) {

  public Admin construirAdmin() {
    Boolean emAtividadeTratada = Boolean.parseBoolean(emAtividade);

    String emailTratado = email.toLowerCase().strip();
    String senhaTratada = senha == null || senha.isBlank() ? null : senha.strip();
    String nomeTratado = nome.strip();

    FuncoesAdmin funcaoTratada = FuncoesAdmin.descobrirFuncaoAdmin(funcao.toUpperCase().strip());

    Criptografia criptografia = new Criptografia();
    return new Admin(
        null,
        emailTratado,
        nomeTratado,
        criptografia.porEmBcrypt(senhaTratada),
        funcaoTratada,
        emAtividadeTratada);
  }
}
