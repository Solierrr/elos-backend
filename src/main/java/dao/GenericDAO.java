package dao;

import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface GenericDAO {

  public Long insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)
      throws SQLException;

  public List<Map<String, Object>> readAll(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

  public GenericExceptionEnum update(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

  public GenericExceptionEnum delete(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;
}
