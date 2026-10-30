package service.endereco;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosDadosEndereco.COMPLEMENTO_INVALIDO;
import static exception.ErrosDadosEndereco.ESTADO_VAZIO;
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
import dao.EnderecoDAO;
import exception.ErrosDadosEndereco;
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
import model.Endereco;
import model.EstadosBrasileiros;
import model.FuncoesAdmin;
import model.HistoricoAlteracoesAdmin;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import util.Anonimizador;

public final class EnderecoService {

  // Constante usada para operações json no código
  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  // Constantes utilizadas pelo código
  private static final Pattern PATTERN_CIDADE =
      Pattern.compile("^[\\p{Script=Latin}\\u0027\\-\\s]+$");

  private static final Pattern PATTERN_BAIRRO =
      Pattern.compile("^[\\p{Script=Latin}0-9.\\u0027\\-\\s]+$");

  private static final Pattern PATTERN_CEP = Pattern.compile("^\\d{8}$");

  private static final Pattern PATTERN_LOGRADOURO =
      Pattern.compile("^[\\p{Script=Latin}0-9,.\\-/ªº\\s]+$");

  private static final Pattern PATTERN_COMPLEMENTO =
      Pattern.compile("^[\\p{Script=Latin}0-9,.\\-/ªº\\s]+$");

  private static final Pattern PATTERN_NUMERO = Pattern.compile("^\\d+$");

  private static final String SEM_NUMERO = "S/N";

  // Validações

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

  // Validação do estado
  public static GenericExceptionEnum validarEstado(String estado) {
    if (estado == null || estado.isBlank()) {
      return ESTADO_VAZIO;
    }

    if (EstadosBrasileiros.descobrirEstadoBrasileiroPorSigla(estado) == null) {
      return ESTADO_VAZIO;
    }
    return VALIDACAO_OK;
  }

  // Validações da cidade
  private static GenericExceptionEnum validarCidade(String cidade) {
    if (cidade == null || cidade.isBlank()) {
      return ErrosDadosEndereco.CIDADE_VAZIA;
    }

    String cidadeTratada = cidade.strip();
    return validarFormatoCidade(cidadeTratada);
  }

  private static GenericExceptionEnum validarFormatoCidade(String cidade) {
    return PATTERN_CIDADE.matcher(cidade).matches()
        ? VALIDACAO_OK
        : ErrosDadosEndereco.CIDADE_INVALIDA;
  }

  // Validações do bairro
  private static GenericExceptionEnum validarBairro(String bairro) {
    if (bairro == null || bairro.isBlank()) {
      return ErrosDadosEndereco.BAIRRO_VAZIO;
    }

    String bairroTratada = bairro.strip();
    return validarFormatoBairro(bairroTratada);
  }

  private static GenericExceptionEnum validarFormatoBairro(String bairro) {
    return PATTERN_BAIRRO.matcher(bairro).matches()
        ? VALIDACAO_OK
        : ErrosDadosEndereco.BAIRRO_INVALIDO;
  }

  // Validações do CEP
  private static GenericExceptionEnum validarCep(String cep) {
    if (cep == null || cep.isBlank()) {
      return ErrosDadosEndereco.CEP_VAZIO;
    }

    String cepTratada = cep.strip().replace("-", "");
    return validarFormatoCep(cepTratada);
  }

  private static GenericExceptionEnum validarFormatoCep(String cep) {
    return PATTERN_CEP.matcher(cep).matches() ? VALIDACAO_OK : ErrosDadosEndereco.CEP_INVALIDO;
  }

  // Validações do logradouro
  private static GenericExceptionEnum validarLogradouro(String logradouro) {
    if (logradouro == null || logradouro.isBlank()) {
      return ErrosDadosEndereco.LOGRADOURO_VAZIO;
    }

    String logradouroTratada = logradouro.strip();
    return validarFormatoLogradouro(logradouroTratada);
  }

  private static GenericExceptionEnum validarFormatoLogradouro(String logradouro) {
    return PATTERN_LOGRADOURO.matcher(logradouro).matches()
        ? VALIDACAO_OK
        : ErrosDadosEndereco.LOGRADOURO_INVALIDO;
  }

