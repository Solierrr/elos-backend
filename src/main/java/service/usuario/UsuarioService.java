package service.usuario;


import static dao.AcoesInstrucao.AND;
import static dao.AcoesInstrucao.IGUAL;
import static dao.AcoesInstrucao.ILIKE;
import static dao.AcoesInstrucao.INNER_JOIN;
import static dao.AcoesInstrucao.VAZIO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGeraisDados.ATRIBUTO_NULL;
import static exception.ErrosGeraisDados.JOIN_INVALIDO;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static service.JoinsPossiveis.EMPRESA_DEMANDANTE;
import static service.ValidacoesComunsService.validarId;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import conexao.Conexao;
import dao.AcoesInstrucao;
import dao.ConstraintsBanco;
import dao.CriarInstrucaoDinamica;
import dao.UsuarioDAO;
import exception.ErrosDadosUsuario;
import exception.ErrosGeraisDados;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import model.Admin;
import model.EmpresaDemandante;
import model.Fornecedor;
import model.FuncoesAdmin;
import model.HistoricoAlteracoesAdmin;
import model.Profissional;
import model.TiposUsuario;
import model.Usuario;
import service.JoinsPossiveis;
import service.empresaDemandante.CamposEmpresaDemandante;
import service.empresaDemandante.EmpresaDemandanteParaInsertDTO;
import service.empresaDemandante.EmpresaDemandanteService;
import service.fornecedor.CamposFornecedor;
import service.fornecedor.FornecedorParaInsertDTO;
import service.fornecedor.FornecedorService;
import service.historicoAlteracoesAdmin.HistoricoAlteracoesAdminService;
import service.profissional.CamposProfissional;
import service.profissional.ProfissionalParaInsertDTO;
import service.profissional.ProfissionalService;
import util.Anonimizador;

public final class UsuarioService {

