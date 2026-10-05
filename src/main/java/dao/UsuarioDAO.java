package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;

import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.TiposUsuario;
import model.Usuario;

public final class UsuarioDAO implements GenericDAO {

    private static String TABELA = "usuario";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws IllegalArgumentException{
        throw new IllegalArgumentException("Existe parametros faltando!");
    }

    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamicaUsuario,
                                       CriarInstrucaoDinamica criarInstrucaoDinamicaFilho,
                                       String tabelaFilho, Connection connection) throws SQLException{

        PreparedStatement preparedStatementUsuario = connection.prepareStatement(criarInstrucaoDinamicaUsuario.construirInsert(TABELA));
        criarInstrucaoDinamicaUsuario.aplicarValoresDoPreparedStatement(preparedStatementUsuario);
        preparedStatementUsuario.executeUpdate();

        ResultSet resultSet = preparedStatementUsuario.getGeneratedKeys();
        resultSet.next();
        Long idCriado = resultSet.getLong(1);

        criarInstrucaoDinamicaFilho.setCampo("id_usuario", idCriado, Types.BIGINT);

        PreparedStatement preparedStatementFilho = connection.prepareStatement(
                criarInstrucaoDinamicaFilho.construirInsert(tabelaFilho));
        criarInstrucaoDinamicaFilho.aplicarValoresDoPreparedStatement(preparedStatementFilho);

        if (preparedStatementFilho.executeUpdate() < 1) {
            return ERRO_GENERICO;
        }
        return SUCESSO;
    }

    @Override
    public List<Usuario> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        List<Usuario> usuarios = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            usuarios.add(new Usuario(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("senha"),
                    resultSet.getString("nome"),
                    TiposUsuario.descobrirTipoUsuario(resultSet.getString("tipo_usuario")),
                    resultSet.getDouble("raio_procura_km")
            ));
        }
        return usuarios;
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

    //        } catch (SQLException sqlException) {
//            if (foiCausadoPorCpfCadastradoProfissional(sqlException))
//                return CPF_INVALIDO;
//
//            if (foiCausadoPorUsuarioCadastradoEmpresaDemandante(sqlException)
//                    || foiCausadoPorUsuarioCadastradoFornecedor(sqlException)
//                    || foiCausadoPorUsuarioCadastradoProfissional(sqlException))
//                return ID_USUARIO_INVALIDO;
//
//            if (foiCausadoPorCnpjCadastradoFornecedor(sqlException) || foiCausadoPorCnpjCadastradoEmpresaDemandante(sqlException))
//                return CNPJ_INVALIDO;
//
//            if (foiCausadoPorEmailCadastrado(sqlException))
//                return EMAIL_INVALIDO;
//
//            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD : ERRO_GENERICO_NO_BD;
//        }

}
