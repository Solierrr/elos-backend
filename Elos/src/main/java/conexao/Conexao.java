package conexao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.*;

public class Conexao {

    private static final Dotenv VARIAVEIS_DE_AMBIENTE = Dotenv.load();

    private static final HikariDataSource dataSource = criarDataSource();

    private static HikariDataSource criarDataSource(){
        final String URL = validarEnvs(VARIAVEIS_DE_AMBIENTE.get("DB_URL"), "DB_URL");
        final String USUARIO = validarEnvs(VARIAVEIS_DE_AMBIENTE.get("DB_USUARIO"), "DB_USUARIO");
        final String SENHA = validarEnvs(VARIAVEIS_DE_AMBIENTE.get("DB_SENHA"), "DB_SENHA");

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(URL);
        config.setUsername(USUARIO);
        config.setPassword(SENHA);
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