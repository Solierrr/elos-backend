package conexao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import config.Env;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexao extends Env {

    private static final HikariDataSource dataSource = criarDataSource();

    private static HikariDataSource criarDataSource(){

        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(getDbUrl());
            config.setUsername(getDbUsuario());
            config.setPassword(getDbSenha());
            config.setConnectionTimeout(15000L);
            config.setIdleTimeout(300000L);
            config.setMaxLifetime(600000L);
            config.setMinimumIdle(3);
            config.setMaximumPoolSize(4);
            config.setPoolName("elos-pool");

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            return new HikariDataSource(config);
        } catch (Throwable e) {
            e.printStackTrace();
            Throwable c = e;
            while (c.getCause() != null) c = c.getCause();
            System.out.println("CAUSA RAIZ: " + c);
            return null;
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    //Método auxiliar para o método conectar
    private static String validarEnvs(String valorEnv, String nomeEnv){
        if (valorEnv == null){
            throw new NullPointerException(String.format("Erro: A variável de ambiente %s não está registrada no .env", nomeEnv));
        }
        else if(valorEnv.isEmpty()){
            throw new IllegalArgumentException(String.format("Erro: A variável de ambiente %s não possuí um valor registrado", nomeEnv));
        }
        return valorEnv.strip();
    }

}