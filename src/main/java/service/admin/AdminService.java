package service.admin;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosDadosAdmin.EM_ATIVIDADE_INVALIDO;
import static exception.ErrosDadosAdmin.FUNCAO_INVALIDA;
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
import dao.AdminDAO;
import dao.ConstraintsBanco;
import dao.CriarInstrucaoDinamica;
import exception.ErrosDadosAdmin;
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
import service.JoinsPossiveis;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import util.Anonimizador;

public final class AdminService {

  // Constante usada para operações json no código
  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  //Constantes utilizadas pelo código
  private static final int TAMANHO_MINIMO_SENHA = 8;
  private static final int TAMANHO_MAXIMO_SENHA = 60;
  private static final Pattern PATTERN_SENHA =
      Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");

  private static final Pattern PATTERN_EMAIL =
      Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
  private static final int TAMANHO_MAXIMO_EMAIL = 256;

  private static final int TAMANHO_MAXIMO_NOME = 150;
  private static final Pattern PATTERN_PESSOA_FISICA =
      Pattern.compile("^[\\p{Script=Latin}']+[\\p{Script=Latin}'\\s\\-]+$");

  // Validações

  // Validações do email
  private static GenericExceptionEnum validarEmail(String email) {
    if (email == null || email.isBlank()) {
      return ErrosGeraisDados.EMAIL_VAZIO;
    }

    String emailTratado = email.toLowerCase().strip();
    if (emailTratado.length() > TAMANHO_MAXIMO_EMAIL) {
      return ErrosGeraisDados.EMAIL_TAMANHO_INVALIDO;
    }
    return validarFormatoEmail(emailTratado) != VALIDACAO_OK
        ? ErrosGeraisDados.EMAIL_INVALIDO
        : VALIDACAO_OK;
  }

  private static GenericExceptionEnum validarFormatoEmail(String email) {
    return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : ErrosGeraisDados.EMAIL_INVALIDO;
  }

  // Validações da senha
  private static GenericExceptionEnum validarSenha(String senha) {
    if (senha == null || senha.isBlank()) {
      return ErrosGeraisDados.SENHA_VAZIA;
    }

    String senhaTrim = senha.strip();
    if (senhaTrim.length() < TAMANHO_MINIMO_SENHA) {
      return ErrosGeraisDados.SENHA_MENOR_QUE_OITO;
    }

    if (senhaTrim.length() > TAMANHO_MAXIMO_SENHA) {
      return ErrosGeraisDados.SENHA_TAMANHO_INVALIDO;
    }

    return validarFormatoSenha(senhaTrim);
  }

  private static GenericExceptionEnum validarFormatoSenha(String senha) {
    return PATTERN_SENHA.matcher(senha).matches() ? VALIDACAO_OK : ErrosGeraisDados.SENHA_FRACA;
  }

  // Validações do nome
  private static GenericExceptionEnum validarNome(String nome) {
    if (nome == null || nome.isBlank()) {
      return ErrosDadosAdmin.NOME_VAZIO;
    }

    String nomeTrim = nome.strip();
    if (nomeTrim.length() > TAMANHO_MAXIMO_NOME) {
      return ErrosDadosAdmin.NOME_TAMANHO_INVALIDO;
    }

    if (nomeTrim.toLowerCase().replace(nomeTrim.charAt(0), ' ').isBlank()) {
      return ErrosDadosAdmin.NOME_INVALIDO;
    }
    return validarNomeRegex(nome);
  }

  private static GenericExceptionEnum validarNomeRegex(String nome) {
    return PATTERN_PESSOA_FISICA.matcher(nome).matches()
        ? VALIDACAO_OK
        : ErrosDadosAdmin.NOME_INVALIDO;
  }

