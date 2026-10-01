package util;

import config.Env;
import io.github.cdimascio.dotenv.Dotenv;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

public class Criptografia extends Env {

    private static final byte[] HMAC_KEY = getHmacKey();
    private static final byte[] AES_KEY = getAesKey();

    private static final String NOME_ALGORITMO_HMAC = "HmacSHA256";
    private static final String NOME_ALGORITMO_AES = "AES/GCM/NoPadding";
    private static final int TAMANHO_VETOR_INICIALIZACAO = 12;
    private static final int TAMANHO_TAG_AUTENTICACAO = 128;

    public String colocarHashNaSenha(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public boolean verificarSeSenhaBate(String senhaSemHash, String senhaComHash){
        return BCrypt.checkpw(senhaSemHash, senhaComHash);
    }

    public byte[] criptografarAes(String stringParaAes){
        byte[] vetorInicializacao = new byte[TAMANHO_VETOR_INICIALIZACAO];
        new SecureRandom().nextBytes(vetorInicializacao);

        Cipher cifraAes = Cipher.getInstance(NOME_ALGORITMO_AES);
        SecretKey chaveAes = new SecretKeySpec(CHAVE_SECRETA_AES, "AES");

        GCMParameterSpec parametrosGcm = new GCMParameterSpec(TAMANHO_TAG_AUTENTICACAO, vetorInicializacao);
        cifraAes.init(Cipher.ENCRYPT_MODE, chaveAes, parametrosGcm);

        byte[] textoCifrado = cifraAes.doFinal(normalizarCpf(cpf).getBytes("UTF-8"));

        // Concatena IV + texto cifrado — o IV precisa ser guardado junto para decriptar depois
        byte[] resultadoCombinado = new byte[vetorInicializacao.length + textoCifrado.length];
        System.arraycopy(vetorInicializacao, 0, resultadoCombinado, 0, vetorInicializacao.length);
        System.arraycopy(textoCifrado, 0, resultadoCombinado, vetorInicializacao.length, textoCifrado.length);

        return resultadoCombinado;
    }





}
