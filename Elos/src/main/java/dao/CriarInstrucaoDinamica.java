package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CriarInstrucaoDinamica {

    //Records
    private record CampoComValor(String campo, Object valor, int dataType) {}

    private record CondicaoWhere(String campo, AcoesInstrucao operacao, AcoesInstrucao operadorLogico,
                                 Object valor, int dataType) {}

    private record OrderBy(String campo, AcoesInstrucao sentido) {}

    private record JoinInfo(AcoesInstrucao tipo, String tabela, String condicao) {}

    //Atributos
    private final List<CampoComValor> camposInsertOuUpdate = new ArrayList<>();

    private final List<CondicaoWhere> condicoesWhere = new ArrayList<>();

    private final List<OrderBy> orderBys = new ArrayList<>();

    private final List<JoinInfo> joins = new ArrayList<>();

    //metodos para construir as instruções
    public String construirSelect(String tabela) {
        exigirTabela(tabela);

        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(tabela);

        if (!joins.isEmpty()) {
            sb.append(" ").append(construirJoin());
        }

        if (!condicoesWhere.isEmpty()) {
            sb.append(" where ").append(construirWhere());
        }

        if (!orderBys.isEmpty()) {
            sb.append(" ").append(construirOrderBy());
        }

        return sb.toString().strip();
    }

    public String construirDelete(String tabela) {
        exigirTabela(tabela);
        exigirWhere();

        return ("delete from " + tabela + " where " + construirWhere()).strip();
    }

    public String construirInsert(String tabela) {
        exigirTabela(tabela);

        if (camposInsertOuUpdate.isEmpty()) {
            throw new IllegalArgumentException("Nenhum campo inserido");
        }

        String[] interrogacoes = new String[camposInsertOuUpdate.size()];
        Arrays.fill(interrogacoes, "?");
        String listaInterrogacoes = String.join(", ", interrogacoes);

        StringBuilder camposEmStringBuilder = new StringBuilder();
        for (int i = 0; i < camposInsertOuUpdate.size(); i++) {
            if (i > 0) {
                camposEmStringBuilder.append(", ");
            }
            camposEmStringBuilder.append(camposInsertOuUpdate.get(i).campo());
        }
        String camposEmString = camposEmStringBuilder.toString();

        return ("insert into " + tabela
                + "(" + camposEmString + ")"
                + " values(" + listaInterrogacoes + ")").trim();
    }

    public String construirUpdate(String tabela) {
        exigirTabela(tabela);

        if (camposInsertOuUpdate.isEmpty()) {
            throw new IllegalArgumentException("Nenhum campo inserido");
        }

        exigirWhere();

        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(tabela).append(" set ");

        for (int i = 0; i < camposInsertOuUpdate.size(); i++) {
            String campo = camposInsertOuUpdate.get(i).campo();

            if (i > 0) {
                sb.append(", ");
            }

            sb.append(campo).append(" = coalesce(?, ").append(campo).append(")");
        }
        sb.append(" where ").append(construirWhere());

        return sb.toString().strip();
    }

    //metodos para adicionar elementos na instrução
    public void setCampo(String campo, Object valor, int dataType) {
        camposInsertOuUpdate.add(new CampoComValor(campo, valor, dataType));
    }

    public void setWhere(String campoWhere, AcoesInstrucao operacaoWhere, AcoesInstrucao operadorLogico,
                         Object valorWhere, int dataType) {
        condicoesWhere.add(new CondicaoWhere(campoWhere, operacaoWhere, operadorLogico, valorWhere, dataType));
    }

    public void setOrderBy(String campo, AcoesInstrucao sentido) {
        orderBys.add(new OrderBy(campo, sentido));
    }

    public void setJoin(AcoesInstrucao tipo, String tabela, String condicao) {
        if (tabela == null || tabela.isBlank()) {
            throw new IllegalArgumentException("Nenhuma tabela de join inserida");
        }
        if (condicao == null || condicao.isBlank()) {
            throw new IllegalArgumentException("Nenhuma condição de join inserida");
        }
        joins.add(new JoinInfo(tipo, tabela, condicao));
    }

    public void aplicarValoresDoPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        int index = 1;
        index = aplicarValores(preparedStatement, index);
        aplicarValoresWhere(preparedStatement, index);
    }

    //Utilitários para aplicar os valores no preparedStatement
    private int aplicarValores(PreparedStatement preparedStatement, int index) throws SQLException {
        for (CampoComValor campoComValor : camposInsertOuUpdate) {
            DataTypesUsados dataTypeAtual = DataTypesUsados.descobrirDataType(campoComValor.dataType());
            dataTypeAtual.realizarSet(preparedStatement, index, campoComValor.valor());
            index++;
        }
        return index;
    }

    private int aplicarValoresWhere(PreparedStatement preparedStatement, int index) throws SQLException {
        for (CondicaoWhere condicaoWhere : condicoesWhere) {
            DataTypesUsados dataTypeAtual = DataTypesUsados.descobrirDataType(condicaoWhere.dataType());
            dataTypeAtual.realizarSet(preparedStatement, index, condicaoWhere.valor());
            index++;
        }
        return index;
    }

    //Utilitários para construir partes da instrução
    private String construirWhere() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < condicoesWhere.size(); i++) {
            CondicaoWhere condicaoWhere = condicoesWhere.get(i);

            if (i > 0) {
                sb.append(condicaoWhere.operadorLogico().getAcao()).append(" ");
            }

            sb.append(condicaoWhere.campo())
                    .append(condicaoWhere.operacao().getAcao())
                    .append("? ");
        }
        return sb.toString().strip();
    }

    private String construirOrderBy() {
        StringBuilder sb = new StringBuilder("order by ");

        for (int i = 0; i < orderBys.size(); i++) {
            OrderBy orderBy = orderBys.get(i);

            if (i > 0) {
                sb.append(", ");
            }

            sb.append(orderBy.campo()).append(" ").append(orderBy.sentido().getAcao());
        }
        return sb.toString();
    }

    private String construirJoin() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < joins.size(); i++) {
            JoinInfo join = joins.get(i);

            if (i > 0) {
                sb.append(" ");
            }

            sb.append(join.tipo().getAcao())
                    .append(" ").append(join.tabela())
                    .append(" on ").append(join.condicao());
        }
        return sb.toString();
    }

    //Utilitários para caso de erro
    private void exigirTabela(String tabela) {
        if (tabela == null || tabela.isBlank()) {
            throw new IllegalArgumentException("Nenhuma tabela inserida");
        }
    }

    private void exigirWhere() {
        if (condicoesWhere.isEmpty()) {
            throw new IllegalArgumentException("Nenhum where inserido");
        }
    }
}