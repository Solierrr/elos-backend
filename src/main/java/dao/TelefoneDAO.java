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

import conexao.Conexao;
import exception.ErrosDoSQL;
import model.TiposTelefone;
import model.Telefone;

public class TelefoneDAO implements GenericDAO<Telefone> {

    @Override
    public int insert(Telefone telefone){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String insert = "insert into telefone(telefone, id_usuario, tipo, principal) values(?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, telefone.getTelefone() );
            preparedStatement.setLong(2, telefone.getIdUsuario());
            preparedStatement.setString(3, telefone.getTipo().getTipoTelefone());
            preparedStatement.setBoolean(4, telefone.isPrincipal() );

            return preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();

        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();

        } finally {
            conexao.desconectar();

        }
    }

    @Override
    public Telefone readById(long id){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from telefone where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new Telefone(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("telefone"),
                        TiposTelefone.descobrirTipoTelefone(resultSet.getString("tipo")),
                        resultSet.getBoolean("principal")
                );
            }

            return new Telefone(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);

        } catch (Exception exception){
            return null;

        } finally {
            conexao.desconectar();

        }
    }

    public Telefone readByTelefone(String telefone){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from telefone where telefone = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, telefone);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new Telefone(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("telefone"),
                        TiposTelefone.descobrirTipoTelefone(resultSet.getString("tipo")),
                        resultSet.getBoolean("principal")
                );
            }

            return new Telefone(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);

        } catch (Exception exception){
            return null;

        } finally {
            conexao.desconectar();

        }
    }

    @Override
    public ArrayList<Telefone> readAll(){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Telefone> telefones = new ArrayList<>();

        try {
            String read = "select * from telefone";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                telefones.add(new Telefone(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("telefone"),
                        TiposTelefone.descobrirTipoTelefone(resultSet.getString("tipo")),
                        resultSet.getBoolean("principal")
                ));
            }

            return telefones;

        } catch (Exception exception){
            return telefones;

        } finally {
            conexao.desconectar();

        }
    }

    public ArrayList<Telefone> readAllByIdUsuario(long idUsuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Telefone> telefones = new ArrayList<>();

        try {
            String read = "select * from telefone where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                telefones.add(new Telefone(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("telefone"),
                        TiposTelefone.descobrirTipoTelefone(resultSet.getString("tipo")),
                        resultSet.getBoolean("principal")
                ));
            }
            return telefones;
        } catch (Exception exception){
            return telefones;
        } finally {
            conexao.desconectar();
        }
    }

    @Override
    public int updateById(Telefone telefone){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update telefone set telefone = ?, tipo = ?, principal = ? where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, telefone.getTelefone() );
            preparedStatement.setString(2, telefone.getTipo().getTipoTelefone() );
            preparedStatement.setBoolean(3, telefone.isPrincipal() );
            preparedStatement.setLong(4, telefone.getId());

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }

    public int updateByTelefone(Telefone telefone){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update telefone set tipo = ?, principal = ? where telefone = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, telefone.getTipo().getTipoTelefone() );
            preparedStatement.setBoolean(2, telefone.isPrincipal() );
            preparedStatement.setString(3, telefone.getTelefone() );

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }

    @Override
    public int deleteById(long id){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String delete = "delete from telefone where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }

    public int deleteByTelefone(String telefone){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String delete = "delete from telefone where telefone = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setString(1, telefone);

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }
}