package dao;

import exception.GenericExceptionEnum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {

    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

    public List<T> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException;

}
