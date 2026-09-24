package dao;

import static exception.ErrosGerais.ERRO_POR_VIOLACAO_DE_REGRA_DO_BD;
import static exception.ErrosGerais.ERRO_GENERICO_NO_BD;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.REGISTRO_NAO_ENCONTRADO;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import exception.ErrosDoSQL;
import model.EmpresaDemandante;
import conexao.Conexao;

public class EmpresaDemandanteDAO implements GenericDAO<EmpresaDemandante> {

    @Override
    public int insert(EmpresaDemandante empresaDemandante) {
        try (Connection connection = Conexao.getConnection()) {
            String insert = "insert into empresa_demandante(id_usuario, tipo_usuario, cnpj, razao_social, eh_mandante) values(?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setLong(1, empresaDemandante.getIdUsuario());
            preparedStatement.setString(2, empresaDemandante.getTipoUsuario().getTipoDoUsuario());
            preparedStatement.setString(3, empresaDemandante.getCnpj());
            preparedStatement.setString(4, empresaDemandante.getRazaoSocial());
            preparedStatement.setBoolean(5, empresaDemandante.isEhMandante());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    @Override
    public EmpresaDemandante readById(long id) {
        try (Connection connection = Conexao.getConnection()) {
            String read = "select * from empresa_demandante where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                );
            }
            return new EmpresaDemandante(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);
        } catch (Exception exception) {
            return null;
        }
    }

    public EmpresaDemandante readByIdUsuario(long idUsuario) {
        try (Connection connection = Conexao.getConnection()) {
            String read = "select * from empresa_demandante where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                );
            }
            return new EmpresaDemandante(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);
        } catch (Exception exception) {
            return null;
        }
    }

    public EmpresaDemandante readByCnpj(String cnpj) {
        try (Connection connection = Conexao.getConnection()) {
            String read = "select * from empresa_demandante where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, cnpj);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                );
            }
            return new EmpresaDemandante(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public List<EmpresaDemandante> readAll() {
        List<EmpresaDemandante> empresaDemandantes = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {
            String read = "select * from empresa_demandante";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                empresaDemandantes.add(new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                ));
            }
            return empresaDemandantes;
        } catch (Exception exception) {
            return empresaDemandantes;
        }
    }

    @Override
    public int updateById(EmpresaDemandante empresaDemandante) {
        try (Connection connection = Conexao.getConnection()) {
            String update = "update empresa_demandante set razao_social = ? where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, empresaDemandante.getRazaoSocial());
            preparedStatement.setLong(2, empresaDemandante.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    public int updateByIdUsuario(EmpresaDemandante empresaDemandante) {
        try (Connection connection = Conexao.getConnection()) {
            String update = "update empresa_demandante set razao_social = ? where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, empresaDemandante.getRazaoSocial());
            preparedStatement.setLong(2, empresaDemandante.getIdUsuario());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    public int updateByCnpj(EmpresaDemandante empresaDemandante) {
        try (Connection connection = Conexao.getConnection()) {
            String update = "update empresa_demandante set razao_social = ? where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, empresaDemandante.getRazaoSocial());
            preparedStatement.setString(2, empresaDemandante.getCnpj());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    @Override
    public int deleteById(long id) {
        try (Connection connection = Conexao.getConnection()) {
            String delete = "delete from empresaDemandante where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    public int deleteByIdUsuario(long idUsuario) {
        try (Connection connection = Conexao.getConnection()) {
            String delete = "delete from empresa_demandante where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setLong(1, idUsuario);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    public int deleteByCnpj(String cnpj) {
        try (Connection connection = Conexao.getConnection()) {
            String delete = "delete from empresa_demandante where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setString(1, cnpj);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }
}
