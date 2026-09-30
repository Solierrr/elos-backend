package dao;

import static exception.ErrosGerais.ERRO_POR_VIOLACAO_DE_REGRA_DO_BD;
import static exception.ErrosGerais.ERRO_GENERICO_NO_BD;
import static exception.ErrosGerais.ERRO_GENERICO;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import conexao.Conexao;
import exception.ErrosDoSQL;
import model.TiposUsuario;
import model.Usuario;

public class UsuarioDAO implements GenericDAO {

    private static String tabela = "usuario";

    @Override
    public int insert(CriarInstrucaoDinamica criarInstrucaoDinamica) {
        try (Connection connection = Conexao.getConnection()){

            String insert = criarInstrucaoDinamica.construirInsert(tabela);

            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    @Override
    public List<Usuario> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica) {

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()){

            String read = criarInstrucaoDinamica.construirSelect(tabela);

            PreparedStatement preparedStatement = connection.prepareStatement(read);

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
    public int update(CriarInstrucaoDinamica criarInstrucaoDinamica) {

        try (Connection connection = Conexao.getConnection()){

            String update = criarInstrucaoDinamica.construirUpdate(tabela);

            PreparedStatement preparedStatement = connection.prepareStatement(update);

            criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    @Override
    public int delete(CriarInstrucaoDinamica criarInstrucaoDinamica) {

        try (Connection connection = Conexao.getConnection()){

            String delete = "delete from usuario where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

}
