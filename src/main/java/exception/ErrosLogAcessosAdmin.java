package exception;

public enum ErrosLogAcessosAdmin implements GenericExceptionEnum{

    SISTEMA_OPERACIONAL_INVALIDO("O sistema operacional utilizado não é aceito", null),
    NAVEGADOR_INVALIDO("O navegador utilizado não é aceito", null);

    //Atributos
    private final String mensagem;
    private final String nomeCampo;

    //Construtor
    ErrosLogAcessosAdmin(String mensagem, String nomeCampo) {
        this.mensagem = mensagem;
        this.nomeCampo = nomeCampo;
    }

    //Getters
    public String getMensagem() {
        return mensagem;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    //Metodos da interface
    @Override
    public String exibirMensagem() {
        return "";
    }

    @Override
    public String nomeCampoErro() {
        return "";
    }
}
