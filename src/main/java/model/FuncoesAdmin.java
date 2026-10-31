package model;

public enum FuncoesAdmin {

  // Variações do enum
  SUPERADMIN("SUPERADMIN"),
  REVISOR("REVISOR"),
  GERENTE("GERENTE"),
  ANALISTA("ANALISTA"),
  MODELADOR("MODELADOR");

  // Atributo
  private final String funcao;

  // Construtor
  FuncoesAdmin(String funcao) {
    this.funcao = funcao;
  }

  // Getter
  public String getFuncao() {
    return funcao;
  }

  // Método utilitario
  public static FuncoesAdmin descobrirFuncaoAdmin(String funcaoRecebida) {
    for (FuncoesAdmin funcao : FuncoesAdmin.values()) {
      if (funcao.getFuncao().equalsIgnoreCase(funcaoRecebida)) {
        return funcao;
      }
    }
    return null;
  }
}
