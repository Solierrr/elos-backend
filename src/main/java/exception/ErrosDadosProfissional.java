package exception;

public enum ErrosDadosProfissional implements GenericExceptionEnum{

    ID_FORNECEDOR_INVALIDO(201, "O ID do fornecedor inserido é inválido", "id-fornecedor"),
    ID_FORNECEDOR_NAO_REGISTRADO(202, "O ID do fornecedor inserido não foi encontrado", "id-fornecedor"),
    PROFISSAO_VAZIA(203, "Nenhuma profissão foi inserida", "profissao"),
    PROFISSAO_TAMANHO_INVALIDO(204, "A profissão inserida possui um tamanho inválido, insira uma profissão com até 100 caracteres", "profissao"),
    PROFISSAO_INVALIDA(205, "A profissão inserida possui caracteres especiais", "profissao"),
    CPF_VAZIO(206, "Nenhum CPF foi inserido", "cpf"),
    CPF_TAMANHO_INVALIDO(207, "O CPF inserido não possui 11 dígitos", "cpf"),
    CPF_INVALIDO(208, "O CPF inserido é inválido", "cpf"),
    CPF_NAO_NUMERICO(209, "O CPF inserido não possuí apenas valores numéricos", "cpf");

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosDadosProfissional(int codigo, String mensagem, String nomeCampoErro) {
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
