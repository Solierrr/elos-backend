package service.empresaDemandante;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosDadosEmpresaDemandante.EH_MANDANTE_INVALIDO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGeraisDados.ATRIBUTO_NULL;
import static exception.ErrosGeraisDados.CNPJ_FORMATO_INVALIDO;
import static exception.ErrosGeraisDados.CNPJ_INVALIDO;
import static exception.ErrosGeraisDados.CNPJ_TAMANHO_INVALIDO;
import static exception.ErrosGeraisDados.CNPJ_VAZIO;
import static exception.ErrosGeraisDados.ID_USUARIO_INVALIDO;
import static exception.ErrosGeraisDados.JOIN_INVALIDO;
import static exception.ErrosGeraisDados.RAZAO_SOCIAL_INVALIDA;
import static exception.ErrosGeraisDados.RAZAO_SOCIAL_TAMANHO_INVALIDO;
import static exception.ErrosGeraisDados.RAZAO_SOCIAL_VAZIA;
import static exception.ErrosGeraisDados.TIPO_USUARIO_INCOMPATIVEL;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static service.ValidacoesComunsService.validarId;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import conexao.Conexao;
import dao.AcoesInstrucao;
import dao.ConstraintsBanco;
import dao.CriarInstrucaoDinamica;
import dao.EmpresaDemandanteDAO;
import dao.FornecedorDAO;
import exception.ErrosGerais;
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
import model.EmpresaDemandante;
import model.Fornecedor;
import model.FuncoesAdmin;
import model.HistoricoAlteracoesAdmin;
import model.TiposUsuario;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import service.fornecedor.CamposFornecedor;
import service.fornecedor.FornecedorParaUpdateDTO;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import service.usuario.CamposUsuario;
import util.Anonimizador;
import util.PorMascara;

public final class EmpresaDemandanteService {

    // Atributo usado para operações json no código
    private static final Gson GSON =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();


    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
  private static final int TAMANHO_CNPJ = 14;
  private static final Pattern PATTERN_CNPJ = Pattern.compile("^[0-9-A-Z]{12}[0-9]{2}$");

