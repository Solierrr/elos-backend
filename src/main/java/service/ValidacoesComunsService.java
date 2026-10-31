package service;

import static exception.ErrosGeraisDados.ID_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;

import exception.GenericExceptionEnum;

public class ValidacoesComunsService {

  //Método utilitario utilizado em todos os services
  public static GenericExceptionEnum validarId(String id) {
    try {
      String idTratado = id.strip();
      Long.parseLong(idTratado);
      return VALIDACAO_OK;
    } catch (NumberFormatException | NullPointerException exception) {
      return ID_INVALIDO;
    }
  }
}
