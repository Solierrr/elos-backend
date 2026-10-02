package exception;

public enum ErrosDadosProfissional implements GenericExceptionEnum{

    ID_FORNECEDOR_INVALIDO(201, "O ID do fornecedor inserido é inválido"),
    ID_FORNECEDOR_NAO_REGISTRADO(202, "O ID do fornecedor inserido não foi encontrado"),
    PROFISSAO_VAZIA(203, "Nenhuma profissão foi inserida"),
    PROFISSAO_TAMANHO_INVALIDO(204, "A profissão inserida possui um tamanho inválido, insira uma profissão com até 100 caracteres"),
    PROFISSAO_INVALIDA(205, "A profissão inserida possui caracteres especiais"),
    CPF_VAZIO(206, "Nenhum CPF foi inserido"),
    CPF_TAMANHO_INVALIDO(207, "O CPF inserido não possui 11 dígitos"),
    CPF_INVALIDO(208, "O CPF inserido é inválido"),
    CPF_NAO_NUMERICO(209, "O CPF inserido não possuí apenas valores numéricos");

    private final int codigo;
    private final String mensagem;

    ErrosDadosProfissional(int codigo, String mensagem) {
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
