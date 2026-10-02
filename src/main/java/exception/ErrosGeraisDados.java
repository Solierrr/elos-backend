package exception;

public enum ErrosGeraisDados implements GenericExceptionEnum{

    ATRIBUTO_NULL(-1, "Atributo null"),
    VALIDACAO_OK(0, "Dado válido"),
    ID_INVALIDO(1, "O ID inserido é inválido"),
    ID_USUARIO_INVALIDO(2, "O ID do usuário inserido é inválido"),
    ID_USUARIO_NAO_REGISTRADO(3, "O ID do usuário inserido não foi encontrado"),
    SENTIDO_ORDER_BY_INVALIDO(4, "O sentido da ordenação inserido é inválido"),
    ORDER_BY_INVALIDO(5, "A clausula de ordenação é inválida"),
    WHERE_INVALIDO(6, "O nome da clausula de filtragem é inválido"),
    TIPO_USUARIO_INCOMPATIVEL(7, "O registro do tipo do usuário do id do usuário inserido é incompatível com esse cadastro"),
    CNPJ_VAZIO(8, "Nenhum CNPJ foi inserido"),
    CNPJ_TAMANHO_INVALIDO(9, "O CNPJ inserido não possui 14 dígitos"),
    CNPJ_FORMATO_INVALIDO(10, "O CNPJ inserido não possuí um formato de um CNPJ"),
    CNPJ_INVALIDO(11, "O CNPJ inserido é inválido"),
    RAZAO_SOCIAL_VAZIA(12, "Nenhuma razão social foi inserida"),
    RAZAO_SOCIAL_TAMANHO_INVALIDO(13, "A razão social inserida excede o tamanho limite de 150 caracteres"),
    RAZAO_SOCIAL_INVALIDA(14, "A razão social inserida é inválida")

    ;

    private final int codigo;
    private final String mensagem;

    ErrosGeraisDados(int codigo, String mensagem) {
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
