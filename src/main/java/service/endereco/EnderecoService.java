package service.endereco;

import static exception.ErrosGeraisDados.VALIDACAO_OK;

import exception.ErrosDadosEndereco;
import exception.ErrosDadosTelefoneAdmin;
import exception.GenericExceptionEnum;
import java.util.Locale;
import java.util.regex.Pattern;

public final class EnderecoService {

  private static final Pattern PATTERN_CIDADE = Pattern.compile("^[\\p{Script=Latin}\\u0027\\-\\s]+$");

  private static final Pattern PATTERN_BAIRRO = Pattern.compile("^[\\p{Script=Latin}0-9.\\u0027\\-\\s]+$");

  private static final Pattern PATTERN_CEP = Pattern.compile("^\\d{8}$");

  private static final Pattern PATTERN_LOGRADOURO = Pattern.compile("^[\\p{Script=Latin}0-9,.\\-/ªº\\s]+$");

  private static final Pattern PATTERN_NUMERO = Pattern.compile("^\\d+$");

  private static final String SEM_NUMERO = "S/N";

  private static GenericExceptionEnum validarCidade(String cidade){
    if(cidade == null || cidade.isBlank()){
      return ErrosDadosEndereco.CIDADE_VAZIA;
    }

    String cidadeTratada = cidade.strip();
    return validarFormatoCidade(cidadeTratada);
  }

  private static GenericExceptionEnum validarFormatoCidade(String cidade) {
    return PATTERN_CIDADE.matcher(cidade).matches() ? VALIDACAO_OK
        : ErrosDadosEndereco.CIDADE_INVALIDA;
  }

  private static GenericExceptionEnum validarBairro(String bairro){
    if(bairro == null || bairro.isBlank()){
      return ErrosDadosEndereco.BAIRRO_VAZIO;
    }

    String bairroTratada = bairro.strip();
    return validarFormatoBairro(bairroTratada);
  }

  private static GenericExceptionEnum validarFormatoBairro(String bairro) {
    return PATTERN_BAIRRO.matcher(bairro).matches() ? VALIDACAO_OK
        : ErrosDadosEndereco.BAIRRO_INVALIDO;
  }

  private static GenericExceptionEnum validarCep(String cep){
    if(cep == null || cep.isBlank()){
      return ErrosDadosEndereco.CEP_VAZIO;
    }

    String cepTratada = cep.strip().replace("-", "");
    return validarFormatoCep(cepTratada);
  }

  private static GenericExceptionEnum validarFormatoCep(String cep) {
    return PATTERN_CEP.matcher(cep).matches() ? VALIDACAO_OK
        : ErrosDadosEndereco.CEP_INVALIDO;
  }

  private static GenericExceptionEnum validarLogradouro(String logradouro){
    if(logradouro == null || logradouro.isBlank()){
      return ErrosDadosEndereco.LOGRADOURO_VAZIO;
    }

    String logradouroTratada = logradouro.strip();
    return validarFormatoLogradouro(logradouroTratada);
  }

  private static GenericExceptionEnum validarFormatoLogradouro(String logradouro) {
    return PATTERN_LOGRADOURO.matcher(logradouro).matches() ? VALIDACAO_OK
        : ErrosDadosEndereco.LOGRADOURO_INVALIDO;
  }

  private static GenericExceptionEnum validarNumero(String numero){
    if(numero == null || numero.isBlank()){
      return ErrosDadosEndereco.NUMERO_VAZIO;
    }

    String numeroTratada = numero.strip();
    return validarFormatoNumero(numeroTratada);
  }

  private static GenericExceptionEnum validarFormatoNumero(String numero) {
    return PATTERN_NUMERO.matcher(numero).matches() || numero.equalsIgnoreCase(SEM_NUMERO) ? VALIDACAO_OK
        : ErrosDadosEndereco.NUMERO_INVALIDO;
  }



}
