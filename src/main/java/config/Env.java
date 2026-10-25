package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Base64;

public abstract class Env {

  // Dados do .env
  private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

  // envs do banco PostgreSQL
  private static final String DB_URL = obterEnvGenerico("DB_URL");
  private static final String DB_USUARIO = obterEnvGenerico("DB_USUARIO");
  private static final String DB_SENHA = obterEnvGenerico("DB_SENHA");

  // envs da criptografia
  private static final byte[] AES_KEY = senhaCriptografia("AES_KEY");
  private static final byte[] HMAC_KEY = senhaCriptografia("HMAC_KEY");

  // Métodos para as envs do PostgreSQL
  protected static String getDbUrl() {
    return DB_URL;
  }

  protected static String getDbUsuario() {
    return DB_USUARIO;
  }

  protected static String getDbSenha() {
    return DB_SENHA;
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
    if (valor.isEmpty()) {
      throw new IllegalStateException("A variável " + nomeVariavel + " não possui valor");
    }
    return valor;
  }

  private static byte[] senhaCriptografia(String nomeVariavel) {
    return Base64.getDecoder().decode(obterEnvGenerico(nomeVariavel));
  }
}
