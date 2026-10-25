package dao;

public enum AcoesInstrucao {

  // Variações do enum
  ILIKE(" ilike "),
  MENOR_IGUAL(" <= "),
  MAIOR_IGUAL(" >= "),
  IGUAL(" = "),
  DIFERENTE(" <> "),
  AND(" and "),
  OR(" or "),
  VAZIO(" "),
  ORDEM_CRESCENTE("asc"),
  ORDEM_DECRESCENTE("desc"),
  INNER_JOIN("inner join"),
  LEFT_JOIN("left join"),
  RIGHT_JOIN("right join");

  // Atributo do enum
  private final String acao;

  // Construtor
  AcoesInstrucao(String acao) {
    this.acao = acao;
  }

  // Getter
  public String getAcao() {
    return acao;
  }
}
