package exception;

public enum ErrosDadosFornecedor implements GenericExceptionEnum {

    TIPO_FORNECEDOR_VAZIO(301, "O tipo do fornecedor não foi inserido", "tipo-fornecedor"),
    TIPO_FORNECEDOR_INVALIDO(302, "O tipo do fornecedor inserido é inválido", "tipo-fornecedor"),
    ;

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosDadosFornecedor(int codigo, String mensagem, String nomeCampoErro) {
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