  // Validação da função do admin
  private static GenericExceptionEnum validarFuncao(String funcao) {
    if (funcao == null || funcao.isBlank()) {
      return ErrosDadosAdmin.FUNCAO_INVALIDA;
    }

    if (FuncoesAdmin.descobrirFuncaoAdmin(funcao.strip()) == null) {
      return ErrosDadosAdmin.FUNCAO_INVALIDA;
    }
    return VALIDACAO_OK;
  }

  // Validação do em atividade
  private static GenericExceptionEnum validarEmAtividade(String ehMandante) {
    if (ehMandante == null || ehMandante.isBlank()) {
      return EM_ATIVIDADE_INVALIDO;
    }

    if ("true".equalsIgnoreCase(ehMandante.toLowerCase().trim())
        || "false".equalsIgnoreCase(ehMandante.toLowerCase().trim())) {
      return VALIDACAO_OK;
    }
    return EM_ATIVIDADE_INVALIDO;
  }

  // Validação para o login
  public static boolean validarDadosLogin(String email, String senha) {
    return validarEmail(email) == VALIDACAO_OK && validarSenha(senha) == VALIDACAO_OK;
  }

  // Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarAdmin(id, idAdmin);
  }

  private GenericExceptionEnum deletarAdmin(String id, Long idAdmin) {
    AdminDAO dao = new AdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);
      try {
        Long idConvertido = Long.parseLong(id.strip());

        if (idConvertido == idAdmin) {
          return ERRO_GENERICO;
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

        List<Map<String, Object>> admin = dao.readAll(criarInstrucaoDinamicaRead, connection);

        if (admin.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        Map<String, Object> adminMap = admin.getFirst();

        try{
          if(((String) adminMap.get("funcao")).equalsIgnoreCase("SUPERADMIN")){
            return ERRO_GENERICO;
          }
        }catch (ClassCastException classCastException){
          return ERRO_GENERICO;
        }


        CriarInstrucaoDinamica criarInstrucaoDinamicaDelete = new CriarInstrucaoDinamica();
        try {
          if ((Boolean) adminMap.get("em_atividade")) {
            criarInstrucaoDinamicaDelete.setCampo(
                CamposAdmin.EM_ATIVIDADE.getNomeCampo(), false, Types.BOOLEAN);
          } else {
            criarInstrucaoDinamicaDelete.setCampo(
                CamposAdmin.EM_ATIVIDADE.getNomeCampo(), true, Types.BOOLEAN);
          }
        } catch (ClassCastException classCastException) {
          System.out.println(
              "O admin de ID:" + adminMap.get("id") + " possui um erro no registro ");
          throw new IllegalArgumentException("Erro no registro do admin");
        }

        criarInstrucaoDinamicaDelete.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

        dao.update(criarInstrucaoDinamicaDelete, connection);

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                idConvertido,
                "ADMIN",
                "UPDATE",
                GSON.toJsonTree(admin.getFirst()).getAsJsonObject(),
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
    AdminDAO dao = new AdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      List<Map<String, Object>> admins = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(admins, admin);

      return admins;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosAdminDTO filtrosAdminDTO, List<GenericExceptionEnum> errosEncontrados, Admin admin) {
    if (filtrosAdminDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarAdminSelect(filtrosAdminDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerAdmin(filtrosAdminDTO, admin);
  }

  private List<Map<String, Object>> lerAdmin(FiltrosAdminDTO filtrosAdminDTO, Admin admin) {
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      AdminDAO dao = new AdminDAO();

      if (filtrosAdminDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposAdmin.ID.getNomeCampo(),
            IGUAL,
            VAZIO,
            filtrosAdminDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposAdmin.ID.getNomeCampo(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosAdminDTO.emailFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposAdmin.EMAIL.getNomeCampo(),
            ILIKE,
            AND,
            filtrosAdminDTO.formatarEmail(),
            Types.VARCHAR);
      }

      if (filtrosAdminDTO.nomeFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposAdmin.NOME.getNomeCampo(),
            ILIKE,
            AND,
            filtrosAdminDTO.formatarNome(),
            Types.VARCHAR);
      }

      if (filtrosAdminDTO.funcaoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposAdmin.FUNCAO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosAdminDTO.formatarFuncao(),
            Types.VARCHAR);
      }

      if (filtrosAdminDTO.emAtividadeFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposAdmin.EM_ATIVIDADE.getNomeCampo(),
            IGUAL,
            AND,
            filtrosAdminDTO.formatarEmAtividade(),
            Types.BOOLEAN);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosAdminDTO.joinFiltro());
      if (filtrosAdminDTO.joinFiltro() != null) {
        String conector = JoinsPossiveis.joinValido("admin", filtrosAdminDTO.joinFiltro());

        if (conector.contains("admin")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> admins = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosAdminDTO.filtroRapido() != null && !filtrosAdminDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosAdminDTO.filtroRapido();

        admins.removeIf(
            adminTemp ->
                adminTemp.values().stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .noneMatch(valorTexto -> valorTexto.contains(termoFiltro)));
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(admins, admin);

      return admins;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarAdminSelect(FiltrosAdminDTO filtrosAdminDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosAdminDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosAdminDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosAdminDTO.nomeFiltro() != null) {
      GenericExceptionEnum validacao = validarNome(filtrosAdminDTO.nomeFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosAdminDTO.funcaoFiltro() != null) {
      GenericExceptionEnum validacao = validarFuncao(filtrosAdminDTO.funcaoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosAdminDTO.emAtividadeFiltro() != null) {
      GenericExceptionEnum validacao = validarEmAtividade(filtrosAdminDTO.emAtividadeFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosAdminDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("admin", filtrosAdminDTO.joinFiltro()) == null) {
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

    GenericExceptionEnum resultado = atualizarAdmin(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarAdmin(JsonObject jsonObject, Long idAdmin) {
    AdminDAO dao = new AdminDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        Admin admin = GSON.fromJson(jsonObject, AdminParaInsertDTO.class).construirAdmin();
        Long id = admin.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> adminLido = dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (adminLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        Map<String, Object> adminMap = adminLido.getFirst();
        try {
          if (((String) adminMap.get("funcao")).equalsIgnoreCase("SUPERADMIN")) {
            return ERRO_GENERICO;
          }
        } catch (ClassCastException classCastException) {
          System.out.println(
              "O admin de ID:" + adminMap.get("id") + " possui um erro no registro ");
          throw new IllegalArgumentException("Erro no registro do admin");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposAdmin.EMAIL.getNomeCampo(), admin.getEmail(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposAdmin.SENHA.getNomeCampo(), admin.getSenha(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposAdmin.NOME.getNomeCampo(), admin.getNome(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposAdmin.EM_ATIVIDADE.getNomeCampo(), admin.isEmAtividade(), Types.BOOLEAN);

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
                "ADMIN",
                "UPDATE",
                GSON.toJsonTree(adminLido.getFirst()).getAsJsonObject(),
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

    AdminParaUpdateDTO adminParaUpdateDTO = GSON.fromJson(jsonObject, AdminParaUpdateDTO.class);

    GenericExceptionEnum emailValidacao = validarEmail(adminParaUpdateDTO.emailUpdate());
    if (emailValidacao != VALIDACAO_OK
        && (adminParaUpdateDTO.emailUpdate() != null
            && !adminParaUpdateDTO.emailUpdate().isBlank())) {
      erros.add(emailValidacao);
    }

    GenericExceptionEnum senhaValidacao = validarSenha(adminParaUpdateDTO.senhaUpdate());
    if (senhaValidacao != VALIDACAO_OK
        && (adminParaUpdateDTO.senhaUpdate() != null
            && !adminParaUpdateDTO.senhaUpdate().isBlank())) {
      erros.add(senhaValidacao);
    }

    GenericExceptionEnum nomeValidacao = validarNome(adminParaUpdateDTO.nomeUpdate());
    if (nomeValidacao != VALIDACAO_OK
        && (adminParaUpdateDTO.nomeUpdate() != null
            && !adminParaUpdateDTO.nomeUpdate().isBlank())) {
      erros.add(nomeValidacao);
    }

    GenericExceptionEnum emAtividadeValidacao =
        validarEmAtividade(adminParaUpdateDTO.emAtividadeUpdate());
    if (emAtividadeValidacao != VALIDACAO_OK
        && (adminParaUpdateDTO.emAtividadeUpdate() != null
            && !adminParaUpdateDTO.emAtividadeUpdate().isBlank())) {
      erros.add(emAtividadeValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirAdminParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK || admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposAdmin.ID.getNomeCampo(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      AdminDAO dao = new AdminDAO();

      List<Map<String, Object>> dados = dao.readAll(criarInstrucaoDinamica, connection);

      for(Map<String, Object> dado : dados){
        dado.remove("senha");
      }
      return dados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  // Métodos relacionados ao insert
  public List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> mensagens = validarAdminInsert(jsonObject);
    if (mensagens.isEmpty()) {
      GenericExceptionEnum resultado = persistirAdmin(jsonObject, idAdmin);
      if (resultado != SUCESSO) {
        mensagens.add(resultado);
      }
    }
    return mensagens;
  }

  private List<GenericExceptionEnum> validarAdminInsert(JsonObject jsonObject) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    AdminParaInsertDTO adminParaInsertDTO = GSON.fromJson(jsonObject, AdminParaInsertDTO.class);

    GenericExceptionEnum validacaoEmail = validarEmail(adminParaInsertDTO.email());
    if (validacaoEmail != VALIDACAO_OK) {
      listaDeErros.add(validacaoEmail);
    }

    GenericExceptionEnum validacaoSenha = validarSenha(adminParaInsertDTO.senha());
    if (validacaoSenha != VALIDACAO_OK) {
      listaDeErros.add(validacaoSenha);
    }

    GenericExceptionEnum validacaoNome = validarNome(adminParaInsertDTO.nome());
    if (validacaoNome != VALIDACAO_OK) {
      listaDeErros.add(validacaoNome);
    }

    GenericExceptionEnum validacaoFuncao = validarFuncao(adminParaInsertDTO.funcao());
    if (validacaoFuncao != VALIDACAO_OK) {
      listaDeErros.add(validacaoFuncao);
    }

    GenericExceptionEnum validacaoEmAtividade =
        validarEmAtividade(adminParaInsertDTO.emAtividade());
    if (validacaoEmAtividade != VALIDACAO_OK) {
      listaDeErros.add(validacaoEmAtividade);
    }
    return listaDeErros;
  }

  private GenericExceptionEnum persistirAdmin(JsonObject jsonObject, Long idAdmin) {
    AdminDAO dao = new AdminDAO();
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    Admin admin = GSON.fromJson(jsonObject, AdminParaInsertDTO.class).construirAdmin();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {
        if(admin.getFuncao() == FuncoesAdmin.SUPERADMIN){
          return FUNCAO_INVALIDA;
        }

        criarInstrucaoDinamica.setCampo(
            CamposAdmin.EMAIL.getNomeCampo(), admin.getEmail(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposAdmin.SENHA.getNomeCampo(), admin.getSenha(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposAdmin.FUNCAO.getNomeCampo(), admin.getFuncao().getFuncao(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposAdmin.NOME.getNomeCampo(), admin.getNome(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposAdmin.EM_ATIVIDADE.getNomeCampo(), admin.isEmAtividade(), Types.BOOLEAN);

        Long idRegistro = dao.insert(criarInstrucaoDinamica, connection);

        if (idRegistro == null) {
          throw new IllegalArgumentException("Ocorreu algum erro e o admin não foi registrado!");
        }

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null, idAdmin, idRegistro, "ADMIN", "INSERT", new JsonObject(), jsonObject, null),
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
