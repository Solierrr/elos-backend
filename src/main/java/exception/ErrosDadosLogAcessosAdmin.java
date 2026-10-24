package exception;

public enum ErrosDadosLogAcessosAdmin implements GenericExceptionEnum{

    SISTEMA_OPERACIONAL_INVALIDO(222,"O sistema operacional utilizado não é aceito", null),
    DATA_INVALIDA( 223,"A data inserida é inválida, siga o padrão dia/mês/ano", "data-acesso"),
    NAVEGADOR_INVALIDO(224,"O navegador utilizado não é aceito", null);

    //Atributos
    private final int codigo;
    private final String mensagem;
    private final String nomeCampo;

    //Construtor
    ErrosDadosLogAcessosAdmin(int codigo, String mensagem, String nomeCampo) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.nomeCampo = nomeCampo;
    }

    //Getters
    public int getCodigo() {
        return codigo;
    }

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
