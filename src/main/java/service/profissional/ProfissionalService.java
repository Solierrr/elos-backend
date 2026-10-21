package service.profissional;

import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosDadosProfissional.*;
import static exception.ErrosGerais.*;
import static exception.ErrosGeraisDados.*;
import static service.ValidacoesComunsService.validarId;
import static service.profissional.CamposProfissional.*;
import static util.Anonimizador.anonimizarCnpjECpf;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import conexao.Conexao;
import dao.AcoesInstrucao;
import dao.ConstraintsBanco;
import dao.CriarInstrucaoDinamica;
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
import model.FuncoesAdmin;
import model.HistoricoAlteracoesAdmin;
import model.Profissional;
import model.TiposUsuario;
import service.JoinsPossiveis;
import service.ValidacoesComunsService;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import service.usuario.CamposUsuario;
import util.Anonimizador;
import util.Criptografia;
import util.PorMascara;

public final class ProfissionalService {

  // Atributo usado para operações json no código
  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  // Constantes para evitar valores mágicos ou instancia desnecessária de objeto
  private static final Pattern PATTERN_PROFISSAO =
      Pattern.compile("^[\\p{Script=Latin}0-9,.\\s\\-/]+$");
  private static final int TAMANHO_MAXIMO_PROFISSAO = 100;

  private static final int TAMANHO_CPF = 11;
  private static final Pattern PATTERN_CPF = Pattern.compile("^[0-9]{11}$");

  // Validações

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

