package exception;

public enum ErrosDadosEmpresaDemandante implements GenericExceptionEnum {

    EH_MANDANTE_INVALIDO(401, "Não foi informado se a conta é ou não mandante");

    private final int codigo;
    private final String mensagem;

    ErrosDadosEmpresaDemandante(int codigo, String mensagem) {
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