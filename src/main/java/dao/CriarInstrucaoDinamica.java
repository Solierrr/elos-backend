package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CriarInstrucaoDinamica {

  // Records utilizados na classe
  private record CampoComValor(String campo, Object valor, int dataType) {}

  private record CondicaoWhere(
      String campo,
      AcoesInstrucao operacao,
      AcoesInstrucao operadorLogico,
      Object valor,
      int dataType) {}

  private record OrderBy(String campo, AcoesInstrucao sentido) {}

  private record DadosJoin(
      AcoesInstrucao tipo, String tabelaDestino, String conectorOrigem, String conectorDestino) {}

  // Atributos
  private final List<CampoComValor> camposInsertOuUpdate = new ArrayList<>();

  private final List<CondicaoWhere> condicoesWhere = new ArrayList<>();

  private final List<OrderBy> orderBys = new ArrayList<>();

  private final List<DadosJoin> joins = new ArrayList<>();

  // Métodos para construir as instruções
  public String construirSelect(String tabela) {
    exigirTabela(tabela);

    StringBuilder sb = new StringBuilder();
    sb.append("select ").append(tabela).append(".* ");

    for (DadosJoin dadosJoin : joins)
      sb.append(", ").append(dadosJoin.tabelaDestino()).append(".* ");

    sb.append(" from ").append(tabela);

    if (!joins.isEmpty()) {
      sb.append(" ").append(construirJoin(tabela));
    }

    if (!condicoesWhere.isEmpty()) {
      sb.append(" where ").append(construirWhere(tabela));
    }

    if (!orderBys.isEmpty()) {
      sb.append(" ").append(construirOrderBy(tabela));
    }

    return sb.toString().strip();
  }

  public String construirDelete(String tabela) {
    exigirTabela(tabela);
    exigirWhere();

    return ("delete from " + tabela + " where " + construirWhere(tabela)).strip();
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

    return ("insert into "
            + tabela
            + "("
            + camposEmString
            + ")"
            + " values("
            + listaInterrogacoes
            + ")")
        .strip();
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

      //Uso do coalesce para que quando o usuário não digite um campo o valor salvo no banco persista
      sb.append(campo).append(" = coalesce(?, ").append(campo).append(")");
    }
    sb.append(" where ").append(construirWhere(tabela));

    return sb.toString().strip();
  }

  // Métodos para adicionar elementos na instrução
  public void setCampo(String campo, Object valor, int dataType) {
    camposInsertOuUpdate.add(new CampoComValor(campo, valor, dataType));
  }

  public void setWhere(
      String campoWhere,
      AcoesInstrucao operacaoWhere,
      AcoesInstrucao operadorLogico,
      Object valorWhere,
      int dataType) {
    Object valorTratado = tratarValorWhere(operacaoWhere, valorWhere);
    String campoWhereTratado = tratarCampoWhere(campoWhere, valorWhere);
    condicoesWhere.add(
        new CondicaoWhere(
            campoWhereTratado, operacaoWhere, operadorLogico, valorTratado, dataType));
  }

  public void setOrderBy(String campo, AcoesInstrucao sentido) {
    orderBys.add(new OrderBy(campo, sentido));
  }

  public void setJoin(
      AcoesInstrucao tipo, String tabelaDestino, String conectorOrigem, String conectorDestino) {
    if (tabelaDestino == null || tabelaDestino.isBlank()) {
      throw new IllegalArgumentException("Nenhuma tabela de join inserida");
    }
    if (conectorOrigem == null || conectorOrigem.isBlank()) {
      throw new IllegalArgumentException("O  conector 1 não foi inserido");
    }
    if (conectorDestino == null || conectorDestino.isBlank()) {
      throw new IllegalArgumentException("O  conector 2 não foi inserido");
    }
    joins.add(new DadosJoin(tipo, tabelaDestino, conectorOrigem, conectorDestino));
  }

  // Método utilitario para construir o PreparedStatement
  public void aplicarValoresDoPreparedStatement(PreparedStatement preparedStatement)
      throws SQLException {
    int index = 1;
    index = aplicarValores(preparedStatement, index);
    aplicarValoresWhere(preparedStatement, index);
  }

  // Utilitários para aplicar os valores no preparedStatement
  private int aplicarValores(PreparedStatement preparedStatement, int index) throws SQLException {
    for (CampoComValor campoComValor : camposInsertOuUpdate) {
      DataTypesUsados dataTypeAtual = DataTypesUsados.descobrirDataType(campoComValor.dataType());
      dataTypeAtual.realizarSet(preparedStatement, index, campoComValor.valor());
      index++;
    }
    return index;
  }

  private void aplicarValoresWhere(PreparedStatement preparedStatement, int index)
      throws SQLException {
    for (CondicaoWhere condicaoWhere : condicoesWhere) {
      DataTypesUsados dataTypeAtual = DataTypesUsados.descobrirDataType(condicaoWhere.dataType());
      dataTypeAtual.realizarSet(preparedStatement, index, condicaoWhere.valor());
      index++;
    }
  }

  // Utilitários para construir partes da instrução
  private String construirWhere(String tabela) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < condicoesWhere.size(); i++) {
      CondicaoWhere condicaoWhere = condicoesWhere.get(i);

      if (i > 0) {
        sb.append(condicaoWhere.operadorLogico().getAcao()).append(" ");
      }

      sb.append(tabela)
          .append(".")
          .append(condicaoWhere.campo())
          .append(condicaoWhere.operacao().getAcao())
          .append("? ");
    }
    return sb.toString().strip();
  }

  private String construirOrderBy(String tabelaOrigem) {
    StringBuilder sb = new StringBuilder("order by ");

    for (int i = 0; i < orderBys.size(); i++) {
      OrderBy orderBy = orderBys.get(i);

      if (i > 0) {
        sb.append(", ");
      }

      sb.append(tabelaOrigem)
          .append(".")
          .append(orderBy.campo())
          .append(" ")
          .append(orderBy.sentido().getAcao());
    }
    return sb.toString();
  }

  private String construirJoin(String tabelaOrigem) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < joins.size(); i++) {
      DadosJoin join = joins.get(i);

      if (i > 0) {
        sb.append(" ");
      }

      sb.append(join.tipo().getAcao())
          .append(" ")
          .append(join.tabelaDestino())
          .append(" on ")
          .append(tabelaOrigem)
          .append(".")
          .append(join.conectorOrigem())
          .append("=")
          .append(join.tabelaDestino())
          .append(".")
          .append(join.conectorDestino());
    }
    return sb.toString();
  }

  // Utilitários para tratar valores das condições
  private Object tratarValorWhere(AcoesInstrucao operacaoWhere, Object valorWhere) {
    if (operacaoWhere == AcoesInstrucao.ILIKE && valorWhere instanceof String valorString) {
      return "%" + valorString + "%";
    }
    return valorWhere;
  }

  private String tratarCampoWhere(String campoWhere, Object valorWhere) {
    return valorWhere instanceof LocalDate ? (campoWhere + "::date") : campoWhere;
  }

  // Utilitários para caso de erro
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
