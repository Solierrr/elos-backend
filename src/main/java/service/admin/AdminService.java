package service.admin;

import static exception.ErrosGeraisDados.VALIDACAO_OK;

import exception.ErrosGeraisDados;
import exception.GenericExceptionEnum;
import java.util.regex.Pattern;

public final class AdminService {

    private static final int TAMANHO_MINIMO_SENHA = 8;
    private static final int TAMANHO_MAXIMO_SENHA = 60;
    private static final Pattern PATTERN_SENHA = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    private static final int TAMANHO_MAXIMO_EMAIL = 256;

    //Validações

    //Validações do email
    private static GenericExceptionEnum validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return ErrosGeraisDados.EMAIL_VAZIO;
        }

        String emailTratado = email.toLowerCase().strip();
        if(emailTratado.length() > TAMANHO_MAXIMO_EMAIL){
            return ErrosGeraisDados.EMAIL_TAMANHO_INVALIDO;
        }
        return validarFormatoEmail(emailTratado) != VALIDACAO_OK ? ErrosGeraisDados.EMAIL_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarFormatoEmail(String email){
        return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : ErrosGeraisDados.EMAIL_INVALIDO;
    }

    //Validações da senha
    private static GenericExceptionEnum validarSenhaUpdate(String senha){
        if (senha == null || senha.isBlank()){
            return VALIDACAO_OK;
        }

        String senhaTrim = senha.strip();
        return validarSenha(senhaTrim);
    }

    private static GenericExceptionEnum validarSenha(String senha){
        if (senha == null || senha.isBlank()){
            return ErrosGeraisDados.SENHA_VAZIA;
        }

        String senhaTrim = senha.strip();
        if (senhaTrim.length() < TAMANHO_MINIMO_SENHA){
            return ErrosGeraisDados.SENHA_MENOR_QUE_OITO;
        }

        if (senhaTrim.length() > TAMANHO_MAXIMO_SENHA){
            return ErrosGeraisDados.SENHA_TAMANHO_INVALIDO;
        }

        return validarFormatoSenha(senhaTrim);
    }

    private static GenericExceptionEnum validarFormatoSenha(String senha){
        return PATTERN_SENHA.matcher(senha).matches() ? VALIDACAO_OK : ErrosGeraisDados.SENHA_FRACA;
    }

    public static boolean validarDadosLogin(String email, String senha){
        return validarEmail(email) == VALIDACAO_OK && validarSenha(senha) == VALIDACAO_OK;
    }


}
