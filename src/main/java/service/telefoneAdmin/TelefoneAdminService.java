package service.telefoneAdmin;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGeraisDados.JOIN_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static service.ValidacoesComunsService.validarId;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import conexao.ConexaoPostgresql;
import dao.AcoesInstrucao;
import dao.ConstraintsBanco;
import dao.CriarInstrucaoDinamica;
import dao.TelefoneAdminDAO;
import exception.ErrosDadosTelefoneAdmin;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import model.Admin;
import model.FuncoesAdmin;
import model.HistoricoAlteracoesAdmin;
import model.TelefoneAdmin;
import model.TiposTelefone;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import util.Anonimizador;

public final class TelefoneAdminService {

  // Constante usada para operações json no código
  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  //Constante utilizada pelo código
  private static final Pattern PATTERN_TELEFONE = Pattern.compile("^[0-9]{2}(9)?[0-9]{8}$");

  // Validações

  // Validação do telefone
  private static GenericExceptionEnum validarTelefone(String telefone) {
    if (telefone == null || telefone.isBlank()) {
      return ErrosDadosTelefoneAdmin.TELEFONE_VAZIO;
    }

    String telefoneTratado =
        telefone.strip().replace("(", "").replace(")", "").replaceAll("\\s", "").replace("-", "");

    return validarFormatoTelefone(telefoneTratado);
  }

  private static GenericExceptionEnum validarFormatoTelefone(String telefone) {
    return PATTERN_TELEFONE.matcher(telefone).matches()
        ? VALIDACAO_OK
        : ErrosDadosTelefoneAdmin.TELEFONE_INVALIDO;
  }

  // Validação do id do admin
  private static GenericExceptionEnum validarIdAdmin(String idAdmin) {
    if (idAdmin == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idAdmin) != VALIDACAO_OK) {
      return ErrosDadosTelefoneAdmin.ID_ADMIN_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  // Validação do tipo
  private static GenericExceptionEnum validarTipo(String tipo) {
    if (tipo == null || tipo.isBlank()) {
      return ErrosDadosTelefoneAdmin.TIPO_VAZIO;
    }
    return TiposTelefone.descobrirTipoTelefone(tipo) == null
        ? ErrosDadosTelefoneAdmin.TELEFONE_INVALIDO
        : VALIDACAO_OK;
  }

  // Validação do principalFiltro
  private static GenericExceptionEnum validarPrincipal(String principal) {
    if (principal == null || principal.isBlank()) {
      return ErrosDadosTelefoneAdmin.PRINCIPAL_INVALIDO;
    }

    if ("true".equalsIgnoreCase(principal.toLowerCase().trim())
        || "false".equalsIgnoreCase(principal.toLowerCase().trim())) {
      return VALIDACAO_OK;
    }
    return ErrosDadosTelefoneAdmin.PRINCIPAL_INVALIDO;
  }

  // Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarTelefoneAdmin(id, idAdmin);
  }

  private GenericExceptionEnum deletarTelefoneAdmin(String id, Long idAdmin) {
    TelefoneAdminDAO dao = new TelefoneAdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);
      try {
        Long idConvertido = Long.parseLong(id.strip());

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

        List<Map<String, Object>> telefoneAdmin =
            dao.readAll(criarInstrucaoDinamicaRead, connection);

        if (telefoneAdmin.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaDelete = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaDelete.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

        dao.delete(criarInstrucaoDinamicaDelete, connection);

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                idConvertido,
                "TELEFONE_ADMIN",
                "DELETE",
                GSON.toJsonTree(telefoneAdmin.getFirst()).getAsJsonObject(),
                new JsonObject(),
                null),
            connection);

