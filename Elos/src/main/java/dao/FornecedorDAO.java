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

import conexao.Conexao;
import exception.ErrosDoSQL;
import model.TiposFornecedor;
import model.Fornecedor;

public class FornecedorDAO implements GenericDAO<Fornecedor> {

    @Override
    public int insert(Fornecedor fornecedor) {


        try (Connection connection = Conexao.getConnection()) {

            String insert = "insert into fornecedor(id_usuario, tipo_usuario, tipo_fornecedor, cnpj, razao_social) values(?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setLong(1, fornecedor.getIdUsuario());
            preparedStatement.setString(2, fornecedor.getTipoUsuario().getTipoDoUsuario());
            preparedStatement.setString(3, fornecedor.getTipoFornecedor().getTipoFornecedor());
            preparedStatement.setString(4, fornecedor.getCnpj());
            preparedStatement.setString(5, fornecedor.getRazaoSocial());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    @Override
    public Fornecedor readById(long id) {


        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                );
            }
            return new Fornecedor(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null);
        } catch (Exception exception) {
            return null;
        }
    }

    public Fornecedor readByIdUsuario(long idUsuario) {


        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                );
            }

            return new Fornecedor(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null);

        } catch (Exception exception) {
            return null;

        }
    }

    public Fornecedor readByCnpj(String cnpj) {


        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, cnpj);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                );
            }

            return new Fornecedor(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null);

        } catch (Exception exception) {
            return null;

        }
    }

    @Override
    public List<Fornecedor> readAll() {

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fornecedores.add(new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                ));
            }
            return fornecedores;
        } catch (Exception exception) {
            return fornecedores;
        }
    }

    public List<Fornecedor> readAllOrderBy(String campoDoOrderBy, String sentidoOrderBy) {

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor order by " + campoDoOrderBy + " " + sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fornecedores.add(new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                ));
            }
            return fornecedores;
        } catch (Exception exception) {
            return fornecedores;
        }
    }

    public List<Fornecedor> readAllByTipoFornecedor(String tipoFornecedor) {

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where tipo_fornecedor = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, tipoFornecedor);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                fornecedores.add(new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                ));
            }
            return fornecedores;
        } catch (Exception exception) {
            return fornecedores;
        }
    }

    public List<Fornecedor> readAllByTipoFornecedorOrderBy(String tipoFornecedor, String campoDoOrderBy, String sentidoOrderBy) {

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where tipo_fornecedor = ? order by " + campoDoOrderBy + " " + sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, tipoFornecedor);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                fornecedores.add(new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                ));
            }
            return fornecedores;
        } catch (Exception exception) {
            return fornecedores;
        }
    }

    public List<Fornecedor> readAllByRazaoSocial(String razaoSocial) {

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where razao_social ilike ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, "%" + razaoSocial + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                fornecedores.add(new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                ));
            }
            return fornecedores;
        } catch (Exception exception) {
            return fornecedores;
        }
    }

    public List<Fornecedor> readAllByRazaoSocialOrderBy(String razaoSocial, String campoDoOrderBy, String sentidoOrderBy) {

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection connection = Conexao.getConnection()) {

            String read = "select * from fornecedor where razao_social ilike ? order by " + campoDoOrderBy + " " + sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, "%" + razaoSocial + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                fornecedores.add(new Fornecedor(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        TiposFornecedor.descobrirTipoFornecedor(resultSet.getString("tipo_fornecedor")),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social")
                ));
            }
            return fornecedores;
        } catch (Exception exception) {
            return fornecedores;
        }
    }

    @Override
    public int updateById(Fornecedor fornecedor) {


        try (Connection connection = Conexao.getConnection()) {

            String update = "update fornecedor set tipo_fornecedor = ?, razao_social = ? where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, fornecedor.getTipoUsuario().getTipoDoUsuario());
            preparedStatement.setString(2, fornecedor.getRazaoSocial());
            preparedStatement.setLong(3, fornecedor.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    public int updateByIdUsuario(Fornecedor fornecedor) {


        try (Connection connection = Conexao.getConnection()) {

            String update = "update fornecedor set tipo_fornecedor = ?, razao_social = ? where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, fornecedor.getTipoUsuario().getTipoDoUsuario());
            preparedStatement.setString(2, fornecedor.getRazaoSocial());
            preparedStatement.setLong(3, fornecedor.getIdUsuario());

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        } catch (Exception exception) {
            return ERRO_GENERICO.getCodigo();
        }
    }

    public int updateByCnpj(Fornecedor fornecedor) {


        try (Connection connection = Conexao.getConnection()) {

            String update = "update fornecedor set tipo_fornecedor = ?, razao_social = ? where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, fornecedor.getTipoUsuario().getTipoDoUsuario());
            preparedStatement.setString(2, fornecedor.getRazaoSocial());
            preparedStatement.setString(3, fornecedor.getCnpj());

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

            String delete = "delete from fornecedor where id = ?";

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

            String delete = "delete from fornecedor where id_usuario = ?";

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

            String delete = "delete from fornecedor where cnpj = ?";

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
