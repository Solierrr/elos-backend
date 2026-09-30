package exception;

public enum ErrosDadosUsuario implements GenericExceptionEnum{

    RAIO_PROCURA_KM_NAO_NUMERICO(101,"O raio de procura em km inserido não é um valor numérico", "raio-procura-km"),
    RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO(102,"Insira raios de procura em km maiores que zero", "raio-procura-km"),
    TIPO_USUARIO_VAZIO(103,"Nenhum tipo de usuário foi inserido", "tipo-usuario"),
    TIPO_USUARIO_INVALIDO(104, "O tipo de usuário que foi inserido é inválido", "tipo-usuario"),
    NOME_VAZIO(105, "Nenhum nome foi inserido", "nome"),
    NOME_TAMANHO_INVALIDO(106, "O tamanho do nome inserido é inválido, insira um nome com até 150 caracteres", "nome"),
    NOME_INVALIDO(107, "O nome inserido é inválido", "nome"),
    SENHA_VAZIA(108, "Nenhuma senha foi inserida", "senha"),
    SENHA_MENOR_QUE_OITO(109, "Insira uma senha com ao menos 8 caracteres", "senha"),
    SENHA_TAMANHO_INVALIDO(110, "A senha inserida possui um tamanho maior que o permitido, insira uma senha com até 60 caracteres", "senha"),
    SENHA_FRACA(111, "A senha deve possuir uma letra maiúscula, uma minuscula, um número e um caractere especial", "senha"),
    EMAIL_INVALIDO(112, "O email inserido é inválido", "email"),
    EMAIL_VAZIO(113, "Nenhum email foi inserido", "email"),
    EMAIL_TAMANHO_INVALIDO(114, "O email inserido possui um tamanho maior que o permitido, insira um email com até 256 caracteres", "email"),
    IMPOSSIVEL_VALIDAR_NOME(115, "Não foi possível validar o nome pois o tipo de usuário inserido é inválido", "nome"),
    RAIOS_PROCURA_KM_NAO_NUMERICO(116,"Um ou mais raios de procura em km inseridos não são valores numéricos", "raio-procura-km"),
    RAIO_PROCURA_KM_TAMANHO_INVALIDO(117,"O raio de procura em km inserido excede o limite aceito de 999km", "raio-procura-km");

    private final int codigo;
    private final String mensagem;
    private final String nomeCampoErro;

    ErrosDadosUsuario(int codigo, String mensagem, String nomeCampoErro) {
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
    public String nomeCampoErro(){
        return getNomeCampoErro();
    };
}
