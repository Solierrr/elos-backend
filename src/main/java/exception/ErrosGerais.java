package exception;

public enum ErrosGerais implements GenericExceptionEnum {

    SUCESSO(0, "Sucesso na operação!", null),
    ERRO_POR_VIOLACAO_DE_REGRA_DO_BD(-1, "Um ou mais dados inseridos estão inválidos", null),
    ERRO_GENERICO_NO_BD(-2, "Um possível erro de conexão ocorreu, cheque sua conexão de internet e tente novamente!", null  ),
    ERRO_GENERICO(-3, "Algo inesperado aconteceu, tente novamente!", "erro"),
    REGISTRO_NAO_ENCONTRADO(-4, "O registro buscado não foi encontrado, verifique se os dados inseridos estão corretos", null),
    REGISTROS_NAO_ENCONTRADOS(-5, "Os registros buscados não foram encontrados, verifique se os dados inseridos estão corretos", null),
    ADMIN_NAO_REGISTRADO(-6, "Tentativa de login em um adm inexistente ", null),
    SENHA_INVALIDA(-7, "A senha inserida pelo usuário não corresponde com a registrada no banco", null),
    ADMIN_INATIVO(-8, "O administrador está inativo", null);

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosGerais(int codigo, String mensagem, String nomeCampoErro) {
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

    @Override
    public String nomeCampoErro() {
        return getNomeCampoErro();
    }
}
