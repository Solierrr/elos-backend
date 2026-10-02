package exception;

public enum ErrosGerais implements GenericExceptionEnum {


    ERRO_POR_VIOLACAO_DE_REGRA_DO_BD(-1, "Um ou mais dados inseridos estão inválidos"),
    ERRO_GENERICO_NO_BD(-2, "Um possível erro de conexão ocorreu, cheque sua conexão de internet e tente novamente!"),
    ERRO_GENERICO(-3, "Algo inesperado aconteceu, tente novamente!"),
    REGISTRO_NAO_ENCONTRADO(-4, "O registro buscado não foi encontrado, verifique se os dados inseridos estão corretos"),
    REGISTROS_NAO_ENCONTRADOS(-5, "Os registros buscados não foram encontrados, verifique se os dados inseridos estão corretos");

    private final int codigo;
    private final String mensagem;

    ErrosGerais(int codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public static ErrosGerais descobrirErroGeral(int codigo){
        for (ErrosGerais errosGerais : ErrosGerais.values()){
            if(errosGerais.getCodigo() == codigo){
                return errosGerais;
            }
        }
        return ERRO_GENERICO;
    }

    @Override
    public String exibirMensagem() {
        return getMensagem();
    }
}
