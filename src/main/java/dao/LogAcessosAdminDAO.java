package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;

import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import model.LogAcessosAdmin;
import model.NavegadoresAdmin;
import model.SistemasOperacionaisAdmin;

public final class LogAcessosAdminDAO implements GenericDAO<LogAcessosAdmin> {

    private static final String TABELA = "log_acessos_admin";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

    @Override
    public List<LogAcessosAdmin> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        List<LogAcessosAdmin> logAcessosAdmins = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            logAcessosAdmins.add(new LogAcessosAdmin(
                    resultSet.getLong("id"),
                    resultSet.getLong("id_admin"),
                    resultSet.getObject("data_alteracao", OffsetDateTime.class),
                    resultSet.getBoolean("sucesso"),
                    resultSet.getString("endereco_ip"),
                    resultSet.getString("motivo_falha"),
                    NavegadoresAdmin.descobrirNavegadorAdmin(resultSet.getString("navegador")),
                    SistemasOperacionaisAdmin.descobrirSistemaOperacionalAdmin(resultSet.getString("sistema_operacional"))
            ));
        }
        return logAcessosAdmins;
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)  throws IllegalArgumentException{
        throw new IllegalArgumentException("Esse dao não suporta updates!");
    }

    @Override
    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        throw new IllegalArgumentException("Esse dao não suporta deletes!");
    }

}
