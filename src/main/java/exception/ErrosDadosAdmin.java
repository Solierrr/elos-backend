package exception;

public enum ErrosDadosAdmin implements GenericExceptionEnum {

    ADMIN_NAO_REGISTRADO(901, "Tentativa de login em um adm inexistente ", null),
    SENHA_INVALIDA(902, "A senha inserida pelo usuário não corresponde com a registrada no banco", null)
    ;

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosDadosAdmin(int codigo, String mensagem, String nomeCampoErro) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.nomeCampoErro = nomeCampoErro;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeCampoErro() {
        return nomeCampoErro;
    }

    @Override
    public String exibirMensagem() {
        return getMensagem();
    }

    @Override
    public String nomeCampoErro() {
        return getNomeCampoErro();
    }
}
