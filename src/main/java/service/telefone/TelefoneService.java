package service.telefone;

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
import dao.TelefoneDAO;
import exception.ErrosDadosTelefone;
import exception.ErrosGeraisDados;
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
import model.Telefone;
import model.TiposTelefone;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import util.Anonimizador;

public final class TelefoneService {

  // Constante usada para operações json no código
  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  // Constante utilizada pelo código
  private static final Pattern PATTERN_TELEFONE = Pattern.compile("^[0-9]{2}[0-9]{8,9}$");

  // Validações

  // Validação do telefone
  private static GenericExceptionEnum validarTelefone(String telefone) {
    if (telefone == null || telefone.isBlank()) {
      return ErrosDadosTelefone.TELEFONE_VAZIO;
    }

    String telefoneTratado =
        telefone.strip().replace("(", "").replace(")", "").replaceAll("\\s", "").replace("-", "");
    return validarFormatoTelefone(telefoneTratado);
  }

  private static GenericExceptionEnum validarFormatoTelefone(String telefone) {
    return PATTERN_TELEFONE.matcher(telefone).matches()
        ? VALIDACAO_OK
        : ErrosDadosTelefone.TELEFONE_INVALIDO;
  }

  // Validação do id do usuário
  private static GenericExceptionEnum validarIdUsuario(String idUsuario) {
    if (idUsuario == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idUsuario) != VALIDACAO_OK) {
      return ErrosGeraisDados.ID_USUARIO_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  // Validação do tipo
  private static GenericExceptionEnum validarTipo(String tipo) {
    if (tipo == null || tipo.isBlank()) {
      return ErrosDadosTelefone.TIPO_VAZIO;
    }
    return TiposTelefone.descobrirTipoTelefone(tipo) == null
        ? ErrosDadosTelefone.TELEFONE_INVALIDO
        : VALIDACAO_OK;
  }

  // Validação do principal
  private static GenericExceptionEnum validarPrincipal(String principal) {
    if (principal == null || principal.isBlank()) {
      return ErrosDadosTelefone.PRINCIPAL_INVALIDO;
    }

    if ("true".equalsIgnoreCase(principal.toLowerCase().trim())
        || "false".equalsIgnoreCase(principal.toLowerCase().trim())) {
      return VALIDACAO_OK;
    }
    return ErrosDadosTelefone.PRINCIPAL_INVALIDO;
  }

  // Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarTelefone(id, idAdmin);
  }

