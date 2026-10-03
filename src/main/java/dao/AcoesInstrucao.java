package dao;

public enum AcoesInstrucao {

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

    private final String acao;

    AcoesInstrucao(String acao) {
        this.acao = acao;
    }

    public String getAcao() {
        return acao;
    }
}