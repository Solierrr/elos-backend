package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.ERRO_GENERICO_NO_BD;
import static exception.ErrosGerais.ERRO_POR_VIOLACAO_DE_REGRA_DO_BD;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGeraisDados.CNPJ_INVALIDO;
import static exception.ErrosGeraisDados.ID_USUARIO_INVALIDO;

import conexao.Conexao;
import exception.ErrosDoSQL;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.EmpresaDemandante;

public class EmpresaDemandanteDAO implements GenericDAO<EmpresaDemandante> {

    private static final String TABELA = "empresa_demandante";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            if (foiCausadoPorCnpjCadastrado(sqlException)) return CNPJ_INVALIDO;

            if (foiCausadoPorUsuarioCadastrado(sqlException)) return ID_USUARIO_INVALIDO;

            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState())
                    ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD
                    : ERRO_GENERICO_NO_BD;
        }
    }

    @Override
    public List<EmpresaDemandante> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        List<EmpresaDemandante> empresaDemandantes = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                empresaDemandantes.add(
                        new EmpresaDemandante(
                                resultSet.getLong("id"),
                                resultSet.getLong("id_usuario"),
                                resultSet.getString("cnpj"),
                                resultSet.getString("razao_social"),
                                resultSet.getBoolean("eh_mandante")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return empresaDemandantes;
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(criarInstrucaoDinamica.construirUpdate(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            if (foiCausadoPorCnpjCadastrado(sqlException)) return CNPJ_INVALIDO;

            if (foiCausadoPorUsuarioCadastrado(sqlException)) return ID_USUARIO_INVALIDO;

            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState())
                    ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD
                    : ERRO_GENERICO_NO_BD;
        }
    }

    @Override
    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(criarInstrucaoDinamica.construirDelete(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState())
                    ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD
                    : ERRO_GENERICO_NO_BD;
        }
    }

    // Métodos auxiliares que são usados para ver se o erro foi causado por um valor unique jácadastrado,
    // isso foi usado para não realizar consultas desnecessárias para algo que o banco conseguiria barrar
    private boolean foiCausadoPorCnpjCadastrado(SQLException sqlException) {
        return "empresa_demandante_cnpj_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorUsuarioCadastrado(SQLException sqlException) {
        return "empresa_demandante_id_usuario_key".contains(sqlException.getMessage());
    }
}
