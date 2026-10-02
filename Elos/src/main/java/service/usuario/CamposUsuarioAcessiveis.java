package service.usuario;

public enum CamposUsuarioAcessiveis {

    ID("id", false),
    TIPO_USUARIO("tipo_usuario", true),
    EMAIL("email", false),
    NOME("nome", true),
    RAIO_PROCURA_KM("raio_procura_km", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_usuario_invalido", false);

    private final String campoUsuario;
    private final boolean multiplosRetornos;

    CamposUsuarioAcessiveis(String campoUsuario, boolean multiplosRetornos) {
        this.campoUsuario = campoUsuario;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoUsuario() {
        return campoUsuario;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
    }

    public static CamposUsuarioAcessiveis descobrirCampoUsuario(String campoUsuarioEntrada){
        if(campoUsuarioEntrada == null){
            return INVALIDO;
        }

        String campoUsuarioEntradaTratado = campoUsuarioEntrada.strip().toLowerCase();
        for(CamposUsuarioAcessiveis campoUsuario : CamposUsuarioAcessiveis.values()){
            if(campoUsuario.getCampoUsuario().equalsIgnoreCase(campoUsuarioEntradaTratado)){
                return campoUsuario;
            }
        }
        return INVALIDO;
    }
}
