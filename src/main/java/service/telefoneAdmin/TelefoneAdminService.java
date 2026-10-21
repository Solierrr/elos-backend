package service.telefoneAdmin;

import static exception.ErrosGeraisDados.VALIDACAO_OK;

import exception.ErrosDadosTelefone;
import exception.ErrosDadosTelefoneAdmin;
import exception.ErrosGeraisDados;
import exception.GenericExceptionEnum;
import java.util.regex.Pattern;
import model.TiposTelefone;
import service.ValidacoesComunsService;

public final class TelefoneAdminService {

  private static final Pattern PATTERN_TELEFONE = Pattern.compile("^[0-9]{2}(9)?[0-9]{8}$");

  //Validações

  //Validação do telefone
  private static GenericExceptionEnum validarTelefone(String telefone) {
    if (telefone == null || telefone.isBlank()) {
      return ErrosDadosTelefoneAdmin.TELEFONE_VAZIO;
    }

    String telefoneTratado = telefone.strip().replace("(", "").replace(")", "");
    return validarFormatoTelefone(telefoneTratado);
  }

  private static GenericExceptionEnum validarFormatoTelefone(String telefone) {
    return PATTERN_TELEFONE.matcher(telefone).matches() ? VALIDACAO_OK
        : ErrosDadosTelefoneAdmin.TELEFONE_INVALIDO;
  }

  //Validação do id do usuário
  private static GenericExceptionEnum validarIdAdmin(String idAdmin) {
    if (idAdmin == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idAdmin) != VALIDACAO_OK) {
      return ErrosDadosTelefoneAdmin.ID_ADMIN_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  //Validação do tipo
  private static GenericExceptionEnum validarTipo(String tipo) {
    if (tipo == null || tipo.isBlank()) {
      return ErrosDadosTelefoneAdmin.TIPO_VAZIO;
    }
    return TiposTelefone.descobrirTipoTelefone(tipo) == null ? ErrosDadosTelefoneAdmin.TELEFONE_INVALIDO
        : VALIDACAO_OK;
  }

  //Validação do principalFiltro
  private static GenericExceptionEnum validarPrincipal(String ehMandante) {
    if (ehMandante == null || ehMandante.isBlank()) {
      return ErrosDadosTelefoneAdmin.PRINCIPAL_INVALIDO;
    }

    if ("true".equalsIgnoreCase(ehMandante.toLowerCase().trim()) || "false".equalsIgnoreCase(
        ehMandante.toLowerCase().trim())) {
      return VALIDACAO_OK;
    }
    return ErrosDadosTelefoneAdmin.PRINCIPAL_INVALIDO;
  }


}
