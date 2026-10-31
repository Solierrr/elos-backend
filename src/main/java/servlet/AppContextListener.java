package servlet;

import conexao.ConexaoPostgresql;
import conexao.ConexaoRedis;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public final class AppContextListener implements ServletContextListener {

  //Uso dessa classe para contornar o problema da primeira requisição ser muito lenta por estar ligando o pool

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ConexaoRedis.criarPool();
    ConexaoPostgresql.criarPool();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ConexaoPostgresql.closePool();
    ConexaoRedis.closePool();
  }
}