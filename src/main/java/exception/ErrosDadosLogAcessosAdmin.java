package exception;

public enum ErrosDadosLogAcessosAdmin implements GenericExceptionEnum{

    SISTEMA_OPERACIONAL_INVALIDO("O sistema operacional utilizado não é aceito", null),
    DATA_INVALIDA( "A data inserida é inválida, siga o padrão dia/mês/ano", "data-acesso"),
    NAVEGADOR_INVALIDO("O navegador utilizado não é aceito", null);

    //Atributos
    private final String mensagem;
    private final String nomeCampo;

    //Construtor
    ErrosDadosLogAcessosAdmin(String mensagem, String nomeCampo) {
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
        return getMensagem();
    }

    @Override
    public String nomeCampoErro() {
        return getNomeCampo();
    }
}
