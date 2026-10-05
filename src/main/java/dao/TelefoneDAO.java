package dao;

import static exception.ErrosDadosTelefone.TELEFONE_INVALIDO;
import static exception.ErrosGerais.*;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.Conexao;
import exception.ErrosDoSQL;
import exception.GenericExceptionEnum;
import model.TiposTelefone;
import model.Telefone;

public final class TelefoneDAO implements GenericDAO<Telefone> {

    private static final String TABELA = "telefone";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

    @Override
    public List<Telefone> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        List<Telefone> telefones = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            telefones.add(new Telefone(
                    resultSet.getLong("id"),
                    resultSet.getLong("id_usuario"),
                    resultSet.getString("telefone"),
                    TiposTelefone.descobrirTipoTelefone(resultSet.getString("tipo")),
                    resultSet.getBoolean("principal")
            ));
        }
        return telefones;
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)  throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirUpdate(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

    @Override
    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirDelete(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

//
//    } catch (SQLException sqlException) {
//        if(foiCausadoPorTelefoneCadastrado(sqlException))
//            return TELEFONE_INVALIDO;
//
//        return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD : ERRO_GENERICO_NO_BD;
//    }

}