  // Validações do número
  private static GenericExceptionEnum validarNumero(String numero) {
    if (numero == null || numero.isBlank()) {
      return ErrosDadosEndereco.NUMERO_VAZIO;
    }

    String numeroTratada = numero.strip();
    return validarFormatoNumero(numeroTratada);
  }

  private static GenericExceptionEnum validarFormatoNumero(String numero) {
    return PATTERN_NUMERO.matcher(numero).matches() || numero.equalsIgnoreCase(SEM_NUMERO)
        ? VALIDACAO_OK
        : ErrosDadosEndereco.NUMERO_INVALIDO;
  }

  // Validações do complemento
  private static GenericExceptionEnum validarComplemento(String complemento) {
    if (complemento == null || complemento.isBlank()) {
      return VALIDACAO_OK;
    }
    return validarFormatoComplemento(complemento);
  }

  private static GenericExceptionEnum validarFormatoComplemento(String complemento) {
    return PATTERN_COMPLEMENTO.matcher(complemento).matches() ? VALIDACAO_OK : COMPLEMENTO_INVALIDO;
  }

  // Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarEndereco(id, idAdmin);
  }

  private GenericExceptionEnum deletarEndereco(String id, Long idAdmin) {
    EnderecoDAO dao = new EnderecoDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);
      try {
        Long idConvertido = Long.parseLong(id.strip());

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

        List<Map<String, Object>> endereco = dao.readAll(criarInstrucaoDinamicaRead, connection);

        if (endereco.isEmpty()) {
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
                "ENDERECO",
                "DELETE",
                GSON.toJsonTree(endereco.getFirst()).getAsJsonObject(),
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
    EnderecoDAO dao = new EnderecoDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      List<Map<String, Object>> enderecos = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(enderecos, admin);

      return enderecos;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosEnderecoDTO filtrosEnderecoDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosEnderecoDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarEnderecoSelect(filtrosEnderecoDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerEnderecos(filtrosEnderecoDTO, admin);
  }

  private List<Map<String, Object>> lerEnderecos(
      FiltrosEnderecoDTO filtrosEnderecoDTO, Admin admin) {
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      EnderecoDAO dao = new EnderecoDAO();

      if (filtrosEnderecoDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEndereco.ID.getNomeCampo(),
            IGUAL,
            VAZIO,
            filtrosEnderecoDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposEndereco.ID.getNomeCampo(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosEnderecoDTO.idUsuarioFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEndereco.ID_USUARIO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosEnderecoDTO.formatarIdUsuario(),
            Types.BIGINT);
      }

      if (filtrosEnderecoDTO.estadoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEndereco.ESTADO.getNomeCampo(),
            IGUAL,
            AND,
            filtrosEnderecoDTO.formatarEstado(),
            Types.VARCHAR);
      }

      if (filtrosEnderecoDTO.cidadeFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEndereco.CIDADE.getNomeCampo(),
            ILIKE,
            AND,
            filtrosEnderecoDTO.formatarCidade(),
            Types.VARCHAR);
      }

      if (filtrosEnderecoDTO.bairroFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEndereco.BAIRRO.getNomeCampo(),
            ILIKE,
            AND,
            filtrosEnderecoDTO.formatarBairro(),
            Types.VARCHAR);
      }

      if (filtrosEnderecoDTO.logradouroFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEndereco.LOGRADOURO.getNomeCampo(),
            ILIKE,
            AND,
            filtrosEnderecoDTO.formatarLogradouro(),
            Types.VARCHAR);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosEnderecoDTO.joinFiltro());
      if (filtrosEnderecoDTO.joinFiltro() != null) {
        String conector = JoinsPossiveis.joinValido("endereco", filtrosEnderecoDTO.joinFiltro());

        if (conector.contains("endereco")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> enderecos = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosEnderecoDTO.filtroRapido() != null
          && !filtrosEnderecoDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosEnderecoDTO.filtroRapido();

        enderecos.removeIf(
            endereco ->
                endereco.values().stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .noneMatch(valorTexto -> valorTexto.contains(termoFiltro)));
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(enderecos, admin);

      return enderecos;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarEnderecoSelect(FiltrosEnderecoDTO filtrosEnderecoDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosEnderecoDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosEnderecoDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.idUsuarioFiltro() != null) {
      GenericExceptionEnum validacao = validarIdUsuario(filtrosEnderecoDTO.idUsuarioFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.estadoFiltro() != null) {
      GenericExceptionEnum validacao = validarEstado(filtrosEnderecoDTO.estadoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.cidadeFiltro() != null) {
      GenericExceptionEnum validacao = validarCidade(filtrosEnderecoDTO.cidadeFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.bairroFiltro() != null) {
      GenericExceptionEnum validacao = validarBairro(filtrosEnderecoDTO.bairroFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.cepFiltro() != null) {
      GenericExceptionEnum validacao = validarCep(filtrosEnderecoDTO.cepFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.logradouroFiltro() != null) {
      GenericExceptionEnum validacao = validarLogradouro(filtrosEnderecoDTO.logradouroFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEnderecoDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("endereco", filtrosEnderecoDTO.joinFiltro()) == null) {
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

    GenericExceptionEnum resultado = atualizarEndereco(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarEndereco(JsonObject jsonObject, Long idAdmin) {
    EnderecoDAO dao = new EnderecoDAO();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        Endereco endereco =
            GSON.fromJson(jsonObject, EnderecoParaUpdateDTO.class).construirEndereco();
        Long id = endereco.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> enderecoLido =
            dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (enderecoLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.ESTADO.getNomeCampo(), endereco.getEstado().getSiglaEstado(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.CIDADE.getNomeCampo(), endereco.getCidade(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.BAIRRO.getNomeCampo(), endereco.getBairro(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.CEP.getNomeCampo(), endereco.getCep(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.LOGRADOURO.getNomeCampo(), endereco.getLogradouro(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.NUMERO.getNomeCampo(), endereco.getNumero(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEndereco.COMPLEMENTO.getNomeCampo(), endereco.getComplemento(), Types.VARCHAR);

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
                "ENDERECO",
                "UPDATE",
                GSON.toJsonTree(enderecoLido.getFirst()).getAsJsonObject(),
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

    EnderecoParaUpdateDTO enderecoParaUpdateDTO =
        GSON.fromJson(jsonObject, EnderecoParaUpdateDTO.class);

    GenericExceptionEnum estadoValidacao = validarEstado(enderecoParaUpdateDTO.estadoUpdate());
    if (estadoValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.estadoUpdate() != null
            && !enderecoParaUpdateDTO.estadoUpdate().isBlank())) {
      erros.add(estadoValidacao);
    }

    GenericExceptionEnum cidadeValidacao = validarCidade(enderecoParaUpdateDTO.cidadeUpdate());
    if (cidadeValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.cidadeUpdate() != null
            && !enderecoParaUpdateDTO.cidadeUpdate().isBlank())) {
      erros.add(cidadeValidacao);
    }

    GenericExceptionEnum bairroValidacao = validarBairro(enderecoParaUpdateDTO.bairroUpdate());
    if (bairroValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.bairroUpdate() != null
            && !enderecoParaUpdateDTO.bairroUpdate().isBlank())) {
      erros.add(bairroValidacao);
    }

    GenericExceptionEnum cepValidacao = validarCep(enderecoParaUpdateDTO.cepUpdate());
    if (cepValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.cepUpdate() != null
            && !enderecoParaUpdateDTO.cepUpdate().isBlank())) {
      erros.add(cepValidacao);
    }

    GenericExceptionEnum logradouroValidacao =
        validarLogradouro(enderecoParaUpdateDTO.logradouroUpdate());
    if (logradouroValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.logradouroUpdate() != null
            && !enderecoParaUpdateDTO.logradouroUpdate().isBlank())) {
      erros.add(logradouroValidacao);
    }

    GenericExceptionEnum numeroValidacao = validarNumero(enderecoParaUpdateDTO.numeroUpdate());
    if (numeroValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.numeroUpdate() != null
            && !enderecoParaUpdateDTO.numeroUpdate().isBlank())) {
      erros.add(numeroValidacao);
    }

    GenericExceptionEnum complementoValidacao =
        validarComplemento(enderecoParaUpdateDTO.complementoUpdate());
    if (complementoValidacao != VALIDACAO_OK
        && (enderecoParaUpdateDTO.complementoUpdate() != null
            && !enderecoParaUpdateDTO.complementoUpdate().isBlank())) {
      erros.add(complementoValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirEnderecoParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposEndereco.ID.getNomeCampo(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = ConexaoPostgresql.getConnection()) {
      EnderecoDAO dao = new EnderecoDAO();

      List<Map<String, Object>> resultados = dao.readAll(criarInstrucaoDinamica, connection);

      if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
        for (Map<String, Object> registro : resultados) {
          registro.remove("cep");
          registro.remove("logradouro");
        }
      }
      return resultados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  // Métodos relacionados ao insert
  public List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> mensagens = validarEnderecoInsert(jsonObject);
    if (mensagens.isEmpty()) {
      GenericExceptionEnum resultado = persistirEndereco(jsonObject, idAdmin);
      if (resultado != SUCESSO) {
        mensagens.add(resultado);
      }
    }
    return mensagens;
  }

  private List<GenericExceptionEnum> validarEnderecoInsert(JsonObject jsonObject) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    EnderecoParaInsertDTO enderecoParaInsertDTO =
        GSON.fromJson(jsonObject, EnderecoParaInsertDTO.class);

    GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(enderecoParaInsertDTO.idUsuario());
    if (validacaoIdUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoIdUsuario);
    }

    GenericExceptionEnum validacaoEstado = validarEstado(enderecoParaInsertDTO.estado());
    if (validacaoEstado != VALIDACAO_OK) {
      listaDeErros.add(validacaoEstado);
    }

    GenericExceptionEnum validacaoCidade = validarCidade(enderecoParaInsertDTO.cidade());
    if (validacaoCidade != VALIDACAO_OK) {
      listaDeErros.add(validacaoCidade);
    }

    GenericExceptionEnum validacaoBairro = validarBairro(enderecoParaInsertDTO.bairro());
    if (validacaoBairro != VALIDACAO_OK) {
      listaDeErros.add(validacaoBairro);
    }

    GenericExceptionEnum validacaoCep = validarCep(enderecoParaInsertDTO.cep());
    if (validacaoCep != VALIDACAO_OK) {
      listaDeErros.add(validacaoCep);
    }

    GenericExceptionEnum validacaoLogradouro =
        validarLogradouro(enderecoParaInsertDTO.logradouro());
    if (validacaoLogradouro != VALIDACAO_OK) {
      listaDeErros.add(validacaoLogradouro);
    }

    GenericExceptionEnum validacaoNumero = validarNumero(enderecoParaInsertDTO.numero());
    if (validacaoNumero != VALIDACAO_OK) {
      listaDeErros.add(validacaoNumero);
    }

    GenericExceptionEnum validacaoComplemento =
        validarComplemento(enderecoParaInsertDTO.complemento());
    if (validacaoComplemento != VALIDACAO_OK) {
      listaDeErros.add(validacaoComplemento);
    }
    return listaDeErros;
  }

  private GenericExceptionEnum persistirEndereco(JsonObject jsonObject, Long idAdmin) {
    EnderecoDAO dao = new EnderecoDAO();

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    Endereco endereco = GSON.fromJson(jsonObject, EnderecoParaInsertDTO.class).construirEndereco();

    try (Connection connection = ConexaoPostgresql.getConnection()) {
      connection.setAutoCommit(false);

      try {

        criarInstrucaoDinamica.setCampo(
            CamposEndereco.ID_USUARIO.getNomeCampo(), endereco.getIdUsuario(), Types.BIGINT);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.ESTADO.getNomeCampo(), endereco.getEstado().getSiglaEstado(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.CIDADE.getNomeCampo(), endereco.getCidade(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.BAIRRO.getNomeCampo(), endereco.getBairro(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.CEP.getNomeCampo(), endereco.getCep(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.LOGRADOURO.getNomeCampo(), endereco.getLogradouro(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.NUMERO.getNomeCampo(), endereco.getNumero(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(
            CamposEndereco.COMPLEMENTO.getNomeCampo(), endereco.getComplemento(), Types.VARCHAR);

        Long idRegistro = dao.insert(criarInstrucaoDinamica, connection);

        if (idRegistro == null) {
          throw new IllegalArgumentException("Ocorreu algum erro e o endereco não foi registrado!");
        }

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(
                null,
                idAdmin,
                idRegistro,
                "ENDERECO",
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
