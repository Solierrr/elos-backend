package model;

public enum NavegadoresAdmin {
    CHROME("CHROME"),
    FIREFOX("FIREFOX"),
    EDGE("EDGE"),
    EXPLORER("EXPLORER"),
    OPERA("OPERA"),
    SAFARI("SAFARI"),
    NAVEGADOR_INVALIDO("NAVEGADOR_INVALIDO");

    private final String navegador;

    NavegadoresAdmin(String navegador) {
        this.navegador = navegador;
    }

    public String getNavegador() {
        return navegador;
    }

    public static NavegadoresAdmin descobrirNavegadorAdmin(String userAgent) throws IllegalArgumentException{
        if (userAgent == null || userAgent.isBlank())
            return NAVEGADOR_INVALIDO;

        if (userAgent.contains("OPR/") || userAgent.contains("Opera")) return OPERA;
        if (userAgent.contains("Edg/") || userAgent.contains("Edge/")) return EDGE;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident/")) return EXPLORER;
        if (userAgent.contains("Firefox/")) return FIREFOX;
        if (userAgent.contains("Chrome/")) return CHROME;
        if (userAgent.contains("Safari/")) return SAFARI;

        return NAVEGADOR_INVALIDO;
    }

}