  private static final Pattern PATTERN_RAZAO_SOCIAL = Pattern.compile(
      "^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

  private static final int TAMANHO_MAXIMO_RAZAO_SOCIAL = 150;

  // Validação do id usuario
  private static GenericExceptionEnum validarIdUsuario(String idUsuario) {
    if (idUsuario == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idUsuario) != VALIDACAO_OK) {
      return ID_USUARIO_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  //Validação do tipo do usuario
  private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario) {
    TiposUsuario tiposUsuario = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
    if (tipoUsuario == null || tiposUsuario != TiposUsuario.EMPRESA_DEMANDANTE) {
      return TIPO_USUARIO_INCOMPATIVEL;
    }
    return VALIDACAO_OK;
  }

  //Validações do cnpj
  private static GenericExceptionEnum validarCnpj(String cnpj) {
    if (cnpj == null || cnpj.isBlank()) {
      return CNPJ_VAZIO;
    }

    String cnpjTratado = cnpj.strip().toUpperCase()
        .replaceAll(".", "")
        .replaceAll("-", "")
        .replaceAll("/", "");

    if (cnpjTratado.length() != TAMANHO_CNPJ) {
      return CNPJ_TAMANHO_INVALIDO;
    }

    if (cnpjTratado.replace(cnpjTratado.charAt(0), ' ').isBlank()) {
      return CNPJ_INVALIDO;
    }

    GenericExceptionEnum validacaoFormatoCnpj = validarFormatoCnpj(cnpjTratado);
    if (validacaoFormatoCnpj != VALIDACAO_OK) {
      return validacaoFormatoCnpj;
    }

    GenericExceptionEnum validacaoDigitosCnpj = validarDigitosCnpj(cnpjTratado);
    if (validacaoDigitosCnpj != VALIDACAO_OK) {
      return validacaoDigitosCnpj;
    }
    return VALIDACAO_OK;
  }

  private static GenericExceptionEnum validarFormatoCnpj(String cnpj) {
    return PATTERN_CNPJ.matcher(cnpj).matches() ? VALIDACAO_OK : CNPJ_FORMATO_INVALIDO;
  }

  private static GenericExceptionEnum validarDigitosCnpj(String cnpj) {
    int[] pesosDv1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    int[] pesosDv2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    String base12 = cnpj.substring(0, 12);
    String dvInformado = cnpj.substring(12, 14);

    int dv1 = calcularDigito(base12, pesosDv1);
    int dv2 = calcularDigito(base12 + dv1, pesosDv2);

    String dvCalculado = "" + dv1 + dv2;
    return dvCalculado.equals(dvInformado) ? VALIDACAO_OK : CNPJ_INVALIDO;
  }

  private static int calcularDigito(String base, int[] pesos) {
    int soma = 0;
    for (int i = 0; i < base.length(); i++) {
      int valor = base.charAt(i) - 48;
      soma += valor * pesos[i];
    }
    int resto = soma % 11;
    return (resto < 2) ? 0 : (11 - resto);
  }

  //Validações da razão social
  private static GenericExceptionEnum validarRazaoSocial(String razaoSocial) {
    if (razaoSocial == null || razaoSocial.isBlank()) {
      return RAZAO_SOCIAL_VAZIA;
    }

    String razaoSocialTratada = razaoSocial.strip();

    if (razaoSocialTratada.length() > TAMANHO_MAXIMO_RAZAO_SOCIAL) {
      return RAZAO_SOCIAL_TAMANHO_INVALIDO;
    }

    if (razaoSocialTratada.toLowerCase().replace(razaoSocialTratada.charAt(0), ' ').isBlank()) {
      return RAZAO_SOCIAL_INVALIDA;
    }

    return validarFormatoRazaoSocial(razaoSocialTratada);
  }

  private static GenericExceptionEnum validarFormatoRazaoSocial(String razaoSocial) {
    return PATTERN_RAZAO_SOCIAL.matcher(razaoSocial).matches() ? VALIDACAO_OK
        : RAZAO_SOCIAL_INVALIDA;
  }

  //Validação do eh mandante
  private static GenericExceptionEnum validarEhMandante(String ehMandante) {
    if (ehMandante == null || ehMandante.isBlank()) {
      return EH_MANDANTE_INVALIDO;
    }

    if ("true".equalsIgnoreCase(ehMandante.toLowerCase().trim()) || "false".equalsIgnoreCase(
        ehMandante.toLowerCase().trim())) {
      return VALIDACAO_OK;
    }
    return EH_MANDANTE_INVALIDO;
  }

    // Métodos relacionados ao delete
    public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
        if (validarId(id) != VALIDACAO_OK) {
            return ERRO_GENERICO;
        }
        return deletarFornecedor(id, idAdmin);
    }

