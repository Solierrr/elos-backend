package model;

public enum SistemasOperacionaisAdmin {
    WINDOWS("WINDOWS"),
    MAC("MAC"),
    LINUX("LINUX"),
    ANDROID("ANDROID"),
    IOS("IOS"),
    SISTEMA_OPERACIONAL_INVALIDO("SISTEMA_OPERACIONAL_INVALIDO");

    private final String sistemaOperacional;

    SistemasOperacionaisAdmin(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public static SistemasOperacionaisAdmin descobrirSistemaOperacionalAdmin(String userAgent){
        if(userAgent == null || userAgent.isBlank())
            return SISTEMA_OPERACIONAL_INVALIDO;

        String sopTratado = userAgent.toLowerCase();

        if (sopTratado.contains("android")) {
            return ANDROID;
        }
        if (sopTratado.contains("iphone") || sopTratado.contains("ipad") || sopTratado.contains("ipod")) {
            return IOS;
        }
        if (sopTratado.contains("windows")) {
            return WINDOWS;
        }
        if (sopTratado.contains("macintosh") || sopTratado.contains("mac os x")) {
            return MAC;
        }
        if (sopTratado.contains("linux") || sopTratado.contains("x11")) {
            return LINUX;
        }
        return SISTEMA_OPERACIONAL_INVALIDO;
    }

}
