package service.redefinirSenha;

import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGeraisDados.EMAIL_INVALIDO;
import static exception.ErrosGeraisDados.ERRO_GENERICO_EMAIL;
import static exception.ErrosGeraisDados.ERRO_LIMITE_TENTATIVAS_TOKEN;
import static exception.ErrosGeraisDados.ERRO_TOKEN;
import static exception.ErrosGeraisDados.LIMTE_EMAILS_ATINGIDO;
import static exception.ErrosGeraisDados.SENHAS_DIFERENTES;
import static exception.ErrosGeraisDados.TOKEN_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;

import conexao.ConexaoPostgresql;
import conexao.ConexaoRedis;
import dao.AdminDAO;
import dao.CriarInstrucaoDinamica;
import dao.UsuarioDAO;
import exception.ErrosGeraisDados;
import exception.GenericExceptionEnum;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.SetParams;
import util.Criptografia;
import util.EnviarEmail;

public final class RedefinicaoSenhaService {

  //Constantes utilizadas pelo código
  private static final Pattern PATTERN_EMAIL =
          Pattern.compile("^[a-z0-9._%+-]+@[a-z-0-9]+[a-z0-9.-]*\\.[a-z]{2,}$");
  private static final int TAMANHO_MAXIMO_EMAIL = 256;

  private static final int TAMANHO_MAXIMO_TOKEN = 100000;

  private static final int MAXIMO_TENTATIVAS_POR_TOKEN = 5;
  private static final int TTL_EMAIL_TOKEN = 600;

  private static final int MAXIMO_ENVIO_EMAILS = 4;
  private static final int TTL_EMAIL_ENVIOS = 3600;

  private static final String TEMPLATE_EMAIL_REDEFINICAO =
      """
        <html>
            <style>
              @import url('https://fonts.googleapis.com/css2?family=Lexend+Deca:wght@100..900&display=swap');
  
              *{
                  font-family: 'Lexend Deca';
                  color: 2B2B2B;
                  letter-spacing: 0;
                  word-spacing: 0;
              }
            </style>

          <body ">
            <h2>Redefinição de senha</h2>
            <p>Use o código abaixo para redefinir sua senha:</p>
            <p style="font-size: 28px; font-weight: bold; letter-spacing: 6px;">%s</p>
            <p>Esse código expira em alguns minutos. Se você não solicitou essa redefinição, ignore este email.</p>
          </body>
        </html>
        """;
  private static final String ASSUNTO_EMAIL = "Redefinição de senha no Elos";

  private static final Pattern PATTERN_TOKEN = Pattern.compile("^[0-9]{5}$");

  private static final int TAMANHO_MINIMO_SENHA = 8;
  private static final int TAMANHO_MAXIMO_SENHA = 60;
  private static final Pattern PATTERN_SENHA =
          Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");

  //Validações

  // Validações da senha
  private static GenericExceptionEnum validarSenha(String senha) {
    if (senha == null || senha.isBlank()) {
      return ErrosGeraisDados.SENHA_VAZIA;
    }

    String senhaTrim = senha.strip();
    if (senhaTrim.length() < TAMANHO_MINIMO_SENHA) {
      return ErrosGeraisDados.SENHA_MENOR_QUE_OITO;
    }

    if (senhaTrim.length() > TAMANHO_MAXIMO_SENHA) {
      return ErrosGeraisDados.SENHA_TAMANHO_INVALIDO;
    }

    return validarFormatoSenha(senhaTrim);
  }

  private static GenericExceptionEnum validarFormatoSenha(String senha) {
    return PATTERN_SENHA.matcher(senha).matches() ? VALIDACAO_OK : ErrosGeraisDados.SENHA_FRACA;
  }

  // Validações do email
  private static GenericExceptionEnum validarEmail(String email) {
    if (email == null || email.isBlank()) {
      return ErrosGeraisDados.EMAIL_VAZIO;
    }

    String emailTratado = email.toLowerCase().strip();
    if (emailTratado.length() > TAMANHO_MAXIMO_EMAIL) {
      return ErrosGeraisDados.EMAIL_TAMANHO_INVALIDO;
    }
    return validarFormatoEmail(emailTratado) != VALIDACAO_OK
            ? ErrosGeraisDados.EMAIL_INVALIDO
            : VALIDACAO_OK;
  }

  private static GenericExceptionEnum validarFormatoEmail(String email) {
    return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : ErrosGeraisDados.EMAIL_INVALIDO;
  }

  //Validação se o email tem registro no banco
  private static boolean ehCadastardo(String email) {
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    AdminDAO adminDAO = new AdminDAO();

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere("email", IGUAL, VAZIO, email.strip().toLowerCase(), Types.VARCHAR);

    try(Connection connection = ConexaoPostgresql.getConnection()){

      List<Map<String, Object>> registro = usuarioDAO.readAll(criarInstrucaoDinamica, connection);
      if (!registro.isEmpty()) {
        return true;
      }

      registro = adminDAO.readAll(criarInstrucaoDinamica, connection);
      if(!registro.isEmpty()) {
        return true;
      }
      return false;
    }catch (SQLException | RuntimeException e) {
     return false;
    }
  }

  //Metodos relacionaados ao envio e persistência do token
  public GenericExceptionEnum enviarToken(String email){
    GenericExceptionEnum validacaoBase = validarEmail(email);
    if(validacaoBase != VALIDACAO_OK){
      return validacaoBase;
    }

    if(!ehCadastardo(email)){
      return EMAIL_INVALIDO;
    }

    return enviarEmailRedefinicao(email);
  }

