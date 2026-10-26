package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Base64;

public abstract class Env {

  // Dados do .env
  private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

  // envs do banco PostgreSQL
  private static final String POSTGRESQL_URL = obterEnvGenerico("POSTGRESQL_URL");
  private static final String POSTGRESQL_USUARIO = obterEnvGenerico("POSTGRESQL_USUARIO");
  private static final String POSTGRESQL_SENHA = obterEnvGenerico("POSTGRESQL_SENHA");

  private static final String REDIS_HOST = obterEnvGenerico("REDIS_HOST");
  private static final String REDIS_PORT  = obterEnvGenerico("REDIS_PORT");
  private static final String REDIS_SENHA  = obterEnvGenerico("REDIS_SENHA");

  // envs da criptografia
  private static final byte[] AES_KEY = senhaCriptografia("AES_KEY");
  private static final byte[] HMAC_KEY = senhaCriptografia("HMAC_KEY");

  // Métodos para as envs do PostgreSQL
  protected static String getPostgresqlUrl() {
    return POSTGRESQL_URL;
  }

  protected static String getPostgresqlUsuario() {
    return POSTGRESQL_USUARIO;
  }

  protected static String getPostgresqlSenha() {
    return POSTGRESQL_SENHA;
  }

  //Métodos para as envs do Redis
  protected static String getRedisHost() {
    return REDIS_HOST;
  }

  protected static String getRedisPort() {
    return REDIS_PORT;
  }

  protected static String getRedisSenha() {
    return REDIS_SENHA;
  }

  // Métodos para as envs de criptografia
  protected static byte[] getAesKey() {
    return AES_KEY.clone();
  }

  protected static byte[] getHmacKey() {
    return HMAC_KEY.clone();
  }

  // Métodos auxiliares
  private static String obterEnvGenerico(String nomeVariavel) {
    String valor = DOTENV.get(nomeVariavel);
    if (valor == null) {
      throw new IllegalStateException(
          "A variável " + nomeVariavel + " não está registrada no .env");
    }
    valor = valor.strip();
    if (valor.isBlank() && !nomeVariavel.startsWith("REDIS_SENHA")) {
      throw new IllegalStateException("A variável " + nomeVariavel + " não possui valor");
    }
    return valor;
  }

  private static byte[] senhaCriptografia(String nomeVariavel) {
    return Base64.getDecoder().decode(obterEnvGenerico(nomeVariavel));
  }
}
