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
import model.Endereco;
import model.EstadosBrasileiros;

public class EnderecoDAO implements GenericDAO<Endereco> {

    @Override
    public int insert(Endereco endereco){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String insert = "insert into endereco(id_usuario, estado, cidade, bairro, cep, logradouro, numero, complemento) values(?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setLong(1, endereco.getIdUsuario() );
            preparedStatement.setString(2, endereco.getEstado().getSiglaEstado());
            preparedStatement.setString(3, endereco.getCidade());
            preparedStatement.setString(4, endereco.getBairro() );
            preparedStatement.setString(5, endereco.getCep() );
            preparedStatement.setString(6, endereco.getLogradouro() );
            preparedStatement.setString(7, endereco.getNumero() );
            preparedStatement.setString(8, endereco.getComplemento() );

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
    public Endereco readById(long id){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from endereco where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new Endereco(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        EstadosBrasileiros.descobrirEstadoBrasileiroPorSigla(resultSet.getString("estado")),
                        resultSet.getString("cidade"),
                        resultSet.getString("bairro"),
                        resultSet.getString("cep"),
                        resultSet.getString("logradouro"),
                        resultSet.getString("numero"),
                        resultSet.getString("complemento")
                );
            }

            return new Endereco(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null,
                          null, null, null, null, null);
        } catch (Exception exception){
            return null;
        } finally {
            conexao.desconectar();
        }
    }

    @Override
    public ArrayList<Endereco> readAll(){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Endereco> enderecos = new ArrayList<>();

        try {
            String read = "select * from endereco";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
            return enderecos;
        } catch (Exception exception){
            return enderecos;
        } finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Endereco> readAllByIdUsuario(long idUsuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Endereco> enderecos = new ArrayList<>();

        try {
            String read = "select * from endereco where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
            return enderecos;
        } catch (Exception exception){
            return enderecos;
        } finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Endereco> readAllByEstado(String estado){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Endereco> enderecos = new ArrayList<>();

        try {
            String read = "select * from endereco where estado = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, estado);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
            return enderecos;
        } catch (Exception exception){
            return enderecos;
        } finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Endereco> readAllByCidade(String cidade){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Endereco> enderecos = new ArrayList<>();

        try {
            String read = "select * from endereco where cidade = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, cidade);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
            return enderecos;
        } catch (Exception exception){
            return enderecos;
        } finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Endereco> readAllByBairro(String bairro){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Endereco> enderecos = new ArrayList<>();

        try {
            String read = "select * from endereco where bairro = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, bairro);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
            return enderecos;
        } catch (Exception exception){
            return enderecos;
        } finally {
            conexao.desconectar();
        }
    }

    public ArrayList<Endereco> readAllByCep(String cep){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();
        ArrayList<Endereco> enderecos = new ArrayList<>();

        try {
            String read = "select * from endereco where cep = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, cep);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
            return enderecos;
        } catch (Exception exception){
            return enderecos;
        } finally {
            conexao.desconectar();
        }
    }

    @Override
    public int updateById(Endereco endereco){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update endereco set estado = ?, cidade = ?, bairro = ?, cep = ?, logradouro = ?, numero = ?, complemento = ? where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, endereco.getEstado().getSiglaEstado() );
            preparedStatement.setString(2, endereco.getCidade() );
            preparedStatement.setString(3, endereco.getBairro() );
            preparedStatement.setString(4, endereco.getCep() );
            preparedStatement.setString(5, endereco.getLogradouro() );
            preparedStatement.setString(6, endereco.getNumero() );
            preparedStatement.setString(7, endereco.getComplemento() );
            preparedStatement.setLong(8, endereco.getId());

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
            String delete = "delete from endereco where id = ?";

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
}