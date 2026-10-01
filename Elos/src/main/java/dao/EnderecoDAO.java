package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.ERRO_GENERICO_NO_BD;
import static exception.ErrosGerais.ERRO_POR_VIOLACAO_DE_REGRA_DO_BD;
import static exception.ErrosGerais.SUCESSO;

import conexao.Conexao;
import exception.ErrosDoSQL;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Endereco;
import model.EstadosBrasileiros;

public class EnderecoDAO implements GenericDAO<Endereco> {

    private static final String TABELA = "endereco";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD : ERRO_GENERICO_NO_BD;
        }
    }

    @Override
    public List<Endereco> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        List<Endereco> enderecos = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                enderecos.add(new Endereco(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        EstadosBrasileiros.descobrirEstadoBrasileiroPorSigla(resultSet.getString("estado")),
                        resultSet.getString("cidade"),
                        resultSet.getString("bairro"),
                        resultSet.getString("cep"),
                        resultSet.getString("logradouro"),
                        resultSet.getString("numero"),
                        resultSet.getString("complemento")
                ));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return enderecos;
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirUpdate(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD : ERRO_GENERICO_NO_BD;
        }
    }

    @Override
    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirDelete(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD : ERRO_GENERICO_NO_BD;
        }
    }

}