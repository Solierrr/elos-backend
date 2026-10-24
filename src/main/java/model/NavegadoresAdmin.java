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

    private static final String[] BASEADOS_CHROME = {
            "Brave/", "Vivaldi/", "SamsungBrowser/",
            "Ya/", "UCBrowser/", "QQBrowser/", "360SE/",
            "Maxthon/", "LBBROWSER/", "MiuiBrowser/", "UCWEB/"
    };

    public static NavegadoresAdmin descobrirNavegadorAdmin(String userAgent) throws IllegalArgumentException{
        if (userAgent == null || userAgent.isBlank())
            return NAVEGADOR_INVALIDO;

        if (userAgent.contains("Firefox/") || userAgent.contains("Gecko/")) return FIREFOX;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident/")) return EXPLORER;
        if (userAgent.contains("Safari/") && !userAgent.contains("Chrome/")) return SAFARI;

        if (userAgent.contains("OPR/") || userAgent.contains("Opera")) return OPERA;
        if (userAgent.contains("Edg/") || userAgent.contains("EdgiOS/")) return EDGE;

        for (String token : BASEADOS_CHROME) {
            if (userAgent.contains(token)) return NAVEGADOR_INVALIDO;
        }

        if (userAgent.contains("Chrome/")) return CHROME;


        return NAVEGADOR_INVALIDO;
    }

}