  // Validação do tipo usuario
  private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario) {
    TiposUsuario tiposUsuario = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
    if (tipoUsuario == null || tiposUsuario != TiposUsuario.PROFISSIONAL) {
      return TIPO_USUARIO_INCOMPATIVEL;
    }
    return VALIDACAO_OK;
  }

  // Validação do id fornecedor
  private static GenericExceptionEnum validarIdFornecedor(String idFornecedor) {
    if (idFornecedor == null) {
      return VALIDACAO_OK;
    }
    if (ValidacoesComunsService.validarId(idFornecedor) != VALIDACAO_OK) {
      return ID_FORNECEDOR_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  // Validações da profissão
  private static GenericExceptionEnum validarProfissao(String profissao) {
    if (profissao == null || profissao.isBlank()) {
      return PROFISSAO_VAZIA;
    }

    String profissaoTratada = profissao.strip();
    if (profissaoTratada.length() > TAMANHO_MAXIMO_PROFISSAO) {
      return PROFISSAO_TAMANHO_INVALIDO;
    }

    if (profissaoTratada.replace(profissaoTratada.charAt(0), ' ').isBlank()) {
      return PROFISSAO_INVALIDA;
    }
    return validarFormatoProfissao(profissaoTratada);
  }

  private static GenericExceptionEnum validarFormatoProfissao(String profissao) {
    return PATTERN_PROFISSAO.matcher(profissao).matches() ? VALIDACAO_OK : PROFISSAO_INVALIDA;
  }

  // Validações do cpf
  private static GenericExceptionEnum validarCpf(String cpf) {
    if (cpf == null || cpf.isBlank()) {
      return CPF_VAZIO;
    }

    String cpfTratado = cpf.strip().replaceAll("\\.", "").replaceAll("-", "");

    if (cpfTratado.length() != TAMANHO_CPF) {
      return CPF_TAMANHO_INVALIDO;
    }

    if (cpfTratado.replace(cpfTratado.charAt(0), ' ').isBlank()) {
      return CPF_INVALIDO;
    }

    GenericExceptionEnum validacaoFormatoCpf = validarFormatoCpf(cpfTratado);
    if (validacaoFormatoCpf != VALIDACAO_OK) {
      return validacaoFormatoCpf;
    }

    GenericExceptionEnum validacaoCondicaoExistenciaCpf =
        validarCondicaoDeExistenciaCpf(cpfTratado);
    if (validacaoCondicaoExistenciaCpf != VALIDACAO_OK) {
      return validacaoCondicaoExistenciaCpf;
    }

    return VALIDACAO_OK;
  }

  private static GenericExceptionEnum validarFormatoCpf(String cpf) {
    return PATTERN_CPF.matcher(cpf).matches() ? VALIDACAO_OK : CPF_NAO_NUMERICO;
  }

  private static GenericExceptionEnum validarCondicaoDeExistenciaCpf(String cpf) {
    int soma = 0;
    int peso = 10;
    for (int i = 0; i < 9; i++) {
      soma += (cpf.charAt(i) - '0') * peso--;
    }
    int resto = 11 - (soma % 11);
    int digito1 = (resto == 10 || resto == 11) ? 0 : resto;

    if (digito1 != (cpf.charAt(9) - '0')) {
      return CPF_INVALIDO;
    }

    soma = 0;
    peso = 11;
    for (int i = 0; i < 10; i++) {
      soma += (cpf.charAt(i) - '0') * peso--;
    }
    resto = 11 - (soma % 11);
    int digito2 = (resto == 10 || resto == 11) ? 0 : resto;

    return digito2 == (cpf.charAt(10) - '0') ? VALIDACAO_OK : CPF_INVALIDO;
  }

  // Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarProfissional(id, idAdmin);
  }

  private GenericExceptionEnum deletarProfissional(String id, Long idAdmin) {
    ProfissionalDAO dao = new ProfissionalDAO();

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
                "PROFISSIONAL",
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
    ProfissionalDAO dao = new ProfissionalDAO();

    try (Connection connection = Conexao.getConnection()) {
      List<Map<String, Object>> profissionais = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(profissionais, admin);

      return profissionais;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(
      FiltrosProfissionalDTO filtrosProfissionalDTO,
      List<GenericExceptionEnum> errosEncontrados,
      Admin admin) {
    if (filtrosProfissionalDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarProfissionalSelect(filtrosProfissionalDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerProfissional(filtrosProfissionalDTO, admin);
  }

  private List<Map<String, Object>> lerProfissional(
      FiltrosProfissionalDTO filtrosProfissionalDTO, Admin admin) {
    try (Connection connection = Conexao.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      ProfissionalDAO dao = new ProfissionalDAO();

      if (filtrosProfissionalDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposProfissional.ID.getCampoProfissional(),
            IGUAL,
            VAZIO,
            filtrosProfissionalDTO.formatarId(),
            Types.BIGINT);
        criarInstrucaoDinamica.setOrderBy(
            CamposProfissional.ID.getCampoProfissional(), AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosProfissionalDTO.idUsuarioFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            ID_USUARIO.getCampoProfissional(),
            IGUAL,
            AND,
            filtrosProfissionalDTO.formatarIdUsuario(),
            Types.BIGINT);
      }

      if (filtrosProfissionalDTO.profissaoFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            PROFISSAO.getCampoProfissional(),
            ILIKE,
            AND,
            filtrosProfissionalDTO.formatarProfissao(),
            Types.VARCHAR);
      }

      if (filtrosProfissionalDTO.cpfFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            CamposProfissional.CPF_HMAC.getCampoProfissional(),
            IGUAL,
            AND,
            filtrosProfissionalDTO.formatarCpf(),
            Types.VARCHAR);
      }

      if (filtrosProfissionalDTO.idFornecedorFiltro() != null) {
        criarInstrucaoDinamica.setWhere(
            ID_FORNECEDOR.getCampoProfissional(),
            IGUAL,
            AND,
            filtrosProfissionalDTO.formatarIdFornecedor(),
            Types.BIGINT);
      }

      JoinsPossiveis tabelaConectada =
          JoinsPossiveis.encontrarTabelaReferente(filtrosProfissionalDTO.joinFiltro());
      if (filtrosProfissionalDTO.joinFiltro() != null) {
        String conector =
            JoinsPossiveis.joinValido("profissional", filtrosProfissionalDTO.joinFiltro());

        if (conector.contains("profissional")) {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), "id", conector);
        } else {
          criarInstrucaoDinamica.setJoin(
              INNER_JOIN, tabelaConectada.getNomeTabela(), conector, "id");
        }
      }

      List<Map<String, Object>> profissionais = dao.readAll(criarInstrucaoDinamica, connection);

      if (filtrosProfissionalDTO.filtroRapido() != null && !filtrosProfissionalDTO.filtroRapido().isBlank()) {
        String termoFiltro = filtrosProfissionalDTO.filtroRapido();

        profissionais.removeIf(profissional ->
                profissional.values().stream()
                        .filter(Objects::nonNull)
                        .map(String::valueOf)
                        .noneMatch(valorTexto -> valorTexto.contains(termoFiltro))
        );
      }

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(profissionais, admin);

      for (Map<String, Object> dado : profissionais) {
        dado.remove("tipo_usuario");
      }

      return profissionais;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarProfissionalSelect(
      FiltrosProfissionalDTO filtrosProfissionalDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosProfissionalDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosProfissionalDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosProfissionalDTO.idUsuarioFiltro() != null) {
      GenericExceptionEnum validacao = validarIdUsuario(filtrosProfissionalDTO.idUsuarioFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosProfissionalDTO.profissaoFiltro() != null) {
      GenericExceptionEnum validacao = validarProfissao(filtrosProfissionalDTO.profissaoFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosProfissionalDTO.cpfFiltro() != null) {
      GenericExceptionEnum validacao = validarCpf(filtrosProfissionalDTO.cpfFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosProfissionalDTO.idFornecedorFiltro() != null) {
      GenericExceptionEnum validacao =
          validarIdFornecedor(filtrosProfissionalDTO.idFornecedorFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosProfissionalDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("profissional", filtrosProfissionalDTO.joinFiltro()) == null) {
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

    GenericExceptionEnum resultado = atualizarProfissional(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

  private GenericExceptionEnum atualizarProfissional(JsonObject jsonObject, Long idAdmin) {
    ProfissionalDAO dao = new ProfissionalDAO();

    try (Connection connection = Conexao.getConnection()) {
      connection.setAutoCommit(false);

      try {
        Profissional profissional =
            GSON.fromJson(jsonObject, ProfissionalParaUpdateDTO.class).construirProfissional();
        Long id = profissional.getId();

        CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
        criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

        List<Map<String, Object>> profissionalLido =
            dao.readAll(criarInstrucaoDinamicaRead, connection);
        if (profissionalLido.isEmpty()) {
          throw new IllegalArgumentException("ID não possui um correspondente no banco!");
        }

        CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

        criarInstrucaoDinamicaUpdate.setCampo(
            CamposProfissional.ID_FORNECEDOR.getCampoProfissional(),
            profissional.getIdFornecedor(),
            Types.BIGINT);
        criarInstrucaoDinamicaUpdate.setCampo(
            PROFISSAO.getCampoProfissional(), profissional.getProfissao(), Types.VARCHAR);
        criarInstrucaoDinamicaUpdate.setCampo(
            DOCUMENTO_CONTRATO.getCampoProfissional(),
            profissional.getDocumentoContrato(),
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
                "PROFISSIONAL",
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

    ProfissionalParaUpdateDTO profissionalParaUpdateDTO =
        GSON.fromJson(jsonObject, ProfissionalParaUpdateDTO.class);

    GenericExceptionEnum idFornecedorValidacao =
        validarIdFornecedor(profissionalParaUpdateDTO.idFornecedorUpdate());
    if (idFornecedorValidacao != VALIDACAO_OK
        && (profissionalParaUpdateDTO.idFornecedorUpdate() != null
            && !profissionalParaUpdateDTO.idFornecedorUpdate().isBlank())) {
      erros.add(idFornecedorValidacao);
    }

    GenericExceptionEnum profissaoValidacao =
        validarProfissao(profissionalParaUpdateDTO.profissaoUpdate());
    if (profissaoValidacao != VALIDACAO_OK
        && (profissionalParaUpdateDTO.profissaoUpdate() != null
            && !profissionalParaUpdateDTO.profissaoUpdate().isBlank())) {
      erros.add(profissaoValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirProfissionalParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(
        CamposUsuario.ID.getCampoUsuario(), IGUAL, VAZIO, Long.parseLong(id), Types.BIGINT);
    try (Connection connection = Conexao.getConnection()) {
      ProfissionalDAO dao = new ProfissionalDAO();

      List<Map<String, Object>> resultados = dao.readAll(criarInstrucaoDinamica, connection);

      if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
        for (Map<String, Object> registro : resultados) {
          registro.remove("documento_contrato");
          registro.remove("tipo_usuario");
          registro.put(
              "cpf",
              PorMascara.porMascaraCpf(
                  anonimizarCnpjECpf(
                      new Criptografia().descriptografarAes((byte[]) registro.get("cpf_aes")),
                      false)));
          registro.remove("cpf_aes");
          registro.remove("cpf_hmac");
        }
      } else {
        for (Map<String, Object> registro : resultados) {
          registro.remove("tipo_usuario");
          registro.put(
              "cpf",
              PorMascara.porMascaraCpf(
                  new Criptografia().descriptografarAes((byte[]) registro.get("cpf_aes"))));
          registro.remove("cpf_aes");
          registro.remove("cpf_hmac");
        }
      }

      return resultados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  // Métodos relacionados ao insert
  public static List<GenericExceptionEnum> validarProfissionalInsert(
      ProfissionalParaInsertDTO profissionalParaInsertDTO) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    GenericExceptionEnum validacaoIdUsuario =
        validarTipoUsuario(profissionalParaInsertDTO.tipoUsuario());
    if (validacaoIdUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoIdUsuario);
    }

    GenericExceptionEnum validacaoProfissao =
        validarProfissao(profissionalParaInsertDTO.profissao());
    if (validacaoProfissao != VALIDACAO_OK) {
      listaDeErros.add(validacaoProfissao);
    }

    GenericExceptionEnum validacaoCpf = validarCpf(profissionalParaInsertDTO.cpf());
    if (validacaoCpf != VALIDACAO_OK && validacaoCpf != ATRIBUTO_NULL) {
      listaDeErros.add(validacaoCpf);
    }

    GenericExceptionEnum validacaoIdFornecedor =
        validarIdFornecedor(profissionalParaInsertDTO.idFornecedor());
    if (validacaoIdFornecedor != VALIDACAO_OK) {
      listaDeErros.add(validacaoIdFornecedor);
    }
    return listaDeErros;
  }
}
