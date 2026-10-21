package service.fornecedor;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosDadosFornecedor.TIPO_FORNECEDOR_INVALIDO;
import static exception.ErrosDadosFornecedor.TIPO_FORNECEDOR_VAZIO;
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
import dao.FornecedorDAO;
import dao.ProfissionalDAO;
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
import model.Fornecedor;
import model.FuncoesAdmin;
import model.HistoricoAlteracoesAdmin;
import model.TiposFornecedor;
import model.TiposUsuario;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import service.usuario.CamposUsuario;
import util.Anonimizador;
import util.PorMascara;

public final class FornecedorService {

  // Atributo usado para operações json no código
  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  // Constantes para evitar valores mágicos ou instancia desnecessária de objetos
  private static final int TAMANHO_CNPJ = 14;
  private static final Pattern PATTERN_CNPJ = Pattern.compile("^[0-9-A-Z]{12}[0-9]{2}$");

  private static final Pattern PATTERN_RAZAO_SOCIAL =
      Pattern.compile("^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

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

  // Validação do tipoUsuario
  private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario) {
    TiposUsuario tiposUsuario = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
    if (tipoUsuario == null || tiposUsuario != TiposUsuario.FORNECEDOR) {
      return TIPO_USUARIO_INCOMPATIVEL;
    }
    return VALIDACAO_OK;
  }

  // Validação do tipo fornecedor
  private static GenericExceptionEnum validarTipoFornecedor(String tipoFornecedor) {
    if (tipoFornecedor == null || tipoFornecedor.isBlank()) {
      return TIPO_FORNECEDOR_VAZIO;
    }

    TiposFornecedor tiposFornecedorEncontrado =
        TiposFornecedor.descobrirTipoFornecedor(tipoFornecedor.toUpperCase().strip());
    return tiposFornecedorEncontrado == null ? TIPO_FORNECEDOR_INVALIDO : VALIDACAO_OK;
  }

  // Validações do cnpj
  private static GenericExceptionEnum validarCnpj(String cnpj) {
    if (cnpj == null || cnpj.isBlank()) {
      return CNPJ_VAZIO;
    }

    String cnpjTratado =
        cnpj.strip().toUpperCase().replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");

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

  // Validações da razão social
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
    return PATTERN_RAZAO_SOCIAL.matcher(razaoSocial).matches()
        ? VALIDACAO_OK
        : RAZAO_SOCIAL_INVALIDA;
  }

  // Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarFornecedor(id, idAdmin);
  }

  private GenericExceptionEnum deletarFornecedor(String id, Long idAdmin) {
    FornecedorDAO dao = new FornecedorDAO();

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
                "FORNECEDOR",
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

  // Métodos relacionados ao select
  public List<Map<String, Object>> realizarSelectInicial(Admin admin) {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    FornecedorDAO dao = new FornecedorDAO();

    try (Connection connection = Conexao.getConnection()) {
      List<Map<String, Object>> fornecedores = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(fornecedores, admin);

      for (Map<String, Object> dado : fornecedores) {
        dado.remove("tipo_usuario");
      }

      return fornecedores;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosFornecedorDTO filtrosFornecedorDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosFornecedorDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarFornecedorSelect(filtrosFornecedorDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerFornecedores(filtrosFornecedorDTO, admin);
  }

  private List<Map<String, Object>> lerFornecedores(
      FiltrosFornecedorDTO filtrosFornecedorDTO, Admin admin) {
    try (Connection connection = Conexao.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      FornecedorDAO dao = new FornecedorDAO();

      if (filtrosFornecedorDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposFornecedor.ID.getCampoFornecedor(),
            IGUAL,
            VAZIO,
            filtrosFornecedorDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposFornecedor.ID.getCampoFornecedor(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosFornecedorDTO.idUsuarioFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposFornecedor.ID_USUARIO.getCampoFornecedor(),
            IGUAL,
            AND,
            filtrosFornecedorDTO.formatarIdUsuario(),
            Types.BIGINT);
      }

      if (filtrosFornecedorDTO.tipoFornecedorFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposFornecedor.TIPO_FORNECEDOR.getCampoFornecedor(),
            IGUAL,
            AND,
            filtrosFornecedorDTO.formatarTipoFornecedor(),
            Types.VARCHAR);
      }

      if (filtrosFornecedorDTO.cnpjFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposFornecedor.CNPJ.getCampoFornecedor(),
            IGUAL,
            AND,
            filtrosFornecedorDTO.formatarCnpj(),
            Types.VARCHAR);
      }

      if (filtrosFornecedorDTO.razaoSocialFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposFornecedor.RAZAO_SOCIAL.getCampoFornecedor(),
            ILIKE,
            AND,
            filtrosFornecedorDTO.formatarRazaoSocial(),
            Types.VARCHAR);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosFornecedorDTO.joinFiltro());
      if (filtrosFornecedorDTO.joinFiltro() != null) {
        String conector =
            JoinsPossiveis.joinValido("fornecedor", filtrosFornecedorDTO.joinFiltro());

        if (conector.contains("fornecedor")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> fornecedores = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosFornecedorDTO.filtroRapido() != null && !filtrosFornecedorDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosFornecedorDTO.filtroRapido();

        fornecedores.removeIf(fornecedor ->
                fornecedor.values().stream()
                        .filter(Objects::nonNull)
                        .map(String::valueOf)
                        .noneMatch(valorTexto -> valorTexto.contains(termoFiltro))
        );
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(fornecedores, admin);

      for (Map<String, Object> dado : fornecedores) {
        dado.remove("tipo_usuario");
      }

      return fornecedores;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarFornecedorSelect(
      FiltrosFornecedorDTO filtrosFornecedorDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosFornecedorDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosFornecedorDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosFornecedorDTO.idUsuarioFiltro() != null) {
      GenericExceptionEnum validacao = validarIdUsuario(filtrosFornecedorDTO.idUsuarioFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosFornecedorDTO.tipoFornecedorFiltro() != null) {
      GenericExceptionEnum validacao =
          validarTipoFornecedor(filtrosFornecedorDTO.tipoFornecedorFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosFornecedorDTO.cnpjFiltro() != null) {
      GenericExceptionEnum validacao = validarCnpj(filtrosFornecedorDTO.cnpjFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosFornecedorDTO.razaoSocialFiltro() != null) {
      GenericExceptionEnum validacao = validarRazaoSocial(filtrosFornecedorDTO.razaoSocialFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosFornecedorDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("fornecedor", filtrosFornecedorDTO.joinFiltro()) == null) {
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

    GenericExceptionEnum resultado = atualizarFornecedor(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarFornecedor(JsonObject jsonObject, Long idAdmin) {
    FornecedorDAO dao = new FornecedorDAO();

    try (Connection connection = Conexao.getConnection()) {
      connection.setAutoCommit(false);

      try {
        Fornecedor fornecedor =
            GSON.fromJson(jsonObject, FornecedorParaUpdateDTO.class).construirFornecedor();
        Long id = fornecedor.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> profissionalLido =
            dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (profissionalLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposFornecedor.TIPO_FORNECEDOR.getCampoFornecedor(),
            fornecedor.getTipoFornecedor().getTipoFornecedor(),
            Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            CamposFornecedor.RAZAO_SOCIAL.getCampoFornecedor(),
            fornecedor.getRazaoSocial(),
            Types.VARCHAR);

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
                "FORNECEDOR",
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

    FornecedorParaUpdateDTO fornecedorParaUpdateDTO =
        GSON.fromJson(jsonObject, FornecedorParaUpdateDTO.class);

    GenericExceptionEnum tipoFornecedorValidacao =
        validarTipoFornecedor(fornecedorParaUpdateDTO.tipoFornecedorUpdate());
    if (tipoFornecedorValidacao != VALIDACAO_OK
        && (fornecedorParaUpdateDTO.tipoFornecedorUpdate() != null
            && !fornecedorParaUpdateDTO.tipoFornecedorUpdate().isBlank())) {
      erros.add(tipoFornecedorValidacao);
    }

    GenericExceptionEnum razaoSocialValidacao =
        validarRazaoSocial(fornecedorParaUpdateDTO.razaoSocialUpdate());
    if (razaoSocialValidacao != VALIDACAO_OK
        && (fornecedorParaUpdateDTO.razaoSocialUpdate() != null
            && !fornecedorParaUpdateDTO.razaoSocialUpdate().isBlank())) {
      erros.add(razaoSocialValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirFornecedorParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposUsuario.ID.getCampoUsuario(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = Conexao.getConnection()) {
      FornecedorDAO dao = new FornecedorDAO();

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

  // Método relacionado ao insert
  public static List<GenericExceptionEnum> validarFornecedorInsert(
      FornecedorParaInsertDTO fornecedorParaInsertDTO) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    GenericExceptionEnum validacaoTipoUsuario =
        validarTipoUsuario(fornecedorParaInsertDTO.tipoUsuario());
    if (validacaoTipoUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoTipoUsuario);
    }

    GenericExceptionEnum validacaoTipoFornecedor =
        validarTipoFornecedor(fornecedorParaInsertDTO.tipoFornecedor());
    if (validacaoTipoFornecedor != VALIDACAO_OK) {
      listaDeErros.add(validacaoTipoFornecedor);
    }

    GenericExceptionEnum validacaoCnpj = validarCnpj(fornecedorParaInsertDTO.cnpj());
    if (validacaoCnpj != VALIDACAO_OK && validacaoCnpj != ATRIBUTO_NULL) {
      listaDeErros.add(validacaoCnpj);
    }

    GenericExceptionEnum validacaoRazaoSocial =
        validarRazaoSocial(fornecedorParaInsertDTO.razaoSocial());
    if (validacaoRazaoSocial != VALIDACAO_OK) {
      listaDeErros.add(validacaoRazaoSocial);
    }
    return listaDeErros;
  }
}
