package conexao;

import config.Env;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ConexaoRedis extends Env {

  private static final JedisPool redisPool = criarPool();

  private static JedisPool criarPool() {

    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(20);
    config.setMaxIdle(10);
    config.setMinIdle(2);

    String senha = getRedisSenha();
    int porta = Integer.parseInt(getRedisPort());

    if (!senha.isBlank()) {
      return new JedisPool(config, getRedisHost(), porta, 2000, senha);
    } else {
      return new JedisPool(config, getRedisHost(), porta);
    }
  }

  public static Jedis getConexao() {
    return redisPool.getResource();
  }
}
