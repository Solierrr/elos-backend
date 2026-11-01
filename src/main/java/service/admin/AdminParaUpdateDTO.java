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

    Criptografia criptografia = new Criptografia();
    String senhaTratada = senhaUpdate == null || senhaUpdate.isBlank() ? null : criptografia.porEmBcrypt(senhaUpdate.strip());
    String nomeTratado = nomeUpdate.strip();

    return new Admin(
        Long.parseLong(idUpdate),
        emailTratado,
        senhaTratada,
        nomeTratado,
        null,
        emAtividadeTratada);
  }
}
