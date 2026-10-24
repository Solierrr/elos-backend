package exception;

public enum ErrosDadosAdmin implements GenericExceptionEnum {

    NOME_VAZIO(105, "Nenhum nomeUpdate foi inserido", "nome"),
    NOME_TAMANHO_INVALIDO(106, "O tamanho do nome inserido é inválido, insira um nome com até 150 caracteres", "nome"),
    NOME_INVALIDO(107, "O nome inserido é inválido", "nome"),
    FUNCAO_INVALIDA(108, "A função inserida é inválida", "funcao"),
    EM_ATIVIDADE_INVALIDO(110, "A opção escolhida é inválida", "em-atividade"),

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
