package service.usuario;

public record UsuarioCadastroDTO(
    String email, String senha, String senhaConfirmacao, String raioProcuraKm) {}
