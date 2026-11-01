package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Base64;

//Uso dessa classe para garantir que o .env fosse acessado uma vez e que o menor número de classes tenham acesso
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

  // envs para enviar email
  private static final String BREVO_API_KEY = obterEnvGenerico("BREVO_API_KEY");
  private static final String EMAIL_USUARIO = obterEnvGenerico("EMAIL_USUARIO");

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

  // Métodos para as envs do email
  protected static String getBrevoApiKey() {
    return BREVO_API_KEY;
  }

  protected static String getEmailUsuario() {
    return EMAIL_USUARIO;
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