    private GenericExceptionEnum deletarFornecedor(String id, Long idAdmin) {
        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();

        try (Connection connection = Conexao.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Long idConvertido = Long.parseLong(id.strip());

                CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();

                criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, idConvertido, Types.BIGINT);

                List<Map<String, Object>> usuario = dao.readAll(criarInstrucaoDinamicaRead, connection);

                if (usuario.isEmpty()) {
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
                        "EMPRESA_DEMANDANTE",
                        "DELETE",
                        GSON.toJsonTree(usuario.getFirst()).getAsJsonObject(),
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

  //Métodos relacionados ao select
  public List<Map<String, Object>> realizarSelectInicial(Admin admin) {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();

    try (Connection connection = Conexao.getConnection()) {
      List<Map<String, Object>> empresasDemandante = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(empresasDemandante, admin);

      for (Map<String, Object> dado : empresasDemandante) {
        dado.remove("tipo_usuario");
      }

      return empresasDemandante;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosEmpresaDemandanteDTO filtrosEmpresaDemandanteDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosEmpresaDemandanteDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarEmpresaDemananteSelect(filtrosEmpresaDemandanteDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerEmpresaDemandante(filtrosEmpresaDemandanteDTO, admin);
  }

  private List<Map<String, Object>> lerEmpresaDemandante(
      FiltrosEmpresaDemandanteDTO filtrosEmpresaDemandanteDTO, Admin admin) {
    try (Connection connection = Conexao.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();

      if (filtrosEmpresaDemandanteDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEmpresaDemandante.ID.getCamposEmpresaDemandante(),
            IGUAL,
            VAZIO,
            filtrosEmpresaDemandanteDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposEmpresaDemandante.ID.getCamposEmpresaDemandante(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosEmpresaDemandanteDTO.idUsuarioFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEmpresaDemandante.ID_USUARIO.getCamposEmpresaDemandante(),
            IGUAL,
            AND,
            filtrosEmpresaDemandanteDTO.formatarIdUsuario(),
            Types.BIGINT);
      }

      if (filtrosEmpresaDemandanteDTO.cnpjFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEmpresaDemandante.CNPJ.getCamposEmpresaDemandante(),
            IGUAL,
            AND,
            filtrosEmpresaDemandanteDTO.formatarCnpj(),
            Types.VARCHAR);
      }

      if (filtrosEmpresaDemandanteDTO.razaoSocialFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEmpresaDemandante.RAZAO_SOCIAL.getCamposEmpresaDemandante(),
            ILIKE,
            AND,
            filtrosEmpresaDemandanteDTO.formatarRazaoSocial(),
            Types.VARCHAR);
      }


      if (filtrosEmpresaDemandanteDTO.ehMandanteFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposEmpresaDemandante.EH_MANDANTE.getCamposEmpresaDemandante(),
            IGUAL,
            AND,
            filtrosEmpresaDemandanteDTO.formatarEhMandante(),
            Types.VARCHAR);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosEmpresaDemandanteDTO.joinFiltro());
      if (filtrosEmpresaDemandanteDTO.joinFiltro() != null) {
        String conector =
            JoinsPossiveis.joinValido("empresa_demandante", filtrosEmpresaDemandanteDTO.joinFiltro());

        if (conector.contains("empresa_demandante")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> empresasDemandante = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosEmpresaDemandanteDTO.filtroRapido() != null && !filtrosEmpresaDemandanteDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosEmpresaDemandanteDTO.filtroRapido();

        empresasDemandante.removeIf(empresaDemandante ->
            empresaDemandante.values().stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .noneMatch(valorTexto -> valorTexto.contains(termoFiltro))
        );
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(empresasDemandante, admin);

      for (Map<String, Object> dado : empresasDemandante) {
        dado.remove("tipo_usuario");
      }

      return empresasDemandante;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarEmpresaDemananteSelect(
      FiltrosEmpresaDemandanteDTO filtrosEmpresaDemandanteDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosEmpresaDemandanteDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosEmpresaDemandanteDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEmpresaDemandanteDTO.idUsuarioFiltro() != null) {
      GenericExceptionEnum validacao = validarIdUsuario(filtrosEmpresaDemandanteDTO.idUsuarioFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEmpresaDemandanteDTO.cnpjFiltro() != null) {
      GenericExceptionEnum validacao =
          validarCnpj(filtrosEmpresaDemandanteDTO.cnpjFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEmpresaDemandanteDTO.razaoSocialFiltro() != null) {
      GenericExceptionEnum validacao = validarFormatoRazaoSocial(filtrosEmpresaDemandanteDTO.razaoSocialFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEmpresaDemandanteDTO.ehMandanteFiltro() != null) {
      GenericExceptionEnum validacao = validarEhMandante(filtrosEmpresaDemandanteDTO.ehMandanteFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosEmpresaDemandanteDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("empresa_demandante", filtrosEmpresaDemandanteDTO.joinFiltro()) == null) {
        erros.add(JOIN_INVALIDO);
      }
    }
    return erros;
  }
  
  //Métodos relacionados ao update
  public List<GenericExceptionEnum> realizarUpdate(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> erros = validarUpdate(jsonObject);
    if (!erros.isEmpty()) {
      return erros;
    }

    GenericExceptionEnum resultado = atualizarEmpresaDemandante(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarEmpresaDemandante(JsonObject jsonObject, Long idAdmin) {
    EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();

    try (Connection connection = Conexao.getConnection()) {
      connection.setAutoCommit(false);

      try {
        EmpresaDemandante empresaDemandante =
            GSON.fromJson(jsonObject, EmpresaDemandanteParaUpdateDTO.class).construirEmpresaDemandante();
        Long id = empresaDemandante.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> profissionalLido =
            dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (profissionalLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEmpresaDemandante.RAZAO_SOCIAL.getCamposEmpresaDemandante(),
            empresaDemandante.getRazaoSocial(),
            Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposEmpresaDemandante.EH_MANDANTE.getCamposEmpresaDemandante(),
            empresaDemandante.isEhMandante(),
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
                "EMPRESA_DEMANDANTE",
                "UPDATE",
                GSON.toJsonTree(profissionalLido.getFirst()).getAsJsonObject(),
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

    EmpresaDemandanteParaUpdateDTO empresaDemandanteParaUpdateDTO =
        GSON.fromJson(jsonObject, EmpresaDemandanteParaUpdateDTO.class);

    GenericExceptionEnum razaoSocialValidacao =
        validarRazaoSocial(empresaDemandanteParaUpdateDTO.razaoSocialUpdate());
    if (razaoSocialValidacao != VALIDACAO_OK
        && (empresaDemandanteParaUpdateDTO.razaoSocialUpdate() != null
        && !empresaDemandanteParaUpdateDTO.razaoSocialUpdate().isBlank())) {
      erros.add(razaoSocialValidacao);
    }

    GenericExceptionEnum ehMandanteValidacao =
        validarEhMandante(empresaDemandanteParaUpdateDTO.ehMandanteUpdate());
    if (ehMandanteValidacao != VALIDACAO_OK
        && (empresaDemandanteParaUpdateDTO.ehMandanteUpdate() != null
        && !empresaDemandanteParaUpdateDTO.ehMandanteUpdate().isBlank())) {
      erros.add(ehMandanteValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirEmpresaDemandanteParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposUsuario.ID.getCampoUsuario(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = Conexao.getConnection()) {
      EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();

      List<Map<String, Object>> resultados = dao.readAll(criarInstrucaoDinamica, connection);

      if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
        for (Map<String, Object> registro : resultados) {
          registro.remove("razao_social");
          registro.remove("tipo_usuario");
          registro.replace(
              "cnpj",
              PorMascara.porMascaraCnpj(
                  Anonimizador.anonimizarCnpjECpf((String) registro.get("cnpj"), true)));
        }
      } else {
        for (Map<String, Object> registro : resultados) {
          registro.remove("tipo_usuario");
          registro.replace("cnpj", PorMascara.porMascaraCnpj((String) registro.get("cnpj")));
        }
      }

      return resultados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  //Métodos relacionados ao insert
  public static List<GenericExceptionEnum> validarEmpresaDemandanteInsert(
      EmpresaDemandanteParaInsertDTO empresaDemandanteParaInsertDTO) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    GenericExceptionEnum validacaoTipoUsuario = validarTipoUsuario(
        empresaDemandanteParaInsertDTO.tipoUsuario());
    if (validacaoTipoUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoTipoUsuario);
    }

    GenericExceptionEnum ehMandanteValidacao = validarEhMandante(
        empresaDemandanteParaInsertDTO.ehMandante());
    if (ehMandanteValidacao != VALIDACAO_OK) {
      listaDeErros.add(ehMandanteValidacao);
    }

    GenericExceptionEnum validacaoCnpj = validarCnpj(empresaDemandanteParaInsertDTO.cnpj());
    if (validacaoCnpj != VALIDACAO_OK && validacaoCnpj != ATRIBUTO_NULL) {
      listaDeErros.add(validacaoCnpj);
    }

    GenericExceptionEnum validacaoRazaoSocial = validarRazaoSocial(
        empresaDemandanteParaInsertDTO.razaoSocial());
    if (validacaoRazaoSocial != VALIDACAO_OK) {
      listaDeErros.add(validacaoRazaoSocial);
    }
    return listaDeErros;
  }

}
