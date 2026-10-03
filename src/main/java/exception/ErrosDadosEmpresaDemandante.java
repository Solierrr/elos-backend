package exception;

public enum ErrosDadosEmpresaDemandante implements GenericExceptionEnum {

    EH_MANDANTE_INVALIDO(401, "Não foi informado se a conta é ou não mandante", "eh-mandante");

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosDadosEmpresaDemandante(int codigo, String mensagem, String nomeCampoErro) {
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