  private GenericExceptionEnum deletarTelefone(String id, Long idAdmin) {
    TelefoneDAO dao = new TelefoneDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);
      try {
        Long idConvertido = Long.parseLong(id.strip());

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

        List<Map<String, Object>> telefone = dao.readAll(criarInstrucaoDinamicaRead, connection);

        if (telefone.isEmpty()) {
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
                "TELEFONE",
                "DELETE",
                GSON.toJsonTree(telefone.getFirst()).getAsJsonObject(),
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
    TelefoneDAO dao = new TelefoneDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      List<Map<String, Object>> telefones = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(telefones, admin);

      return telefones;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosTelefoneDTO filtrosTelefoneDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosTelefoneDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarTelefoneSelect(filtrosTelefoneDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerTelefones(filtrosTelefoneDTO, admin);
  }

  private List<Map<String, Object>> lerTelefones(
      FiltrosTelefoneDTO filtrosTelefoneDTO, Admin admin) {
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      TelefoneDAO dao = new TelefoneDAO();

      if (filtrosTelefoneDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefone.ID.getNomeCampo(),
            IGUAL,
            VAZIO,
            filtrosTelefoneDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposTelefone.ID.getNomeCampo(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosTelefoneDTO.idUsuarioFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefone.ID_USUARIO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosTelefoneDTO.formatarIdUsuario(),
            Types.BIGINT);
      }

      if (filtrosTelefoneDTO.telefoneFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefone.TELEFONE.getNomeCampo(),
            ILIKE,
            AND,
            filtrosTelefoneDTO.formatarTelefone(),
            Types.VARCHAR);
      }

      if (filtrosTelefoneDTO.tipoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefone.TIPO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosTelefoneDTO.formatarTipo(),
            Types.VARCHAR);
      }

      if (filtrosTelefoneDTO.principalFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposTelefone.PRINCIPAL.getNomeCampo(),
            IGUAL,
            AND,
            filtrosTelefoneDTO.formatarPrincipal(),
            Types.BOOLEAN);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosTelefoneDTO.joinFiltro());
      if (filtrosTelefoneDTO.joinFiltro() != null) {
        String conector = JoinsPossiveis.joinValido("telefone", filtrosTelefoneDTO.joinFiltro());

        //Inverte a ordem das tabelas de acordo com quem tem a FK
        if (conector.contains("telefone")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> telefones = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosTelefoneDTO.filtroRapido() != null
          && !filtrosTelefoneDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosTelefoneDTO.filtroRapido();

        telefones.removeIf(telefone ->
            telefone.values().stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .noneMatch(valorTexto -> valorTexto.contains(termoFiltro)));
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(telefones, admin);

      return telefones;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarTelefoneSelect(FiltrosTelefoneDTO filtrosTelefoneDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosTelefoneDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosTelefoneDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneDTO.idUsuarioFiltro() != null) {
      GenericExceptionEnum validacao = validarIdUsuario(filtrosTelefoneDTO.idUsuarioFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneDTO.telefoneFiltro() != null) {
      GenericExceptionEnum validacao = validarTelefone(filtrosTelefoneDTO.telefoneFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneDTO.tipoFiltro() != null) {
      GenericExceptionEnum validacao = validarTipo(filtrosTelefoneDTO.tipoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneDTO.principalFiltro() != null) {
      GenericExceptionEnum validacao = validarPrincipal(filtrosTelefoneDTO.principalFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosTelefoneDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("telefone", filtrosTelefoneDTO.joinFiltro()) == null) {
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

    GenericExceptionEnum resultado = atualizarTelefone(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarTelefone(JsonObject jsonObject, Long idAdmin) {
    TelefoneDAO dao = new TelefoneDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        Telefone telefone =
            GSON.fromJson(jsonObject, TelefoneParaUpdateDTO.class).construirTelefone();
        Long id = telefone.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> telefoneLido =
            dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (telefoneLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposTelefone.TELEFONE.getNomeCampo(), telefone.getTelefone(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposTelefone.TIPO.getNomeCampo(),
            telefone.getTipo().getTipoTelefone(),
            Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposTelefone.PRINCIPAL.getNomeCampo(), telefone.isPrincipal(), Types.BOOLEAN);

        criarInstrucaoDinamicaUpdate.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        dao.update(criarInstrucaoDinamicaUpdate, connection);

        jsonObject
            .entrySet()
            .removeIf(campo ->
                campo.getValue() != null
                    && !campo.getValue().isJsonNull()
                    && (campo.getValue().isJsonPrimitive()
                        && campo.getValue().getAsString().trim().isEmpty()));

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                id,
                "TELEFONE",
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

    TelefoneParaUpdateDTO telefoneParaUpdateDTO =
        GSON.fromJson(jsonObject, TelefoneParaUpdateDTO.class);

    GenericExceptionEnum telefoneValidacao =
        validarTelefone(telefoneParaUpdateDTO.telefoneUpdate());
    if (telefoneValidacao != VALIDACAO_OK
        && (telefoneParaUpdateDTO.telefoneUpdate() != null
            && !telefoneParaUpdateDTO.telefoneUpdate().isBlank())) {
      erros.add(telefoneValidacao);
    }

    GenericExceptionEnum tipoValidacao = validarTipo(telefoneParaUpdateDTO.tipoUpdate());
    if (tipoValidacao != VALIDACAO_OK
        && (telefoneParaUpdateDTO.tipoUpdate() != null
            && !telefoneParaUpdateDTO.tipoUpdate().isBlank())) {
      erros.add(tipoValidacao);
    }

    GenericExceptionEnum principalValidacao =
        validarPrincipal(telefoneParaUpdateDTO.principalUpdate());
    if (principalValidacao != VALIDACAO_OK
        && (telefoneParaUpdateDTO.principalUpdate() != null
            && !telefoneParaUpdateDTO.principalUpdate().isBlank())) {
      erros.add(principalValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirTelefoneParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposTelefone.ID.getNomeCampo(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      TelefoneDAO dao = new TelefoneDAO();

      List<Map<String, Object>> resultados = dao.readAll(criarInstrucaoDinamica, connection);

      if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
        for (Map<String, Object> registro : resultados) {
          registro.remove("telefone");
        }
      }

      return resultados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  // Métodos relacionados ao insert
  public List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> mensagens = validarTelefoneInsert(jsonObject);
    if (mensagens.isEmpty()) {
      GenericExceptionEnum resultado = persistirTelefone(jsonObject, idAdmin);
      if (resultado != SUCESSO) {
        mensagens.add(resultado);
      }
    }
    return mensagens;
  }

  private List<GenericExceptionEnum> validarTelefoneInsert(JsonObject jsonObject) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    TelefoneParaInsertDTO telefoneParaInsertDTO =
        GSON.fromJson(jsonObject, TelefoneParaInsertDTO.class);

    GenericExceptionEnum validacaoTelefone = validarTelefone(telefoneParaInsertDTO.telefone());
    if (validacaoTelefone != VALIDACAO_OK) {
      listaDeErros.add(validacaoTelefone);
    }

    GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(telefoneParaInsertDTO.idUsuario());
    if (validacaoIdUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoIdUsuario);
    }

    GenericExceptionEnum validacaoTipo = validarTipo(telefoneParaInsertDTO.tipo());
    if (validacaoTipo != VALIDACAO_OK) {
      listaDeErros.add(validacaoTipo);
    }

    GenericExceptionEnum validacaoPrincipal = validarPrincipal(telefoneParaInsertDTO.principal());
    if (validacaoPrincipal != VALIDACAO_OK) {
      listaDeErros.add(validacaoPrincipal);
    }
    return listaDeErros;
  }

  private GenericExceptionEnum persistirTelefone(JsonObject jsonObject, Long idAdmin) {
    TelefoneDAO dao = new TelefoneDAO();

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    Telefone telefone = GSON.fromJson(jsonObject, TelefoneParaInsertDTO.class).construirTelefone();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        criarInstrucaoDinamica.setCampo(
            CamposTelefone.TELEFONE.getNomeCampo(), telefone.getTelefone(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposTelefone.ID_USUARIO.getNomeCampo(), telefone.getIdUsuario(), Types.BIGINT);
        criarInstrucaoDinamica.setCampo(
            CamposTelefone.TIPO.getNomeCampo(),
            telefone.getTipo().getTipoTelefone(),
            Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposTelefone.PRINCIPAL.getNomeCampo(), telefone.isPrincipal(), Types.BOOLEAN);

        Long idRegistro = dao.insert(criarInstrucaoDinamica, connection);

        if (idRegistro == null) {
          throw new IllegalArgumentException("Ocorreu algum erro e o telefone não foi registrado!");
        }

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                idRegistro,
                "TELEFONE",
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
