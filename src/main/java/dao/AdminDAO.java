package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;

import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Admin;
import model.FuncoesAdmin;

public final class AdminDAO implements GenericDAO<Admin>{

    private static String TABELA = "admin";

    @Override
    public Long insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA), Statement.RETURN_GENERATED_KEYS
        );
        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        if(preparedStatement.executeUpdate() >= 1){
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        }

        return null;
    }

    @Override
    public List<Admin> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        List<Admin> admins = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            admins.add(new Admin(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("nome"),
                    resultSet.getString("senha"),
                    FuncoesAdmin.descobrirFuncaoAdmin(resultSet.getString("funcao")),
                    resultSet.getBoolean("em_atividade")
            ));
        }
        return admins;
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)  throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirUpdate(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

    @Override
    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws IllegalArgumentException{
        throw new IllegalArgumentException("Esse dao não suporta remover registros!");
    }

}
