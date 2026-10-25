package service.logAcessosAdmin;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosDadosLogAcessosAdmin.DATA_INVALIDA;
import static exception.ErrosDadosLogAcessosAdmin.NAVEGADOR_INVALIDO;
import static exception.ErrosDadosLogAcessosAdmin.SISTEMA_OPERACIONAL_INVALIDO;
import static exception.ErrosGerais.ADMIN_INATIVO;
import static exception.ErrosGerais.SENHA_INVALIDA;
import static exception.ErrosGeraisDados.JOIN_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static service.ValidacoesComunsService.validarId;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.ENDERECO_IP;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.ID_ADMIN;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.MOTIVO_FALHA;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.NAVEGADOR;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.SISTEMA_OPERACIONAL;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.SUCESSO;

import conexao.Conexao;
import dao.AcoesInstrucao;
import dao.CriarInstrucaoDinamica;
import dao.LogAcessosAdminDAO;
import exception.ErrosDadosTelefoneAdmin;
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
import model.NavegadoresAdmin;
import model.SistemasOperacionaisAdmin;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import util.Anonimizador;

public final class LogAcessosAdminService {

  //Constante utilizada pelo código
  private static final int INET = -1000;

  //Métodos para o login
  public List<GenericExceptionEnum> validarDadosNoLogin(String userAgent) {
    List<GenericExceptionEnum> erros = new ArrayList<>();
      if (NavegadoresAdmin.descobrirNavegadorAdmin(userAgent)
          == NavegadoresAdmin.NAVEGADOR_INVALIDO) {
          erros.add(NAVEGADOR_INVALIDO);
      }

      if (SistemasOperacionaisAdmin.descobrirSistemaOperacionalAdmin(userAgent)
          == SistemasOperacionaisAdmin.SISTEMA_OPERACIONAL_INVALIDO) {
          erros.add(SISTEMA_OPERACIONAL_INVALIDO);
      }

    return erros;
  }

  public void salvarAcessoDoAdmin(List<GenericExceptionEnum> erros, String userAgent, String ip,
      Long idAdmin, Connection connection) throws SQLException {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

    StringBuilder mensagemErro = new StringBuilder();

    if (idAdmin == null) {
      return;
    }

    criarInstrucaoDinamica.setCampo(ID_ADMIN.getNomeCampo(), idAdmin, Types.BIGINT);

    if (erros.contains(NAVEGADOR_INVALIDO)) {
      criarInstrucaoDinamica.setCampo(NAVEGADOR.getNomeCampo(),
          NavegadoresAdmin.NAVEGADOR_INVALIDO.getNavegador(), Types.VARCHAR);
      mensagemErro.append("Navegador inválido");
    } else {
      criarInstrucaoDinamica.setCampo(NAVEGADOR.getNomeCampo(),
          NavegadoresAdmin.descobrirNavegadorAdmin(userAgent).getNavegador(), Types.VARCHAR);
    }

    if (erros.contains(SISTEMA_OPERACIONAL_INVALIDO)) {
      criarInstrucaoDinamica.setCampo(SISTEMA_OPERACIONAL.getNomeCampo(),
          SistemasOperacionaisAdmin.SISTEMA_OPERACIONAL_INVALIDO.getSistemaOperacional(),
          Types.VARCHAR);
      if (mensagemErro.isEmpty()) {
        mensagemErro.append("Sistema operacional inválido");
      } else {
        mensagemErro.append("e sistema operacional inválido");
      }
    } else {
      criarInstrucaoDinamica.setCampo(SISTEMA_OPERACIONAL.getNomeCampo(),
          SistemasOperacionaisAdmin.descobrirSistemaOperacionalAdmin(userAgent)
              .getSistemaOperacional(),
          Types.VARCHAR);
    }

    if (erros.contains(ADMIN_INATIVO)) {
      if (mensagemErro.isEmpty()) {
        mensagemErro.append("A conta do administrator está desativada");
      } else {
        mensagemErro.append("e a conta do administrator está desativada");
      }
    }

    if (erros.contains(SENHA_INVALIDA)) {
      if (mensagemErro.isEmpty()) {
        mensagemErro.append("Senha inválida");
      } else {
        mensagemErro.append("e sistema operacional inválido");
      }
    }

    criarInstrucaoDinamica.setCampo(ENDERECO_IP.getNomeCampo(), ip, INET);

    if (erros.isEmpty()) {
      criarInstrucaoDinamica.setCampo(SUCESSO.getNomeCampo(), true, Types.BOOLEAN);
      criarInstrucaoDinamica.setCampo(MOTIVO_FALHA.getNomeCampo(), null, Types.VARCHAR);
    } else {
      criarInstrucaoDinamica.setCampo(SUCESSO.getNomeCampo(), false, Types.BOOLEAN);
      criarInstrucaoDinamica.setCampo(MOTIVO_FALHA.getNomeCampo(),
          mensagemErro.toString(), Types.VARCHAR);
    }

    LogAcessosAdminDAO logAcessosAdminDAO = new LogAcessosAdminDAO();

    logAcessosAdminDAO.insert(criarInstrucaoDinamica, connection);
  }

  // Validações

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