  private GenericExceptionEnum enviarEmailRedefinicao(String email){
    String emailTratado = email.toLowerCase().strip();


    String token = gerarToken();
    GenericExceptionEnum resultadoPesistirToken = persistirToken(token, emailTratado);
    if(resultadoPesistirToken != SUCESSO){
      return resultadoPesistirToken;
    }

    EnviarEmail enviarEmail = new EnviarEmail();

    return enviarEmail.enviarEmail(emailTratado, ASSUNTO_EMAIL, TEMPLATE_EMAIL_REDEFINICAO.formatted(token)) ? SUCESSO : ERRO_GENERICO_EMAIL;
  }

  private GenericExceptionEnum persistirToken(String token, String email){
    try(Jedis jedis = ConexaoRedis.getConexao()){

      if(jedis.exists(email+":envios")){
        String qtdEnvios =  jedis.get(email+":envios");
        Integer qtdEnviosConvertido = Integer.parseInt(qtdEnvios);

        if (qtdEnviosConvertido >= MAXIMO_ENVIO_EMAILS) {
          jedis.del(email+":token");
          return LIMTE_EMAILS_ATINGIDO;
        }

        qtdEnviosConvertido++;
        SetParams params = new SetParams().keepTtl();
        jedis.set(email + ":envios", String.valueOf(qtdEnviosConvertido), params);
      } else {
        jedis.setex(email + ":envios", TTL_EMAIL_ENVIOS, "1");
      }

      Criptografia criptografia = new Criptografia();
      String tokenHasheado =  criptografia.porEmBcrypt(token);

      Map<String, String> map = new HashMap<>();
      map.put("token", tokenHasheado);
      map.put("qtd", "1");

      jedis.hset(email+":token", map);
      jedis.expire(email+":token", TTL_EMAIL_TOKEN);

      return SUCESSO;
    }catch (JedisException jedisException){
      return ERRO_GENERICO;
    }
  }

  private String gerarToken(){
    SecureRandom random = new SecureRandom();
    int numero = random.nextInt(TAMANHO_MAXIMO_TOKEN);
    return String.format("%05d", numero);
  }

  //Validação do token
  public GenericExceptionEnum verificarToken(String token, String email){
    if(token == null || token.isEmpty() || !PATTERN_TOKEN.matcher(token).matches()){
      return ERRO_TOKEN;
    }

    return validarToken(token, email);
  }

  private GenericExceptionEnum validarToken(String tokenRecebido, String email){

    try(Jedis jedis = ConexaoRedis.getConexao()){

      String token = jedis.hget(email + ":token", "token");

      if(token == null){
        int qtdsEnvios =  Integer.parseInt(jedis.get(email + ":envios"));

        if(qtdsEnvios >= MAXIMO_ENVIO_EMAILS){
          return LIMTE_EMAILS_ATINGIDO;
        }
        return ERRO_LIMITE_TENTATIVAS_TOKEN;
      }

      String qtd = jedis.hget(email + ":token", "qtd");
      int qtdConvertido = Integer.parseInt(qtd);

      if (qtdConvertido >= MAXIMO_TENTATIVAS_POR_TOKEN) {
        jedis.del(email + ":token");
        return ERRO_LIMITE_TENTATIVAS_TOKEN;
      }

      Criptografia criptografia = new Criptografia();
      if (criptografia.verificarBcrypt(tokenRecebido, token)) {
        jedis.del(email + ":token");
        return SUCESSO;
      }

      jedis.hincrBy(email + ":token", "qtd", 1);

      return TOKEN_INVALIDO;
    }catch (JedisException jedisException){
      return ERRO_GENERICO;
    }

  }

  //Métodos para redefinir a senha
  public GenericExceptionEnum alterarSenha(String email, String senha, String senhaConfirmacao){
    GenericExceptionEnum validacaoSenha = validarSenha(senha);
    if(validacaoSenha != VALIDACAO_OK){
      return validacaoSenha;
    }

    if(!senha.equals(senhaConfirmacao)){
      return SENHAS_DIFERENTES;
    }
    return persistirNovaSenha(email, senha.strip());
  }

  private GenericExceptionEnum persistirNovaSenha(String email, String senha){
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    AdminDAO adminDAO = new AdminDAO();

    try(Connection connection = ConexaoPostgresql.getConnection()){
      connection.setAutoCommit(false);

      try{
        Criptografia criptografia = new Criptografia();

        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
        criarInstrucaoDinamica.setCampo("senha", criptografia.porEmBcrypt(senha), Types.VARCHAR);
        criarInstrucaoDinamica.setWhere("email", IGUAL, VAZIO, email, Types.VARCHAR);

        GenericExceptionEnum resultadoUpdateUsuario = usuarioDAO.update(criarInstrucaoDinamica, connection);

        if(resultadoUpdateUsuario == SUCESSO){
          return resultadoUpdateUsuario;
        }

        GenericExceptionEnum resultadoUpdateAdmin = adminDAO.update(criarInstrucaoDinamica, connection);

        connection.commit();
        return resultadoUpdateAdmin;
      } catch (SQLException | RuntimeException exception) {
        connection.rollback();

        throw exception;
      }

    } catch (SQLException | RuntimeException exception) {
      return ERRO_GENERICO;
    }

  }

}
