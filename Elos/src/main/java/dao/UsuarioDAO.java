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
import model.TiposUsuario;
import model.Usuario;

public class UsuarioDAO implements GenericDAO<Usuario> {

    @Override
    public int insert(Usuario usuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            if(usuario.getRaioProcuraKm() > 0) {
                String insert = "insert into usuario(email, senha, nome, tipo_usuario, raio_procura_km) values(?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insert);
                preparedStatement.setString(1, usuario.getEmail() );
                preparedStatement.setString(2, usuario.getSenha());
                preparedStatement.setString(3, usuario.getNome());
                preparedStatement.setString(4, usuario.getTipoUsuario().getTipoDoUsuario());
                preparedStatement.setDouble(5, usuario.getRaioProcuraKm());

                return preparedStatement.executeUpdate();
            }else{
                String insert = "insert into usuario(email, senha, nome, tipo_usuario, raio_procura_km) values(?, ?, ?, ?, DEFAULT)";

                PreparedStatement preparedStatement = connection.prepareStatement(insert);
                preparedStatement.setString(1, usuario.getEmail() );
                preparedStatement.setString(2, usuario.getSenha());
                preparedStatement.setString(3, usuario.getNome());
                preparedStatement.setString(4, usuario.getTipoUsuario().getTipoDoUsuario());

                return preparedStatement.executeUpdate();
            }
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        }finally {
            conexao.desconectar();
        }
    }

    @Override
    public Usuario readById(long id){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from usuario where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new Usuario(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("senha"),
                        resultSet.getString("nome"),
                        TiposUsuario.descobrirTipoUsuario(resultSet.getString("tipo_usuario")),
                        resultSet.getDouble("raio_procura_km")
                );
            }
            return new Usuario(REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null, null, REGISTRO_NAO_ENCONTRADO.getCodigo());
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public Usuario readByEmail(String email){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from usuario where email = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new Usuario(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("senha"),
                        resultSet.getString("nome"),
                        TiposUsuario.descobrirTipoUsuario(resultSet.getString("tipo_usuario")),
                        resultSet.getDouble("raio_procura_km")
                );
            }
            return new Usuario(REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null, null, REGISTRO_NAO_ENCONTRADO.getCodigo());
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    @Override
    public ArrayList<Usuario> readAll(){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllOrderBy(String campoDoOrderBy, String sentidoOrderBy){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario order by "+campoDoOrderBy+" "+sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllByNome(String nome){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario where nome ilike ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, "%" + nome + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllByNomeOrderBy(String nome, String campoDoOrderBy, String sentidoOrderBy){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario where nome ilike ? order by "+campoDoOrderBy+" "+sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, "%" + nome + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllByTipoUsuario(String tipoUsuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario where tipo_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, tipoUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllByTipoUsuarioOrderBy(String tipoUsuario, String campoDoOrderBy, String sentidoOrderBy){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario where tipo_usuario = ? order by "+campoDoOrderBy+" "+sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, tipoUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllWhereRaioProcuraKmEntre(double raioProcuraKmBase, double raioProcuraKmTeto){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String read = "select * from usuario where raio_procura_km between ? and ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setDouble(1, raioProcuraKmBase);
            preparedStatement.setDouble(2, raioProcuraKmTeto);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Usuario> readAllWhereRaioProcuraKmEntreOrderBy(double raioProcuraKmBase, double raioProcuraKmTeto, String campoDoOrderBy, String sentidoOrderBy){

        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {

            String read = "select * from usuario where raio_procura_km between ? and ? order by "+campoDoOrderBy+" "+sentidoOrderBy;

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setDouble(1, raioProcuraKmBase);
            preparedStatement.setDouble(2, raioProcuraKmTeto);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
        } catch (Exception exception){
            return null;
        }finally {
            conexao.desconectar();
        }
    }

    @Override
    public int updateById(Usuario usuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update usuario set email = coalesce(?, email), senha = coalesce(?, senha), nome = coalesce(?, nome), raio_procura_km = coalesce(?, raio_procura_km) where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, usuario.getEmail() );
            preparedStatement.setString(2, usuario.getSenha() );
            preparedStatement.setString(3, usuario.getNome() );
            preparedStatement.setDouble(4, usuario.getRaioProcuraKm());
            preparedStatement.setLong(5, usuario.getId());

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        }finally {
            conexao.desconectar();
        }
    }

    public int updateByEmail(Usuario usuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update usuario set senha = ?, nome = ?, raio_procura_km = ? where email = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, usuario.getSenha() );
            preparedStatement.setString(2, usuario.getNome() );
            preparedStatement.setDouble(3, usuario.getRaioProcuraKm());
            preparedStatement.setString(4, usuario.getEmail());

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        }finally {
            conexao.desconectar();
        }
    }

    @Override
    public int deleteById(long id){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String delete = "delete from usuario where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        }finally {
            conexao.desconectar();
        }
    }

    public int deleteByEmail(String email){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String delete = "delete from usuario where email = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setString(1, email);

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        }finally {
            conexao.desconectar();
        }
    }
}