        connection.commit();
        return SUCESSO;
      } catch (SQLException | IllegalArgumentException exception) {
        connection.rollback();

        throw exception;
      }

    } catch (SQLException | IllegalArgumentException exception) {
      return ERRO_GENERICO;
    }
  }

  // Métodos relacionados ao select
  public List<Map<String, Object>> realizarSelectInicial(Admin admin) {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    TelefoneAdminDAO dao = new TelefoneAdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      List<Map<String, Object>> telefonesAdmin = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(telefonesAdmin, admin);

      return telefonesAdmin;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosTelefoneAdminDTO filtrosTelefoneAdminDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosTelefoneAdminDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarTelefoneAdminSelect(filtrosTelefoneAdminDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerTelefonesAdmin(filtrosTelefoneAdminDTO, admin);
  }

  private List<Map<String, Object>> lerTelefonesAdmin(
      FiltrosTelefoneAdminDTO filtrosTelefoneAdminDTO, Admin admin) {
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      TelefoneAdminDAO dao = new TelefoneAdminDAO();

      if (filtrosTelefoneAdminDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefoneAdmin.ID.getNomeCampo(),
            IGUAL,
            VAZIO,
            filtrosTelefoneAdminDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposTelefoneAdmin.ID.getNomeCampo(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosTelefoneAdminDTO.idAdminFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefoneAdmin.ID_ADMIN.getNomeCampo(),
            IGUAL,
            AND,
            filtrosTelefoneAdminDTO.formatarIdAdmin(),
            Types.BIGINT);
      }

      if (filtrosTelefoneAdminDTO.telefoneFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefoneAdmin.TELEFONE.getNomeCampo(),
            ILIKE,
            AND,
            filtrosTelefoneAdminDTO.formatarTelefone(),
            Types.VARCHAR);
      }

      if (filtrosTelefoneAdminDTO.tipoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefoneAdmin.TIPO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosTelefoneAdminDTO.formatarTipo(),
            Types.VARCHAR);
      }

      if (filtrosTelefoneAdminDTO.principalFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefoneAdmin.PRINCIPAL.getNomeCampo(),
            IGUAL,
            AND,
            filtrosTelefoneAdminDTO.formatarPrincipal(),
            Types.BOOLEAN);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosTelefoneAdminDTO.joinFiltro());
      if (filtrosTelefoneAdminDTO.joinFiltro() != null) {
        String conector =
            JoinsPossiveis.joinValido("telefone_admin", filtrosTelefoneAdminDTO.joinFiltro());

        if (conector.contains("telefone")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> telefonesAdmin = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosTelefoneAdminDTO.filtroRapido() != null
          && !filtrosTelefoneAdminDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosTelefoneAdminDTO.filtroRapido();

        telefonesAdmin.removeIf(
            telefoneAdmin ->
                telefoneAdmin.values().stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .noneMatch(valorTexto -> valorTexto.contains(termoFiltro)));
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(telefonesAdmin, admin);

      return telefonesAdmin;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarTelefoneAdminSelect(
      FiltrosTelefoneAdminDTO filtrosTelefoneAdminDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosTelefoneAdminDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosTelefoneAdminDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneAdminDTO.idAdminFiltro() != null) {
      GenericExceptionEnum validacao = validarIdAdmin(filtrosTelefoneAdminDTO.idAdminFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneAdminDTO.telefoneFiltro() != null) {
      GenericExceptionEnum validacao = validarTelefone(filtrosTelefoneAdminDTO.telefoneFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneAdminDTO.tipoFiltro() != null) {
      GenericExceptionEnum validacao = validarTipo(filtrosTelefoneAdminDTO.tipoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneAdminDTO.principalFiltro() != null) {
      GenericExceptionEnum validacao = validarPrincipal(filtrosTelefoneAdminDTO.principalFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneAdminDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("telefone_admin", filtrosTelefoneAdminDTO.joinFiltro())
          == null) {
        erros.add(JOIN_INVALIDO);
      }
    }
    return erros;
  }

  // Métodos relacionados ao update
  public List<GenericExceptionEnum> realizarUpdate(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> erros = validarUpdate(jsonObject);
    if (!erros.isEmpty()) {
      return erros;
    }

    GenericExceptionEnum resultado = atualizarTelefoneAdmin(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarTelefoneAdmin(JsonObject jsonObject, Long idAdmin) {
    TelefoneAdminDAO dao = new TelefoneAdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        TelefoneAdmin telefoneAdmin =
            GSON.fromJson(jsonObject, TelefoneAdminParaUpdateDTO.class).construirTelefoneAdmin();
        Long id = telefoneAdmin.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> telefoneLido =
            dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (telefoneLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposTelefoneAdmin.TELEFONE.getNomeCampo(), telefoneAdmin.getTelefone(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposTelefoneAdmin.TIPO.getNomeCampo(),
            telefoneAdmin.getTipo().getTipoTelefone(),
            Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposTelefoneAdmin.PRINCIPAL.getNomeCampo(),
            telefoneAdmin.isPrincipal(),
            Types.BOOLEAN);

        criarInstrucaoDinamicaUpdate.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        dao.update(criarInstrucaoDinamicaUpdate, connection);

        jsonObject
            .entrySet()
            .removeIf(
                campo ->
                    campo.getValue() != null
                        && !campo.getValue().isJsonNull()
                        && (campo.getValue().isJsonPrimitive()
                            && campo.getValue().getAsString().trim().isEmpty()));

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                id,
                "TELEFONE_ADMIN",
                "UPDATE",
                GSON.toJsonTree(telefoneLido.getFirst()).getAsJsonObject(),
                jsonObject,
                null),
            connection);

        connection.commit();
        return SUCESSO;
      } catch (SQLException | IllegalArgumentException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException | IllegalArgumentException exception) {
      if (exception instanceof SQLException) {
        return ConstraintsBanco.foiCausadoPorQual((SQLException) exception);
      }

      return ERRO_GENERICO;
    }
  }

  private List<GenericExceptionEnum> validarUpdate(JsonObject jsonObject) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    TelefoneAdminParaUpdateDTO telefoneAdminParaUpdateDTO =
        GSON.fromJson(jsonObject, TelefoneAdminParaUpdateDTO.class);

    GenericExceptionEnum telefoneValidacao =
        validarTelefone(telefoneAdminParaUpdateDTO.telefoneUpdate());
    if (telefoneValidacao != VALIDACAO_OK
        && (telefoneAdminParaUpdateDTO.telefoneUpdate() != null
            && !telefoneAdminParaUpdateDTO.telefoneUpdate().isBlank())) {
      erros.add(telefoneValidacao);
    }

    GenericExceptionEnum tipoValidacao = validarTipo(telefoneAdminParaUpdateDTO.tipoUpdate());
    if (tipoValidacao != VALIDACAO_OK
        && (telefoneAdminParaUpdateDTO.tipoUpdate() != null
            && !telefoneAdminParaUpdateDTO.tipoUpdate().isBlank())) {
      erros.add(tipoValidacao);
    }

    GenericExceptionEnum principalValidacao =
        validarPrincipal(telefoneAdminParaUpdateDTO.principalUpdate());
    if (principalValidacao != VALIDACAO_OK
        && (telefoneAdminParaUpdateDTO.principalUpdate() != null
            && !telefoneAdminParaUpdateDTO.principalUpdate().isBlank())) {
      erros.add(principalValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirTelefoneAdminParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposTelefoneAdmin.ID.getNomeCampo(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      TelefoneAdminDAO dao = new TelefoneAdminDAO();

      List<Map<String, Object>> resultados = dao.readAll(criarInstrucaoDinamica, connection);

      if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
        for (Map<String, Object> registro : resultados) {
          registro.remove("telefone_admin");
        }
      }

      return resultados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  // Métodos relacionados ao insert
  public List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> mensagens = validarTelefoneAdminInsert(jsonObject);
    if (mensagens.isEmpty()) {
      GenericExceptionEnum resultado = persistirTelefoneAdmin(jsonObject, idAdmin);
      if (resultado != SUCESSO) {
        mensagens.add(resultado);
      }
    }
    return mensagens;
  }

  private List<GenericExceptionEnum> validarTelefoneAdminInsert(JsonObject jsonObject) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    TelefoneAdminParaInsertDTO telefoneAdminParaInsertDTO =
        GSON.fromJson(jsonObject, TelefoneAdminParaInsertDTO.class);

    GenericExceptionEnum validacaoTelefone = validarTelefone(telefoneAdminParaInsertDTO.telefone());
    if (validacaoTelefone != VALIDACAO_OK) {
      listaDeErros.add(validacaoTelefone);
    }

    GenericExceptionEnum validacaoIdUsuario = validarIdAdmin(telefoneAdminParaInsertDTO.idAdmin());
    if (validacaoIdUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoIdUsuario);
    }

    GenericExceptionEnum validacaoTipo = validarTipo(telefoneAdminParaInsertDTO.tipo());
    if (validacaoTipo != VALIDACAO_OK) {
      listaDeErros.add(validacaoTipo);
    }

    GenericExceptionEnum validacaoPrincipal =
        validarPrincipal(telefoneAdminParaInsertDTO.principal());
    if (validacaoPrincipal != VALIDACAO_OK) {
      listaDeErros.add(validacaoPrincipal);
    }
    return listaDeErros;
  }

  private GenericExceptionEnum persistirTelefoneAdmin(JsonObject jsonObject, Long idAdmin) {
    TelefoneAdminDAO dao = new TelefoneAdminDAO();

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    TelefoneAdmin telefoneAdmin =
        GSON.fromJson(jsonObject, TelefoneAdminParaInsertDTO.class).construirTelefoneAdmin();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        criarInstrucaoDinamica.setCampo(
            CamposTelefoneAdmin.TELEFONE.getNomeCampo(), telefoneAdmin.getTelefone(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposTelefoneAdmin.ID_ADMIN.getNomeCampo(), telefoneAdmin.getIdAdmin(), Types.BIGINT);
        criarInstrucaoDinamica.setCampo(
            CamposTelefoneAdmin.TIPO.getNomeCampo(),
            telefoneAdmin.getTipo().getTipoTelefone(),
            Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposTelefoneAdmin.PRINCIPAL.getNomeCampo(),
            telefoneAdmin.isPrincipal(),
            Types.BOOLEAN);

        Long idRegistro = dao.insert(criarInstrucaoDinamica, connection);

        if (idRegistro == null) {
          throw new IllegalArgumentException("Ocorreu algum erro e o telefone do admin não foi registrado!");
        }

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                idRegistro,
                "TELEFONE_ADMIN",
                "INSERT",
                new JsonObject(),
                jsonObject,
                null),
            connection);

        connection.commit();
        return SUCESSO;
      } catch (SQLException | IllegalArgumentException exception) {
        connection.rollback();
        throw exception;
      }

    } catch (SQLException | IllegalArgumentException exception) {
      if (exception instanceof SQLException) {
        return ConstraintsBanco.foiCausadoPorQual((SQLException) exception);
      }

      return ERRO_GENERICO;
    }
  }
}
