package service.historicoAlteracoesAdmin;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosGeraisDados.JOIN_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static service.ValidacoesComunsService.validarId;

import conexao.ConexaoPostgresql;
import dao.AcoesInstrucao;
import dao.CriarInstrucaoDinamica;
import dao.HistoricoAlteracoesAdminDAO;
import exception.ErrosDadosHistoricoAlteracoes;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import model.Admin;
import model.HistoricoAlteracoesAdmin;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import util.Anonimizador;

public final class HistoricoAlteracoesAdminService {

  // Constante utilizada pelo código
  private static final String[] ACOES = new String[] {"UPDATE", "DELETE", "INSERT"};

  // Usado  para permitir que seja possivel ver as alterações do banco e qual admin as fez
  public static void salvarAcao(
      HistoricoAlteracoesAdmin historicoAlteracoesAdmin, Connection connection)
      throws SQLException {

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

    criarInstrucaoDinamica.setCampo(
        "id_admin", historicoAlteracoesAdmin.getIdAdmin(), Types.BIGINT);
    criarInstrucaoDinamica.setCampo(
        "id_registro", historicoAlteracoesAdmin.getIdRegistro(), Types.BIGINT);
    criarInstrucaoDinamica.setCampo(
        "tabela_modificada", historicoAlteracoesAdmin.getTabelaModificada(), Types.VARCHAR);
    criarInstrucaoDinamica.setCampo("acao", historicoAlteracoesAdmin.getAcao(), Types.VARCHAR);
    criarInstrucaoDinamica.setCampo(
        "dados_antigos", historicoAlteracoesAdmin.getDadosAntigos(), Types.OTHER);
    criarInstrucaoDinamica.setCampo(
        "dados_atualizados", historicoAlteracoesAdmin.getDadosAtualizados(), Types.OTHER);

    HistoricoAlteracoesAdminDAO dao = new HistoricoAlteracoesAdminDAO();

    dao.insert(criarInstrucaoDinamica, connection);
  }

  // Validações

  // Validação do id do admin
  private static GenericExceptionEnum validarIdAdmin(String idAdmin) {
    if (idAdmin == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idAdmin) != VALIDACAO_OK) {
      return ErrosDadosHistoricoAlteracoes.ID_ADMIN_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  // Validação do id do registro
  private static GenericExceptionEnum validarIdRegistro(String idRegistro) {
    if (idRegistro == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idRegistro) != VALIDACAO_OK) {
      return ErrosDadosHistoricoAlteracoes.ID_REGISTRO_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  // Validação da data de alteração
  private static GenericExceptionEnum validarDataAlteracao(String dataAlteracao) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {
      LocalDate data = LocalDate.parse(dataAlteracao.strip(), formatador);

      return VALIDACAO_OK;
    } catch (DateTimeParseException dateTimeParseException) {
      return ErrosDadosHistoricoAlteracoes.DATA_INVALIDA;
    }
  }

  private static GenericExceptionEnum validarAcao(String acao) {
    if (acao == null || acao.isBlank()) {
      return ErrosDadosHistoricoAlteracoes.ACAO_INVALIDA;
    }

    for (String acaoMapeada : ACOES) {
      if (acaoMapeada.equalsIgnoreCase(acao)) {
        return VALIDACAO_OK;
      }
    }
    return ErrosDadosHistoricoAlteracoes.ACAO_INVALIDA;
  }

  // Métodos relacionados ao select
  public List<Map<String, Object>> realizarSelectInicial(Admin admin) {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    HistoricoAlteracoesAdminDAO dao = new HistoricoAlteracoesAdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      List<Map<String, Object>> historicosAlteracoes =
          dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(historicosAlteracoes, admin);

      return historicosAlteracoes;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosHistoricoAlteracoesDTO filtrosHistoricoAlteracoesDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosHistoricoAlteracoesDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarLogAcessoAdminSelect(filtrosHistoricoAlteracoesDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerHistoricoAlteracoes(filtrosHistoricoAlteracoesDTO, admin);
  }

  private List<Map<String, Object>> lerHistoricoAlteracoes(
      FiltrosHistoricoAlteracoesDTO filtrosHistoricoAlteracoesDTO, Admin admin) {
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      HistoricoAlteracoesAdminDAO dao = new HistoricoAlteracoesAdminDAO();

      if (filtrosHistoricoAlteracoesDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposHistoricoAlteracoes.ID.getNomeCampo(),
            IGUAL,
            VAZIO,
            filtrosHistoricoAlteracoesDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposHistoricoAlteracoes.ID.getNomeCampo(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosHistoricoAlteracoesDTO.idAdminFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposHistoricoAlteracoes.ID_ADMIN.getNomeCampo(),
            IGUAL,
            AND,
            filtrosHistoricoAlteracoesDTO.formatarIdAdmin(),
            Types.BIGINT);
      }

      if (filtrosHistoricoAlteracoesDTO.idRegistroFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposHistoricoAlteracoes.ID_REGISTRO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosHistoricoAlteracoesDTO.formatarIdRegistro(),
            Types.BIGINT);
      }

      if (filtrosHistoricoAlteracoesDTO.dataAlteracaoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposHistoricoAlteracoes.DATA_ALTERACAO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosHistoricoAlteracoesDTO.formatarDataAlteracao(),
            Types.DATE);
      }

      if (filtrosHistoricoAlteracoesDTO.acaoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposHistoricoAlteracoes.ACAO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosHistoricoAlteracoesDTO.formatarAcao(),
            Types.VARCHAR);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosHistoricoAlteracoesDTO.joinFiltro());
      if (filtrosHistoricoAlteracoesDTO.joinFiltro() != null) {
        String conector =
            JoinsPossiveis.joinValido(
                "log_acessos_admin", filtrosHistoricoAlteracoesDTO.joinFiltro());

        if (conector.contains("log_acessos_admin")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> historicosAlteracao =
          dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosHistoricoAlteracoesDTO.filtroRapido() != null
          && !filtrosHistoricoAlteracoesDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosHistoricoAlteracoesDTO.filtroRapido();

        historicosAlteracao.removeIf(historicoAlteracao ->
            historicoAlteracao.values().stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .noneMatch(valorTexto -> valorTexto.contains(termoFiltro)));
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(historicosAlteracao, admin);

      return historicosAlteracao;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarLogAcessoAdminSelect(
      FiltrosHistoricoAlteracoesDTO filtrosHistoricoAlteracoesDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosHistoricoAlteracoesDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosHistoricoAlteracoesDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosHistoricoAlteracoesDTO.idAdminFiltro() != null) {
      GenericExceptionEnum validacao =
          validarIdAdmin(filtrosHistoricoAlteracoesDTO.idAdminFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosHistoricoAlteracoesDTO.idRegistroFiltro() != null) {
      GenericExceptionEnum validacao =
          validarIdRegistro(filtrosHistoricoAlteracoesDTO.idAdminFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosHistoricoAlteracoesDTO.dataAlteracaoFiltro() != null) {
      GenericExceptionEnum validacao =
          validarDataAlteracao(filtrosHistoricoAlteracoesDTO.dataAlteracaoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosHistoricoAlteracoesDTO.acaoFiltro() != null) {
      GenericExceptionEnum validacao = validarAcao(filtrosHistoricoAlteracoesDTO.acaoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosHistoricoAlteracoesDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("log_acessos_admin", filtrosHistoricoAlteracoesDTO.joinFiltro())
          == null) {
        erros.add(JOIN_INVALIDO);
      }
    }
    return erros;
  }
}