  //Atributo usado para operações json no código
  private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(
      FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
  private static final double TAMANHO_MAXIMO_RAIO_PROCURA_KM = 999.99;

  private static final int TAMANHO_MAXIMO_NOME = 150;

  private static final Pattern PATTERN_PESSOA_FISICA = Pattern.compile(
      "^[\\p{Script=Latin}']+[\\p{Script=Latin}'\\s\\-]+$");
  private static final Pattern PATTERN_PESSOA_JURIDICA = Pattern.compile(
      "^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

  private static final int TAMANHO_MINIMO_SENHA = 8;
  private static final int TAMANHO_MAXIMO_SENHA = 60;

  //Exige uma maiúscula, minuscula, número e caractere especial
  private static final Pattern PATTERN_SENHA = Pattern.compile(
      "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");

  private static final Pattern PATTERN_EMAIL = Pattern.compile(
      "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
  private static final int TAMANHO_MAXIMO_EMAIL = 256;

  //Validações

  //Validações do email
  private static GenericExceptionEnum validarEmail(String email) {
    if (email == null || email.isBlank()) {
      return ErrosGeraisDados.EMAIL_VAZIO;
    }

    String emailTratado = email.toLowerCase().strip();
    if (emailTratado.length() > TAMANHO_MAXIMO_EMAIL) {
      return ErrosGeraisDados.EMAIL_TAMANHO_INVALIDO;
    }
    return validarFormatoEmail(emailTratado) != VALIDACAO_OK ? ErrosGeraisDados.EMAIL_INVALIDO
        : VALIDACAO_OK;
  }

  private static GenericExceptionEnum validarFormatoEmail(String email) {
    return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : ErrosGeraisDados.EMAIL_INVALIDO;
  }

  //Validações da senha
  private static GenericExceptionEnum validarSenhaUpdate(String senha) {
    if (senha == null || senha.isBlank()) {
      return VALIDACAO_OK;
    }

    String senhaTrim = senha.strip();
    return validarSenha(senhaTrim);
  }

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

  //Validações do nome
  private static GenericExceptionEnum validarNome(String nome, String tipoUsuario) {
    if (nome == null || nome.isBlank()) {
      return ErrosDadosUsuario.NOME_VAZIO;
    }

    String nomeTrim = nome.strip();
    if (nomeTrim.length() > TAMANHO_MAXIMO_NOME) {
      return ErrosDadosUsuario.NOME_TAMANHO_INVALIDO;
    }

    if (nomeTrim.toLowerCase().replace(nomeTrim.charAt(0), ' ').isBlank()) {
      return ErrosDadosUsuario.NOME_INVALIDO;
    }

    TiposUsuario tiposUsuarioInserido = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
    return TiposUsuario.PROFISSIONAL == tiposUsuarioInserido ? validarNomePessoaFisica(nomeTrim)
        : validarNomePessoaJuridica(nomeTrim);
  }

  private static GenericExceptionEnum validarNomePessoaFisica(String nome) {
    return PATTERN_PESSOA_FISICA.matcher(nome).matches() ? VALIDACAO_OK
        : ErrosDadosUsuario.NOME_INVALIDO;
  }

  private static GenericExceptionEnum validarNomePessoaJuridica(String nome) {
    return PATTERN_PESSOA_JURIDICA.matcher(nome).matches() ? VALIDACAO_OK
        : ErrosDadosUsuario.NOME_INVALIDO;
  }

  //Validação do tipo do usuario
  private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario) {
    if (tipoUsuario == null || tipoUsuario.isBlank()) {
      return ErrosDadosUsuario.TIPO_USUARIO_VAZIO;
    }

    if (TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip()) == null) {
      return ErrosDadosUsuario.TIPO_USUARIO_INVALIDO;
    }
    return VALIDACAO_OK;
  }

  //Validações do raio de procura em km
  private static GenericExceptionEnum validarRaioProcuraKm(String raioProcuraKm) {
    if (raioProcuraKm == null || raioProcuraKm.isBlank()) {
      return ATRIBUTO_NULL;
    }

    Double raioProcuraKmConvertido = validarRaioProcuraKmConversivel(raioProcuraKm);
    if (raioProcuraKmConvertido == null) {
      return ErrosDadosUsuario.RAIO_PROCURA_KM_NAO_NUMERICO;
    }

    if (raioProcuraKmConvertido <= 0) {
      return ErrosDadosUsuario.RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO;
    }

    if (raioProcuraKmConvertido > TAMANHO_MAXIMO_RAIO_PROCURA_KM) {
      return ErrosDadosUsuario.RAIO_PROCURA_KM_TAMANHO_INVALIDO;
    }

    return VALIDACAO_OK;
  }

  private static Double validarRaioProcuraKmConversivel(String raioProcuraKm) {
    try {
      String raioProcuraKmTrim = raioProcuraKm.strip().replace(',', '.');
      return Double.parseDouble(raioProcuraKmTrim);
    } catch (NumberFormatException numberFormatException) {
      return null;
    }
  }

  //Métodos relacionados ao delete
  public GenericExceptionEnum realizarDelete(String id, Long idAdmin) {
    if (validarId(id) != VALIDACAO_OK) {
      return ERRO_GENERICO;
    }
    return deletarUsuario(id, idAdmin);
  }

  private GenericExceptionEnum deletarUsuario(String id, Long idAdmin) {
    UsuarioDAO dao = new UsuarioDAO();

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
            new HistoricoAlteracoesAdmin(null, idAdmin, idConvertido, "USUARIO", "DELETE",
                GSON.toJsonTree(usuario.getFirst()).getAsJsonObject(), new JsonObject(), null),
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
  public List<Map<String, Object>> realizarSelectIncial(Admin admin) {
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    UsuarioDAO dao = new UsuarioDAO();

    try (Connection connection = Conexao.getConnection()) {
      List<Map<String, Object>> usuarios = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(usuarios, admin);

      return usuarios;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  public List<Map<String, Object>> realizarSelect(FiltrosUsuarioDTO filtrosUsuarioDTO,
      List<GenericExceptionEnum> errosEncontrados, Admin admin) {
    if (filtrosUsuarioDTO == null) {
      return new ArrayList<>();
    }

    errosEncontrados.addAll(validarUsuarioSelect(filtrosUsuarioDTO));
    if (!errosEncontrados.isEmpty()) {
      return new ArrayList<>();
    }
    return lerUsuarios(filtrosUsuarioDTO, admin);
  }

  private List<Map<String, Object>> lerUsuarios(FiltrosUsuarioDTO filtrosUsuarioDTO, Admin admin) {
    try (Connection connection = Conexao.getConnection()) {
      CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
      UsuarioDAO dao = new UsuarioDAO();

      if (filtrosUsuarioDTO.idFiltro() != null) {
        criarInstrucaoDinamica.setWhere(CamposUsuario.ID.getCampoUsuario(), IGUAL, VAZIO,
            filtrosUsuarioDTO.formatarId(), CamposUsuario.ID.getDataType());
        criarInstrucaoDinamica.setOrderBy(CamposUsuario.ID.getCampoUsuario(),
            AcoesInstrucao.ORDEM_CRESCENTE);
      }

      if (filtrosUsuarioDTO.emailFiltro() != null) {
        criarInstrucaoDinamica.setWhere(CamposUsuario.EMAIL.getCampoUsuario(), ILIKE, AND,
            filtrosUsuarioDTO.formatarEmail(), CamposUsuario.EMAIL.getDataType());
      }

      if (filtrosUsuarioDTO.nomeFiltro() != null) {
        criarInstrucaoDinamica.setWhere(CamposUsuario.NOME.getCampoUsuario(), ILIKE, AND,
            filtrosUsuarioDTO.formatarNome(), CamposUsuario.NOME.getDataType());
      }

      if (filtrosUsuarioDTO.tipoUsuarioFiltro() != null) {
        criarInstrucaoDinamica.setWhere(CamposUsuario.TIPO_USUARIO.getCampoUsuario(), IGUAL, AND,
            filtrosUsuarioDTO.formatarTipoUsuario(), CamposUsuario.TIPO_USUARIO.getDataType());
      }

      if (filtrosUsuarioDTO.raioProcuraKmFiltro() != null) {
        criarInstrucaoDinamica.setWhere(CamposUsuario.RAIO_PROCURA_KM.getCampoUsuario(),
            AcoesInstrucao.MENOR_IGUAL, AND, filtrosUsuarioDTO.formatarRaioProcura(),
            CamposUsuario.RAIO_PROCURA_KM.getDataType());
      }

      JoinsPossiveis tabelaConectada = JoinsPossiveis.encontrarTabelaReferente(
          filtrosUsuarioDTO.joinFiltro());
      if (filtrosUsuarioDTO.joinFiltro() != null) {
        String conector = JoinsPossiveis.joinValido("usuario", filtrosUsuarioDTO.joinFiltro());

        if (conector.contains("usuario")) {
          criarInstrucaoDinamica.setJoin(INNER_JOIN, tabelaConectada.getNomeTabela(), "id",
              conector);
        } else {
          criarInstrucaoDinamica.setJoin(INNER_JOIN, tabelaConectada.getNomeTabela(), conector,
              "id");
        }

      }

      List<Map<String, Object>> usuarios = dao.readAll(criarInstrucaoDinamica, connection);

      Anonimizador anonimizador = new Anonimizador();
      anonimizador.anonimizarDadosLidos(usuarios, admin);

      return usuarios;
    } catch (SQLException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  private List<GenericExceptionEnum> validarUsuarioSelect(FiltrosUsuarioDTO filtrosUsuarioDTO) {
    List<GenericExceptionEnum> erros = new ArrayList<>();

    if (filtrosUsuarioDTO.idFiltro() != null) {
      GenericExceptionEnum validacao = validarId(filtrosUsuarioDTO.idFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosUsuarioDTO.nomeFiltro() != null) {
      GenericExceptionEnum validacao = validarNome(filtrosUsuarioDTO.nomeFiltro(),
          TiposUsuario.EMPRESA_DEMANDANTE.getTipoDoUsuario());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosUsuarioDTO.tipoUsuarioFiltro() != null) {
      GenericExceptionEnum validacao = validarTipoUsuario(filtrosUsuarioDTO.tipoUsuarioFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosUsuarioDTO.raioProcuraKmFiltro() != null) {
      GenericExceptionEnum validacao = validarRaioProcuraKm(
          filtrosUsuarioDTO.raioProcuraKmFiltro());
      if (validacao != VALIDACAO_OK) {
        erros.add(validacao);
      }
    }

    if (filtrosUsuarioDTO.joinFiltro() != null) {
      if (JoinsPossiveis.joinValido("usuario", filtrosUsuarioDTO.joinFiltro()) == null) {
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

    GenericExceptionEnum resultado = atualizarUsuario(jsonObject, idAdmin);
    if (resultado != SUCESSO) {
      erros.add(resultado);
    }
    return erros;
  }

    private GenericExceptionEnum atualizarUsuario(JsonObject jsonObject, Long idAdmin) {
      UsuarioDAO dao = new UsuarioDAO();

      try (Connection connection = Conexao.getConnection()) {
        connection.setAutoCommit(false);

        try {

          Usuario usuario = GSON.fromJson(jsonObject, UsuarioParaUpdateDTO.class).construirUsuario();
          Long id = usuario.getId();

          CriarInstrucaoDinamica criarInstrucaoDinamicaRead = new CriarInstrucaoDinamica();
          criarInstrucaoDinamicaRead.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

          List<Map<String, Object>> usuarioLido = dao.readAll(criarInstrucaoDinamicaRead, connection);
          if (usuarioLido.isEmpty()) {
            throw new IllegalArgumentException("ID não possui um correspondente no banco!");
          }

          CriarInstrucaoDinamica criarInstrucaoDinamicaUpdate = new CriarInstrucaoDinamica();

          criarInstrucaoDinamicaUpdate.setCampo(CamposUsuario.EMAIL.getCampoUsuario(),
              usuario.getEmail(), Types.VARCHAR);
          criarInstrucaoDinamicaUpdate.setCampo(CamposUsuario.SENHA.getCampoUsuario(),
              usuario.getSenha(), Types.VARCHAR);
          criarInstrucaoDinamicaUpdate.setCampo(CamposUsuario.NOME.getCampoUsuario(),
              usuario.getNome(), Types.VARCHAR);
          criarInstrucaoDinamicaUpdate.setCampo(CamposUsuario.RAIO_PROCURA_KM.getCampoUsuario(),
              usuario.getRaioProcuraKm(), Types.DOUBLE);

          criarInstrucaoDinamicaUpdate.setWhere("id", IGUAL, VAZIO, id, Types.BIGINT);

          dao.update(criarInstrucaoDinamicaUpdate, connection);

          jsonObject.entrySet().removeIf(
              campo -> campo.getValue() != null && !campo.getValue().isJsonNull() && (
                  campo.getValue().isJsonPrimitive() && campo.getValue().getAsString().trim()
                      .isEmpty()));

          HistoricoAlteracoesAdminService.salvarAcao(
              new HistoricoAlteracoesAdmin(null, idAdmin, id, "USUARIO", "UPDATE",
                  GSON.toJsonTree(usuarioLido.getFirst()).getAsJsonObject(), jsonObject, null),
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

    UsuarioParaUpdateDTO usuarioParaUpdateDTO = GSON.fromJson(jsonObject,
        UsuarioParaUpdateDTO.class);

    GenericExceptionEnum emailValidacao = validarEmail(usuarioParaUpdateDTO.emailUpdate());
    if (emailValidacao != VALIDACAO_OK && (usuarioParaUpdateDTO.emailUpdate() != null
        && !usuarioParaUpdateDTO.emailUpdate().isBlank())) {
      erros.add(emailValidacao);
    }

    GenericExceptionEnum senhaValidacao = validarSenhaUpdate(usuarioParaUpdateDTO.senhaUpdate());
    if (senhaValidacao != VALIDACAO_OK && (usuarioParaUpdateDTO.senhaUpdate() != null
        && !usuarioParaUpdateDTO.senhaUpdate().isBlank())) {
      erros.add(senhaValidacao);
    }

    //Uso de um patter mais permissivo para garantir englobar qualquer tipo de usuário na validação
    GenericExceptionEnum nomeValidacao = validarNome(usuarioParaUpdateDTO.nomeUpdate(),
        EMPRESA_DEMANDANTE.getNomeTabela());
    if (nomeValidacao != VALIDACAO_OK && (usuarioParaUpdateDTO.nomeUpdate() != null
        && !usuarioParaUpdateDTO.nomeUpdate().isBlank())) {
      erros.add(nomeValidacao);
    }

    GenericExceptionEnum raioProcuraKmValidacao = validarRaioProcuraKm(
        usuarioParaUpdateDTO.raioProcuraKmUpdate());
    if (raioProcuraKmValidacao != VALIDACAO_OK && (
        usuarioParaUpdateDTO.raioProcuraKmUpdate() != null
            && !usuarioParaUpdateDTO.raioProcuraKmUpdate().isBlank())) {
      erros.add(raioProcuraKmValidacao);
    }
    return erros;
  }

  public List<Map<String, Object>> exibirUsuarioParaUpdate(String id, Admin admin) {
    if (validarId(id) != VALIDACAO_OK) {
      return new ArrayList<>();
    }

    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    criarInstrucaoDinamica.setWhere(CamposUsuario.ID.getCampoUsuario(), IGUAL, VAZIO,
        Long.parseLong(id), Types.BIGINT);
    try (Connection connection = Conexao.getConnection()) {
      UsuarioDAO dao = new UsuarioDAO();

      List<Map<String, Object>> resultados = dao.readAll(criarInstrucaoDinamica, connection);

      if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
        for (Map<String, Object> registro : resultados) {
          registro.remove("senha");
          registro.remove("nome");
          registro.remove("email");
        }
      } else {
        for (Map<String, Object> registro : resultados) {
          registro.remove("senha");
        }
      }

      return resultados;
    } catch (SQLException | IllegalArgumentException | ClassCastException exception) {
      return new ArrayList<>();
    }
  }

  //Métodos relacionados ao insert
  public List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject, Long idAdmin) {
    List<GenericExceptionEnum> mensagens = validarUsuarioInsert(jsonObject);
    if (mensagens.isEmpty()) {
      GenericExceptionEnum resultado = persistirUsuario(jsonObject, idAdmin);
      if (resultado != SUCESSO) {
        mensagens.add(resultado);
      }
    }
    return mensagens;
  }

  private List<GenericExceptionEnum> validarUsuarioInsert(JsonObject jsonObject) {
    List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

    UsuarioParaInsertDTO usuarioParaInsertDTO = GSON.fromJson(jsonObject,
        UsuarioParaInsertDTO.class);

    GenericExceptionEnum validacaoEmail = validarEmail(usuarioParaInsertDTO.email());
    if (validacaoEmail != VALIDACAO_OK) {
      listaDeErros.add(validacaoEmail);
    }

    GenericExceptionEnum validacaoSenha = validarSenha(usuarioParaInsertDTO.senha());
    if (validacaoSenha != VALIDACAO_OK) {
      listaDeErros.add(validacaoSenha);
    }

    GenericExceptionEnum validacaoRaioProcuraKm = validarRaioProcuraKm(
        usuarioParaInsertDTO.raioProcuraKm());
    if (validacaoRaioProcuraKm != VALIDACAO_OK && validacaoRaioProcuraKm != ATRIBUTO_NULL) {
      listaDeErros.add(validacaoRaioProcuraKm);
    }

    GenericExceptionEnum validacaoTipoUsuario = validarTipoUsuario(
        usuarioParaInsertDTO.tipoUsuario());
    if (validacaoTipoUsuario != VALIDACAO_OK) {
      listaDeErros.add(validacaoTipoUsuario);
    }

    GenericExceptionEnum validacaoNome = validarNome(usuarioParaInsertDTO.nome(),
        usuarioParaInsertDTO.tipoUsuario());
    if (validacaoNome != VALIDACAO_OK) {
      listaDeErros.add(validacaoNome);
    }

    if (usuarioParaInsertDTO.tipoUsuario() == null || usuarioParaInsertDTO.tipoUsuario()
        .isBlank()) {
      return listaDeErros;
    }

    if (TiposUsuario.PROFISSIONAL.getTipoDoUsuario()
        .equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario())) {
      listaDeErros.addAll(ProfissionalService.validarProfissionalInsert(
          GSON.fromJson(jsonObject, ProfissionalParaInsertDTO.class)));
    }

    if (TiposUsuario.FORNECEDOR.getTipoDoUsuario()
        .equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario())) {
      listaDeErros.addAll(FornecedorService.validarFornecedorInsert(
          GSON.fromJson(jsonObject, FornecedorParaInsertDTO.class)));
    }

    if (TiposUsuario.EMPRESA_DEMANDANTE.getTipoDoUsuario()
        .equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario())) {
      listaDeErros.addAll(EmpresaDemandanteService.validarEmpresaDemandanteInsert(
          GSON.fromJson(jsonObject, EmpresaDemandanteParaInsertDTO.class)));
    }

    return listaDeErros;
  }

  private GenericExceptionEnum persistirUsuario(JsonObject jsonObject, Long idAdmin) {
    UsuarioDAO dao = new UsuarioDAO();
    CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
    Usuario usuario = GSON.fromJson(jsonObject, UsuarioParaInsertDTO.class).construirUsuario();
    String tabelaFilho = null;

    try (Connection connection = Conexao.getConnection()) {
      connection.setAutoCommit(false);

      try {

        if (usuario.getRaioProcuraKm() != null) {
          criarInstrucaoDinamica.setCampo(CamposUsuario.RAIO_PROCURA_KM.getCampoUsuario(),
              usuario.getRaioProcuraKm(), Types.DOUBLE);
        }

        criarInstrucaoDinamica.setCampo(CamposUsuario.EMAIL.getCampoUsuario(), usuario.getEmail(),
            Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(CamposUsuario.SENHA.getCampoUsuario(), usuario.getSenha(),
            Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(CamposUsuario.TIPO_USUARIO.getCampoUsuario(),
            usuario.getTipoUsuario().getTipoDoUsuario(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(CamposUsuario.NOME.getCampoUsuario(), usuario.getNome(),
            Types.VARCHAR);

        CriarInstrucaoDinamica criarInstrucaoDinamicaCadastroFilha = new CriarInstrucaoDinamica();

        if (usuario.getTipoUsuario() == TiposUsuario.EMPRESA_DEMANDANTE) {
          EmpresaDemandante empresaDemandante = GSON.fromJson(jsonObject,
              EmpresaDemandanteParaInsertDTO.class).construirEmpresaDemandante();

          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposEmpresaDemandante.TIPO_USUARIO.getCamposEmpresaDemandante(),
              empresaDemandante.getTipoUsuario().getTipoDoUsuario(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposEmpresaDemandante.CNPJ.getCamposEmpresaDemandante(),
              empresaDemandante.getCnpj(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposEmpresaDemandante.RAZAO_SOCIAL.getCamposEmpresaDemandante(),
              empresaDemandante.getRazaoSocial(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposEmpresaDemandante.EH_MANDANTE.getCamposEmpresaDemandante(),
              empresaDemandante.isEhMandante(), Types.BOOLEAN);
          tabelaFilho = "empresa_demandante";
        } else if (usuario.getTipoUsuario() == TiposUsuario.FORNECEDOR) {
          Fornecedor fornecedor = GSON.fromJson(jsonObject, FornecedorParaInsertDTO.class)
              .construirFornecedor();

          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposFornecedor.TIPO_USUARIO.getCampoFornecedor(),
              fornecedor.getTipoUsuario().getTipoDoUsuario(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposFornecedor.TIPO_FORNECEDOR.getCampoFornecedor(),
              fornecedor.getTipoFornecedor().getTipoFornecedor(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(CamposFornecedor.CNPJ.getCampoFornecedor(),
              fornecedor.getCnpj(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposFornecedor.RAZAO_SOCIAL.getCampoFornecedor(), fornecedor.getRazaoSocial(),
              Types.VARCHAR);
          tabelaFilho = "fornecedor";
        } else if (usuario.getTipoUsuario() == TiposUsuario.PROFISSIONAL) {
          Profissional profissional = GSON.fromJson(jsonObject, ProfissionalParaInsertDTO.class)
              .construirProfissional();

          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposProfissional.TIPO_USUARIO.getCampoProfissional(),
              profissional.getTipoUsuario().getTipoDoUsuario(), Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposProfissional.PROFISSAO.getCampoProfissional(), profissional.getProfissao(),
              Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposProfissional.CPF_AES.getCampoProfissional(), profissional.getCpfAes(),
              Types.BINARY);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposProfissional.CPF_HMAC.getCampoProfissional(), profissional.getCpfHmac(),
              Types.VARCHAR);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposProfissional.ID_FORNECEDOR.getCampoProfissional(),
              profissional.getIdFornecedor(), Types.BIGINT);
          criarInstrucaoDinamicaCadastroFilha.setCampo(
              CamposProfissional.DOCUMENTO_CONTRATO.getCampoProfissional(),
              profissional.getDocumentoContrato(), Types.VARCHAR);
          tabelaFilho = "profissional";
        }

        Long idRegistro = dao.insert(criarInstrucaoDinamica, criarInstrucaoDinamicaCadastroFilha,
            tabelaFilho, connection);

        if (idRegistro == null) {
          throw new IllegalArgumentException("Ocorreu algum erro e o usuário não foi registrado!");
        }

        HistoricoAlteracoesAdminService.salvarAcao(
            new HistoricoAlteracoesAdmin(null, idAdmin, idRegistro, "USUARIO", "INSERT",
                new JsonObject(), jsonObject, null), connection);

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
