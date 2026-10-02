package model;

public enum TiposUsuario {

    FORNECEDOR("FORNECEDOR"),
    EMPRESA_DEMANDANTE("EMPRESA_DEMANDANTE"),
    PROFISSIONAL("PROFISSIONAL");

    private final String tipoDoUsuario;

    TiposUsuario(String tipoUsuario) {
        this.tipoDoUsuario = tipoUsuario;
    }

    public String getTipoDoUsuario() {
        return tipoDoUsuario;
    }

    public static TiposUsuario descobrirTipoUsuario(String tipoUsuarioRecebido){
        if(tipoUsuarioRecebido == null){
            return null;
        }

        String tipoUsuarioRecebidoTratado = tipoUsuarioRecebido.toUpperCase().strip();
        for(TiposUsuario tiposUsuario : TiposUsuario.values()){
            if(tiposUsuario.getTipoDoUsuario().equalsIgnoreCase(tipoUsuarioRecebidoTratado)){
                return tiposUsuario;
            }
        }
        return null;
    }
}
