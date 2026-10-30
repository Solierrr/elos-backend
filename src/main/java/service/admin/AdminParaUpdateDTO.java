package service.admin;

import model.Admin;
import util.Criptografia;

public record AdminParaUpdateDTO(
    String idUpdate,
    String emailUpdate,
    String senhaUpdate,
    String nomeUpdate,
    String emAtividadeUpdate) {

  public Admin construirAdmin() {
    Boolean emAtividadeTratada = Boolean.parseBoolean(emAtividadeUpdate);

    String emailTratado = emailUpdate.toLowerCase().strip();
    String senhaTratada = senhaUpdate == null || senhaUpdate.isBlank() ? null : senhaUpdate.strip();
    String nomeTratado = nomeUpdate.strip();

    Criptografia criptografia = new Criptografia();
    return new Admin(
        Long.parseLong(idUpdate),
        emailTratado,
        criptografia.porEmBcrypt(senhaTratada),
        nomeTratado,
        null,
        emAtividadeTratada);
  }
}
