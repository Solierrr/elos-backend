package conexao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import config.Env;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexaoPostgresql extends Env {

  //Constante responsável por gerenciar as conexões do banco
  private static final HikariDataSource postgresqlPool = criarPool();

  private static HikariDataSource criarPool() {

    HikariConfig config = new HikariConfig();
    config.setDriverClassName("org.postgresql.Driver");
    config.setJdbcUrl(getPostgresqlUrl());
    config.setUsername(getPostgresqlUsuario());
    config.setPassword(getPostgresqlSenha());
    config.setConnectionTimeout(15000L);
    config.setIdleTimeout(300000L);
    config.setMaxLifetime(600000L);
    config.setMinimumIdle(3);
    config.setMaximumPoolSize(4);
    config.setPoolName("elos-pool");

    // Aumenta a velocidade do pool
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    return new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return postgresqlPool.getConnection();
  }

}