  //Validação da data de acesso
  private static GenericExceptionEnum validarDataAcesso(String dataAcesso){
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try{
      LocalDate data = LocalDate.parse(dataAcesso.strip(), formatador);

      return VALIDACAO_OK;
    }catch (DateTimeParseException dateTimeParseException){
      return DATA_INVALIDA;
    }
  }

  //Validação do navegador
  private static GenericExceptionEnum validarNavegador(String navegador){
    NavegadoresAdmin navegadorConertido;
    try {
      navegadorConertido = NavegadoresAdmin.valueOf(navegador.toUpperCase().strip());
    } catch (IllegalArgumentException e) {
      navegadorConertido = null;
    }
    return navegadorConertido == null ? null : NAVEGADOR_INVALIDO;
  }

  //Validação do sistema operacional
  private static GenericExceptionEnum validarSistemaOperacional(String sistemaOperacional){
    SistemasOperacionaisAdmin sistemasOperacionaisAdmin;
    try {
      sistemasOperacionaisAdmin = SistemasOperacionaisAdmin.valueOf(sistemaOperacional.toUpperCase().strip());
    } catch (IllegalArgumentException e) {
      sistemasOperacionaisAdmin = null;
    }
    return sistemasOperacionaisAdmin == null ? null : SISTEMA_OPERACIONAL_INVALIDO;
  }

  // Métodos relacionados ao select
  public List<Map<String, Object>> realizarSelectInicial(Admin admin) {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    LogAcessosAdminDAO dao = new LogAcessosAdminDAO();

    try (Connection connection = Conexao.getConnection()) {
      List<Map<String, Object>> acessosAdmin = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(acessosAdmin, admin);

      return acessosAdmin;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosLogAcessosAdminDTO filtrosLogAcessosAdminDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosLogAcessosAdminDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarLogAcessoAdminSelect(filtrosLogAcessosAdminDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerLogAcessoAdmin(filtrosLogAcessosAdminDTO, admin);
  }

  private List<Map<String, Object>> lerLogAcessoAdmin(
      FiltrosLogAcessosAdminDTO filtrosLogAcessosAdminDTO, Admin admin) {
    try (Connection connection = Conexao.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      LogAcessosAdminDAO dao = new LogAcessosAdminDAO();

      if (filtrosLogAcessosAdminDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposLogAcessosAdmin.ID.getNomeCampo(),
            IGUAL,
            VAZIO,
            filtrosLogAcessosAdminDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposLogAcessosAdmin.ID.getNomeCampo(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosLogAcessosAdminDTO.idAdminFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            ID_ADMIN.getNomeCampo(),
            IGUAL,
            AND,
            filtrosLogAcessosAdminDTO.formatarIdAdmin(),
            Types.BIGINT);
      }

      if (filtrosLogAcessosAdminDTO.dataAcessoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposLogAcessosAdmin.DATA_ACESSO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosLogAcessosAdminDTO.formatarDataAcesso(),
            Types.DATE);
      }

      if (filtrosLogAcessosAdminDTO.navegadorFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            NAVEGADOR.getNomeCampo(),
            IGUAL,
            AND,
            filtrosLogAcessosAdminDTO.formatarNavegador(),
            Types.VARCHAR);
      }

        if (filtrosLogAcessosAdminDTO.sistemaOperacionalFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            SISTEMA_OPERACIONAL.getNomeCampo(),
            IGUAL,
            AND,
            filtrosLogAcessosAdminDTO.formatarSistemaOperacional(),
            Types.VARCHAR);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosLogAcessosAdminDTO.joinFiltro());
      if (filtrosLogAcessosAdminDTO.joinFiltro() != null) {
        String conector = JoinsPossiveis.joinValido("log_acessos_admin", filtrosLogAcessosAdminDTO.joinFiltro());

        if (conector.contains("log_acessos_admin")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> acessosAdmin = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosLogAcessosAdminDTO.filtroRapido() != null
          && !filtrosLogAcessosAdminDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosLogAcessosAdminDTO.filtroRapido();

        acessosAdmin.removeIf(
            acessoAdmin ->
                acessoAdmin.values().stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .noneMatch(valorTexto -> valorTexto.contains(termoFiltro)));
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(acessosAdmin, admin);

      return acessosAdmin;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarLogAcessoAdminSelect(
      FiltrosLogAcessosAdminDTO filtrosLogAcessosAdminDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosLogAcessosAdminDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosLogAcessosAdminDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosLogAcessosAdminDTO.idAdminFiltro() != null) {
      GenericExceptionEnum validacao = validarIdAdmin(filtrosLogAcessosAdminDTO.idAdminFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosLogAcessosAdminDTO.dataAcessoFiltro() != null) {
      GenericExceptionEnum validacao = validarDataAcesso(filtrosLogAcessosAdminDTO.dataAcessoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosLogAcessosAdminDTO.navegadorFiltro() != null) {
      GenericExceptionEnum validacao = validarNavegador(filtrosLogAcessosAdminDTO.navegadorFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosLogAcessosAdminDTO.sistemaOperacionalFiltro() != null) {
      GenericExceptionEnum validacao = validarSistemaOperacional(filtrosLogAcessosAdminDTO.sistemaOperacionalFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosLogAcessosAdminDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("log_acessos_admin", filtrosLogAcessosAdminDTO.joinFiltro()) == null) {
        erros.add(JOIN_INVALIDO);
      }
    }
    return erros;
  }

}
