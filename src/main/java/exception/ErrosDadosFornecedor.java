package exception;

public enum ErrosDadosFornecedor implements GenericExceptionEnum {

    TIPO_FORNECEDOR_VAZIO(301, "O tipo do fornecedor não foi inserido"),
    TIPO_FORNECEDOR_INVALIDO(302, "O tipo do fornecedor inserido é inválido"),
    ;

    private final int codigo;
    private final String mensagem;

    ErrosDadosFornecedor(int codigo, String mensagem) {
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
