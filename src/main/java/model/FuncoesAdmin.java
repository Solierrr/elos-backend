package model;

public enum FuncoesAdmin {

    SUPERADMIN("SUPERADMIN"),
    REVISOR("REVISOR"),
    GERENTE("GERENTE"),
    ANALISTA("ANALISTA"),
    MODELADOR("MODELADOR");

    private final String funcao;

    FuncoesAdmin(String funcao) {
        this.funcao = funcao;
    }

    public String getFuncao() {
        return funcao;
    }

    public static FuncoesAdmin descobrirFuncaoAdmin(String funcaoRecebida){
        for(FuncoesAdmin funcao : FuncoesAdmin.values()){
            if(funcao.getFuncao().equalsIgnoreCase(funcaoRecebida)){
                return funcao;
            }
        }
        return null;
    }
}

