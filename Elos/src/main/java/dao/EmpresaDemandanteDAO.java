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

import exception.ErrosDoSQL;
import model.EmpresaDemandante;
import conexao.Conexao;

public class EmpresaDemandanteDAO implements GenericDAO<EmpresaDemandante> {

    @Override
    public int insert(EmpresaDemandante empresaDemandante) {
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String insert = "insert into empresa_demandante(id_usuario, tipo_usuario, cnpj, razao_social, eh_mandante) values(?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setLong(1, empresaDemandante.getIdUsuario());
            preparedStatement.setString(2, empresaDemandante.getTipoUsuario().getTipoDoUsuario());
            preparedStatement.setString(3, empresaDemandante.getCnpj());
            preparedStatement.setString(4, empresaDemandante.getRazaoSocial());
            preparedStatement.setBoolean(5, empresaDemandante.isEhMandante());

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
    public EmpresaDemandante readById(long id){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from empresa_demandante where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                );
            }
            return new EmpresaDemandante(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null,false);
        } catch (Exception exception){
            return null;
        } finally {
            conexao.desconectar();
        }
    }

    public EmpresaDemandante readByIdUsuario(long idUsuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from empresa_demandante where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setLong(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                );
            }
            return new EmpresaDemandante(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);
        } catch (Exception exception){
            return null;
        } finally {
            conexao.desconectar();
        }
    }

    public EmpresaDemandante readByCnpj(String cnpj){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try {
            String read = "select * from empresa_demandante where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            preparedStatement.setString(1, cnpj);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                );
            }
            return new EmpresaDemandante(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null,false);
        } catch (Exception exception){
            return null;
        } finally {
            conexao.desconectar();
        }
    }

    @Override
    public ArrayList<EmpresaDemandante> readAll(){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        ArrayList<EmpresaDemandante> empresaDemandantes = new ArrayList<>();

        try {
            String read = "select * from empresa_demandante";

            PreparedStatement preparedStatement = connection.prepareStatement(read);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                empresaDemandantes.add(new EmpresaDemandante(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_usuario"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao_social"),
                        resultSet.getBoolean("eh_mandante")
                ));
            }
            return empresaDemandantes;
        } catch (Exception exception){
            return empresaDemandantes;
        } finally {
            conexao.desconectar();
        }
    }

    @Override
    public int updateById(EmpresaDemandante empresaDemandante){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update empresa_demandante set razao_social = ? where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, empresaDemandante.getRazaoSocial() );
            preparedStatement.setLong(2, empresaDemandante.getId());

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }

    public int updateByIdUsuario(EmpresaDemandante empresaDemandante){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update empresa_demandante set razao_social = ? where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, empresaDemandante.getRazaoSocial() );
            preparedStatement.setLong(2, empresaDemandante.getIdUsuario());

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }

    public int updateByCnpj(EmpresaDemandante empresaDemandante){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String update = "update empresa_demandante set razao_social = ? where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, empresaDemandante.getRazaoSocial() );
            preparedStatement.setString(2, empresaDemandante.getCnpj());

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
            String delete = "delete from empresaDemandante where id = ?";

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

    public int deleteByIdUsuario(long idUsuario){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String delete = "delete from empresa_demandante where id_usuario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setLong(1, idUsuario);

            return preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            return ErrosDoSQL.foiCausadoPorConstraint(sqlException.getSQLState()) ? ERRO_POR_VIOLACAO_DE_REGRA_DO_BD.getCodigo() : ERRO_GENERICO_NO_BD.getCodigo();
        }catch (Exception exception){
            return ERRO_GENERICO.getCodigo();
        } finally {
            conexao.desconectar();
        }
    }

    public int deleteByCnpj(String cnpj){
        Conexao conexao = new Conexao();
        Connection connection = conexao.conectar();

        try{
            String delete = "delete from empresa_demandante where cnpj = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setString(1, cnpj);

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
