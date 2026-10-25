package dao;

import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//Criada para garantir que todos os DAOs implementem todas as ações de um CRUD
public interface GenericDAO {

  //Métodos de assinatura
  public Long insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)
      throws SQLException;

  public List<Map<String, Object>> readAll(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

  public GenericExceptionEnum update(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

  public GenericExceptionEnum delete(
      CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;
}
