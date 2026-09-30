package dao;

import static exception.ErrosDadosProfissional.CPF_INVALIDO;
import static exception.ErrosDadosUsuario.EMAIL_INVALIDO;
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.TiposUsuario;
import model.Usuario;

public class UsuarioDAO implements GenericDAO {

    private static String TABELA = "usuario";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        throw new IllegalArgumentException("Existe parametros faltando!");
    }

    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamicaUsuario,
                                       CriarInstrucaoDinamica criarInstrucaoDinamicaFilho,
                                       String tabelaFilho) {

        try (Connection connection = Conexao.getConnection()) {
            connection.setAutoCommit(false);

            try {
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
                    connection.rollback();
                    return ERRO_GENERICO;
                }

                connection.commit();
                return SUCESSO;
            } catch (SQLException sqlException) {
                connection.rollback();
                throw sqlException;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException sqlException) {
            if (foiCausadoPorCpfCadastradoProfissional(sqlException))
                return CPF_INVALIDO;

            if (foiCausadoPorUsuarioCadastradoEmpresaDemandante(sqlException)
                    || foiCausadoPorUsuarioCadastradoFornecedor(sqlException)
                    || foiCausadoPorUsuarioCadastradoProfissional(sqlException))
                return ID_USUARIO_INVALIDO;

            if (foiCausadoPorCnpjCadastradoFornecedor(sqlException) || foiCausadoPorCnpjCadastradoEmpresaDemandante(sqlException))
                return CNPJ_INVALIDO;

            if (foiCausadoPorEmailCadastrado(sqlException))
                return EMAIL_INVALIDO;

            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD : ERRO_GENERICO_NO_BD;
        }
    }

    @Override
    public List<Usuario> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {
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
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirUpdate(TABELA));

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
        } catch (SQLException sqlException) {
            if (foiCausadoPorCpfCadastradoProfissional(sqlException))
                return CPF_INVALIDO;

            if (foiCausadoPorUsuarioCadastradoEmpresaDemandante(sqlException)
                    || foiCausadoPorUsuarioCadastradoFornecedor(sqlException)
                    || foiCausadoPorUsuarioCadastradoProfissional(sqlException))
                return ID_USUARIO_INVALIDO;

            if (foiCausadoPorCnpjCadastradoFornecedor(sqlException) || foiCausadoPorCnpjCadastradoEmpresaDemandante(sqlException))
                return CNPJ_INVALIDO;

            if (foiCausadoPorEmailCadastrado(sqlException))
                return EMAIL_INVALIDO;

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

    //Métodos auxiliares que são usados para ver se o erro foi causado por um valor unique já cadastrado,
    // isso foi usado para não realizar consultas desnecessárias para algo que o banco conseguiria barrar
    private boolean foiCausadoPorCnpjCadastradoFornecedor(SQLException sqlException){
        return "fornecedor_cnpj_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorUsuarioCadastradoFornecedor(SQLException sqlException){
        return "fornecedor_id_usuario_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorCpfCadastradoProfissional(SQLException sqlException){
        return "profissional_cpf_hmac_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorUsuarioCadastradoProfissional(SQLException sqlException){
        return "profissional_id_usuario_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorCnpjCadastradoEmpresaDemandante(SQLException sqlException) {
        return "empresa_demandante_cnpj_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorUsuarioCadastradoEmpresaDemandante(SQLException sqlException) {
        return "empresa_demandante_id_usuario_key".contains(sqlException.getMessage());
    }

    private boolean foiCausadoPorEmailCadastrado(SQLException sqlException){
        return "usuario_email_key".contains(sqlException.getMessage());
    }

}
