package conexao;

import config.Env;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class ConexaoRedis extends Env {

  //Uso de pool de conexões para aumentar a velocidade e evitar ficar abrindo e fechando conexões desnecessárias
  private static JedisPool redisPool;

  public static void criarPool() {

    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(20);
    config.setMaxIdle(10);
    config.setMinIdle(2);

    String senha = getRedisSenha();
    int porta = Integer.parseInt(getRedisPort());

    if (!senha.isBlank()) {
      redisPool = new JedisPool(config, getRedisHost(), porta, 2000, senha);
    } else {
      redisPool = new JedisPool(config, getRedisHost(), porta);
    }
  }

  public static Jedis getConexao() {
    return redisPool.getResource();
  }

  public static void closePool(){
    redisPool.close();
  }
}
