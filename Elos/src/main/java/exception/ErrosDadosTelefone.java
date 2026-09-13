package exception;

public enum ErrosDadosTelefone implements GenericExceptionEnum {

    TELEFONE_VAZIO(501, "Nenhum telefone foi inserido"),
    TELEFONE_INVALIDO(502, "O telefone inserido é inválido"),
    TIPO_VAZIO(503, "O tipo do telefone não foi inserido"),
    TIPO_INVALIDO(504, "O tipo do telefone inserido é inválido"),
    PRINCIPAL_INVALIDO(505, "Não foi informado se o telefone é ou não o seu principal");

    private final int codigo;
    private final String mensagem;

    ErrosDadosTelefone(int codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    @Override
    public String exibirMensagem() {
        return getMensagem();
    }
}