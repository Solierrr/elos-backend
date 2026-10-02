package conexao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.DriverManager;
import java.sql.Connection;

public class Conexao {

    private static final Dotenv VARIAVEIS_DE_AMBIENTE = Dotenv.load();
    private Connection connection;

    public Connection conectar(){
        try{
            final String URL = validarEnvs(VARIAVEIS_DE_AMBIENTE.get("DB_URL"), "DB_URL");
            final String USUARIO = validarEnvs(VARIAVEIS_DE_AMBIENTE.get("DB_USUARIO"), "DB_USUARIO");
            final String SENHA = validarEnvs(VARIAVEIS_DE_AMBIENTE.get("DB_SENHA"), "DB_SENHA");

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USUARIO, SENHA);
            return connection;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void desconectar(){
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Método auxiliar para o método conectar
    private static String validarEnvs(String valorEnv, String nomeEnv){
        if (valorEnv == null){
            throw new NullPointerException(String.format("Erro: A variável de ambiente %s não está registrada no .env", nomeEnv));
        }
        else if(valorEnv.isEmpty()){
            throw new IllegalArgumentException(String.format("Erro: A variável de ambiente %s não possuí um valor registrado", nomeEnv));
        }
        return valorEnv.strip();
    }
}