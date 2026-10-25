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
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UsuarioDAO implements GenericDAO {

  // Constante utilitaria
  private final static String TABELA = "usuario";

  // Métodos implementados

  //Esse DAO necessita de uma implementação diferente do insert pelo fato de que ele é uma entidade pai
  @Override
  public Long insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)
      throws IllegalArgumentException {
    throw new IllegalArgumentException("Existe parametros faltando!");
  }

  public Long insert(
      CriarInstrucaoDinamica criarInstrucaoDinamicaUsuario,
      CriarInstrucaoDinamica criarInstrucaoDinamicaFilho,
      String tabelaFilho,
      Connection connection)
      throws SQLException {

    PreparedStatement preparedStatementUsuario =
        connection.prepareStatement(
            criarInstrucaoDinamicaUsuario.construirInsert(TABELA), Statement.RETURN_GENERATED_KEYS);
    criarInstrucaoDinamicaUsuario.aplicarValoresDoPreparedStatement(preparedStatementUsuario);
    preparedStatementUsuario.executeUpdate();

    ResultSet resultSet = preparedStatementUsuario.getGeneratedKeys();
    resultSet.next();
    Long idCriado = resultSet.getLong(1);

    criarInstrucaoDinamicaFilho.setCampo("id_usuario", idCriado, Types.BIGINT);

    PreparedStatement preparedStatementFilho =
        connection.prepareStatement(criarInstrucaoDinamicaFilho.construirInsert(tabelaFilho));
    criarInstrucaoDinamicaFilho.aplicarValoresDoPreparedStatement(preparedStatementFilho);

    if (preparedStatementFilho.executeUpdate() < 1) {
      return null;
    }
    return idCriado;
  }

  @Override
  public List<Map<String, Object>> readAll(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
    List<Map<String, Object>> dados = new ArrayList<>();

    PreparedStatement preparedStatement =
        connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));
    criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

    // Uso do ResultSetMetaData para resolver os conflitos de campos com o mesmo valor ao realizar joins
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
