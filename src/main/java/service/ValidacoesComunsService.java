package service;

import static exception.ErrosGeraisDados.ID_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;

import exception.GenericExceptionEnum;

public class ValidacoesComunsService {

  public static GenericExceptionEnum validarId(String id) {
    try {
      String idTratado = id.strip();
      Long.parseLong(idTratado);
      return VALIDACAO_OK;
    } catch (NumberFormatException numberFormatException) {
      return ID_INVALIDO;
    }
  }

}
