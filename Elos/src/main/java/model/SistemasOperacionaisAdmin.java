package model;

public enum SistemasOperacionaisAdmin {
    WINDOWS("WINDOWS"),
    MAC("MAC"),
    LINUX("LINUX"),
    ANDROID("ANDROID"),
    IOS("IOS");

    private final String sistemaOperacional;

    SistemasOperacionaisAdmin(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public static SistemasOperacionaisAdmin descobrirSistemaOperacionalAdmin(String sistemaOperacionalRecebido){
        for(SistemasOperacionaisAdmin sop : SistemasOperacionaisAdmin.values()){
            if(sop.getSistemaOperacional().equalsIgnoreCase(sistemaOperacionalRecebido)){
                return sop;
            }
        }
        return null;
    }
}
