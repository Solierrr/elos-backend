package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;

import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Fornecedor;

public final class FornecedorDAO implements GenericDAO {

  private static final String TABELA = "fornecedor";

  @Override
  public Long insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement(
            criarInstrucaoDinamica.construirInsert(TABELA), Statement.RETURN_GENERATED_KEYS);
    criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

    if (preparedStatement.executeUpdate() >= 1) {
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      resultSet.next();
      return resultSet.getLong(1);
    }

    return null;
  }

  @Override
  public List<Map<String, Object>> readAll(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
    List<Map<String, Object>> dados = new ArrayList<>();

    PreparedStatement preparedStatement =
        connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));
    criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

    ResultSet resultSet = preparedStatement.executeQuery();
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    while (resultSet.next()) {
      Map<String, Object> valoresRegistroAtual = new LinkedHashMap<>();

      for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
        String nomeColuna = resultSetMetaData.getColumnName(i);

        if (nomeColuna.equalsIgnoreCase("id")
            && !resultSetMetaData.getTableName(i).equalsIgnoreCase(TABELA))
          valoresRegistroAtual.put(
              "id_" + resultSetMetaData.getTableName(i).toLowerCase(), resultSet.getObject(i));
        else valoresRegistroAtual.put(nomeColuna.toLowerCase(), resultSet.getObject(i));
      }

      dados.add(valoresRegistroAtual);
    }
    return dados;
  }

  @Override
  public GenericExceptionEnum update(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement(criarInstrucaoDinamica.construirUpdate(TABELA));

    criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

    return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
  }

  @Override
  public GenericExceptionEnum delete(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement(criarInstrucaoDinamica.construirDelete(TABELA));

    criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

    return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
  }
}
