package util;

import config.Env;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

public final class Criptografia extends Env {

  // Chaves responsáveis pela criptografia
  private static final byte[] HMAC_KEY = getHmacKey();
  private static final byte[] AES_KEY = getAesKey();

  // Constantes utilizadas pelo código
  private static final String NOME_ALGORITMO_HMAC = "HmacSHA256";
  private static final String NOME_ALGORITMO_AES = "AES/GCM/NoPadding";
  private static final int TAMANHO_VETOR_INICIALIZACAO = 12;
  private static final int TAMANHO_TAG_AUTENTICACAO = 128;

  // Métodos do BCrypt
  public String porEmBcrypt(String senha) {
    return BCrypt.hashpw(senha, BCrypt.gensalt());
  }

  public boolean verificarBcrypt(String senhaSemHash, String senhaComHash) {
    return BCrypt.checkpw(senhaSemHash, senhaComHash);
  }

  // Método do HMAC
  public String criptografarHmac(String stringParaHmac) {
    try {
      Mac gerarMac = Mac.getInstance(NOME_ALGORITMO_HMAC);
      gerarMac.init(new SecretKeySpec(HMAC_KEY, NOME_ALGORITMO_HMAC));

      byte[] hashEmHmac = gerarMac.doFinal(stringParaHmac.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hashEmHmac);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new RuntimeException("Erro ao realizar a criptografia HMAC", noSuchAlgorithmException);
    } catch (InvalidKeyException invalidKeyException) {
      throw new RuntimeException("Erro chave HMAC não encontrada", invalidKeyException);
    }
  }

  // Métodos do AES
  public byte[] criptografarAes(String stringParaAes) {
    try {
      byte[] vetorInicializacao = new byte[TAMANHO_VETOR_INICIALIZACAO];
      new SecureRandom().nextBytes(vetorInicializacao);

      Cipher cifraAes = Cipher.getInstance(NOME_ALGORITMO_AES);
      SecretKey chaveAes = new SecretKeySpec(AES_KEY, "AES");

      GCMParameterSpec parametrosGcm =
          new GCMParameterSpec(TAMANHO_TAG_AUTENTICACAO, vetorInicializacao);
      cifraAes.init(Cipher.ENCRYPT_MODE, chaveAes, parametrosGcm);

      byte[] textoCifrado = cifraAes.doFinal(stringParaAes.getBytes(StandardCharsets.UTF_8));

      byte[] resultadoCombinado = new byte[vetorInicializacao.length + textoCifrado.length];
      System.arraycopy(vetorInicializacao, 0, resultadoCombinado, 0, vetorInicializacao.length);
      System.arraycopy(
          textoCifrado, 0, resultadoCombinado, vetorInicializacao.length, textoCifrado.length);

      return resultadoCombinado;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException exception) {
      throw new RuntimeException("Erro ao realizar a criptografia AES", exception);
    } catch (InvalidKeyException invalidKeyException) {
      throw new RuntimeException("Erro chave AES não encontrada", invalidKeyException);
    } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException exception) {
      throw new RuntimeException("Erro nas constantes da classe Criptografia", exception);
    } catch (BadPaddingException badPaddingException) {
      throw new RuntimeException(
          "O dado que se deseja descriptografar não está de acordo com a criptografia do sistema",
          badPaddingException);
    }
  }

  public String descriptografarAes(byte[] bytesParaDescriptografar) {
    try {
      byte[] vetorInicializacao = new byte[TAMANHO_VETOR_INICIALIZACAO];
      byte[] textoCifrado = new byte[bytesParaDescriptografar.length - TAMANHO_VETOR_INICIALIZACAO];

      System.arraycopy(
          bytesParaDescriptografar, 0, vetorInicializacao, 0, TAMANHO_VETOR_INICIALIZACAO);
      System.arraycopy(
          bytesParaDescriptografar,
          TAMANHO_VETOR_INICIALIZACAO,
          textoCifrado,
          0,
          textoCifrado.length);

      Cipher cifraAes = Cipher.getInstance(NOME_ALGORITMO_AES);
      SecretKey chaveAes = new SecretKeySpec(AES_KEY, "AES");

      GCMParameterSpec parametrosGcm =
          new GCMParameterSpec(TAMANHO_TAG_AUTENTICACAO, vetorInicializacao);
      cifraAes.init(Cipher.DECRYPT_MODE, chaveAes, parametrosGcm);

      byte[] textoDecifrado = cifraAes.doFinal(textoCifrado);
      return new String(textoDecifrado, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException exception) {
      throw new RuntimeException("Erro ao realizar a criptografia AES", exception);
    } catch (InvalidKeyException invalidKeyException) {
      throw new RuntimeException("Erro chave AES não encontrada", invalidKeyException);
    } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException exception) {
      throw new RuntimeException("Erro nas constantes da classe Criptografia", exception);
    } catch (BadPaddingException badPaddingException) {
      throw new RuntimeException(
          "O dado que se deseja descriptografar não está de acordo com a criptografia do sistema",
          badPaddingException);
    }
  }
}
