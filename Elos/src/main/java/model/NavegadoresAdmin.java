package model;

public enum NavegadoresAdmin {
    CHROME("WINDOWS"),
    FIREFOX("MAC"),
    EDGE("LINUX"),
    EXPLORER("ANDROID"),
    OPERA("IOS"),
    SAFARI("SAFARI");

    private final String navegador;

    NavegadoresAdmin(String navegador) {
        this.navegador = navegador;
    }

    public String getNavegador() {
        return navegador;
    }

    public static NavegadoresAdmin descobrirNavegadorAdmin(String navegadorRecebido){
        for(NavegadoresAdmin navegador : NavegadoresAdmin.values()){
            if(navegador.getNavegador().equalsIgnoreCase(navegadorRecebido)){
                return navegador;
            }
        }
        return null;
    }

}
