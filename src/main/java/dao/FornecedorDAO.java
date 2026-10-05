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
import model.Fornecedor;
import model.TiposFornecedor;
import model.TiposUsuario;

public final class FornecedorDAO implements GenericDAO<Fornecedor> {

    private static final String TABELA = "fornecedor";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

    @Override
    public List<Fornecedor> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        List<Fornecedor> fornecedores = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            fornecedores.add(new Fornecedor(
                    resultSet.getLong("id"),
                    resultSet.getLong("id_usuario"),
                    TiposUsuario.descobrirTipoUsuario(resultSet.getString("tipo_usuario")),
                    TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                    resultSet.getString("cnpj"),
                    resultSet.getString("razao_social")
            ));
        }

        return fornecedores;
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

}