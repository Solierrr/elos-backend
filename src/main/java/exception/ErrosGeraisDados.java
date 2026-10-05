package exception;

public enum ErrosGeraisDados implements GenericExceptionEnum{

    ATRIBUTO_NULL(-1, "Atributo null", "null"),
    VALIDACAO_OK(0, "Dado válido", "valicacao-ok"),
    ID_INVALIDO(1, "O ID inserido é inválido", "id"),
    ID_USUARIO_INVALIDO(2, "O ID do usuário inserido é inválido", "id-usuario"),
    ID_USUARIO_NAO_REGISTRADO(3, "O ID do usuário inserido não foi encontrado", "id-usuario"),
    SENTIDO_ORDER_BY_INVALIDO(4, "O sentido da ordenação inserido é inválido", "order-by"),
    ORDER_BY_INVALIDO(5, "A clausula de ordenação é inválida", "order-by"),
    WHERE_INVALIDO(6, "O nome da clausula de filtragem é inválido", "where"),
    TIPO_USUARIO_INCOMPATIVEL(7, "O registro do tipo do usuário do id do usuário inserido é incompatível com esse cadastro", "tipo-usuario"),
    CNPJ_VAZIO(8, "Nenhum CNPJ foi inserido", "cnpj"),
    CNPJ_TAMANHO_INVALIDO(9, "O CNPJ inserido não possui 14 dígitos", "cnpj"),
    CNPJ_FORMATO_INVALIDO(10, "O CNPJ inserido não possuí um formato de um CNPJ", "cnpj"),
    CNPJ_INVALIDO(11, "O CNPJ inserido é inválido", "cpnj"),
    RAZAO_SOCIAL_VAZIA(12, "Nenhuma razão social foi inserida", "razao-social"),
    RAZAO_SOCIAL_TAMANHO_INVALIDO(13, "A razão social inserida excede o tamanho limite de 150 caracteres", "razao-social"),
    RAZAO_SOCIAL_INVALIDA(14, "A razão social inserida é inválida", "razao-social"),
    SENHA_VAZIA(15, "Nenhuma senha foi inserida", "senha"),
    SENHA_MENOR_QUE_OITO(16, "Insira uma senha com ao menos 8 caracteres", "senha"),
    SENHA_TAMANHO_INVALIDO(17, "A senha inserida possui um tamanho maior que o permitido, insira uma senha com até 60 caracteres", "senha"),
    SENHA_FRACA(18, "A senha deve possuir uma letra maiúscula, uma minuscula, um número e um caractere especial", "senha"),
    EMAIL_INVALIDO(19, "O email inserido é inválido", "email"),
    EMAIL_VAZIO(20, "Nenhum email foi inserido", "email"),
    EMAIL_TAMANHO_INVALIDO(21, "O email inserido possui um tamanho maior que o permitido, insira um email com até 256 caracteres", "email"),
    ;

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosGeraisDados(int codigo, String mensagem, String nomeCampoErro) {
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
