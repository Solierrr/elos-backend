package model;

public enum TiposTelefone {

    PESSOAL("PESSOAL"),
    EMPRESARIAL("EMPRESARIAL");

    private final String tipoTelefone;

    TiposTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public static TiposTelefone descobrirTipoTelefone(String tipoTelefoneRecebido){
        if(tipoTelefoneRecebido == null){
            return null;
        }

        String tipoTelefoneRecebidoTratado = tipoTelefoneRecebido.toUpperCase().strip();
        for(TiposTelefone tiposTelefone : TiposTelefone.values()){
            if(tiposTelefone.tipoTelefone.equalsIgnoreCase(tipoTelefoneRecebidoTratado)){
                return tiposTelefone;
            }
        }
        return null;
    }
}
