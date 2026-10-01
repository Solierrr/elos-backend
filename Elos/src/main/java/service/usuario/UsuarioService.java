package service.usuario;

import static exception.ErrosDadosUsuario.EMAIL_INVALIDO;
import static exception.ErrosDadosUsuario.EMAIL_TAMANHO_INVALIDO;
import static exception.ErrosDadosUsuario.EMAIL_VAZIO;
import static exception.ErrosDadosUsuario.IMPOSSIVEL_VALIDAR_NOME;
import static exception.ErrosDadosUsuario.NOME_INVALIDO;
import static exception.ErrosDadosUsuario.NOME_TAMANHO_INVALIDO;
import static exception.ErrosDadosUsuario.NOME_VAZIO;
import static exception.ErrosDadosUsuario.RAIOS_PROCURA_KM_NAO_NUMERICO;
import static exception.ErrosDadosUsuario.RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO;
import static exception.ErrosDadosUsuario.RAIO_PROCURA_KM_NAO_NUMERICO;
import static exception.ErrosDadosUsuario.RAIO_PROCURA_KM_TAMANHO_INVALIDO;
import static exception.ErrosDadosUsuario.SENHA_FRACA;
import static exception.ErrosDadosUsuario.SENHA_MENOR_QUE_OITO;
import static exception.ErrosDadosUsuario.SENHA_TAMANHO_INVALIDO;
import static exception.ErrosDadosUsuario.SENHA_VAZIA;
import static exception.ErrosDadosUsuario.TIPO_USUARIO_INVALIDO;
import static exception.ErrosDadosUsuario.TIPO_USUARIO_VAZIO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.REGISTROS_NAO_ENCONTRADOS;
import static exception.ErrosGerais.REGISTRO_NAO_ENCONTRADO;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGerais.descobrirErroGeral;
import static exception.ErrosGeraisDados.ATRIBUTO_NULL;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static exception.ErrosGeraisDados.WHERE_INVALIDO;
import static service.ValidacoesComunsService.validarId;
import static service.usuario.CamposUsuario.EMAIL;
import static service.usuario.CamposUsuario.GENERICO;
import static service.usuario.CamposUsuario.ID;
import static service.usuario.CamposUsuario.NOME;
import static service.usuario.CamposUsuario.RAIO_PROCURA_KM;
import static service.usuario.CamposUsuario.TIPO_USUARIO;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.CriarInstrucaoDinamica;
import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import model.TiposUsuario;
import model.Usuario;
import service.ValidacoesComunsService;
import service.profissional.ProfissionalService;

public final class UsuarioService {

    //Atributo usado para operações json no código
    private static Gson GSON = new Gson();

    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
    private static final double TAMANHO_MAXIMO_RAIO_PROCURA_KM = 999.99;

    private static final int TAMANHO_MAXIMO_NOME = 150;

    private static final Pattern PATTERN_PESSOA_FISICA = Pattern.compile("^[\\p{Script=Latin}']+[\\p{Script=Latin}'\\s\\-]+$");
    private static final Pattern PATTERN_PESSOA_JURIDICA = Pattern.compile("^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

    private static final int TAMANHO_MINIMO_SENHA = 8;
    private static final int TAMANHO_MAXIMO_SENHA = 60;
    private static final Pattern PATTERN_SENHA = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    private static final int TAMANHO_MAXIMO_EMAIL = 256;

    //Validações

    //Validações do email
    private static GenericExceptionEnum validarEmailBasico(String email) {
        if (email == null || email.isBlank()) {
            return EMAIL_VAZIO;
        }

        String emailTratado = email.toLowerCase().strip();
        if(emailTratado.length() > TAMANHO_MAXIMO_EMAIL){
            return EMAIL_TAMANHO_INVALIDO;
        }
        return validarFormatoEmail(emailTratado) != VALIDACAO_OK ? EMAIL_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarFormatoEmail(String email){
        return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : EMAIL_INVALIDO;
    }

    //Validações da senha
    private static GenericExceptionEnum validarSenhaUpdate(String senha){
        if (senha == null || senha.isBlank()){
            return VALIDACAO_OK;
        }

        String senhaTrim = senha.strip();
        return validarSenha(senhaTrim);
    }

    private static GenericExceptionEnum validarSenha(String senha){
        if (senha == null || senha.isBlank()){
            return SENHA_VAZIA;
        }

        String senhaTrim = senha.strip();
        if (senhaTrim.length() < TAMANHO_MINIMO_SENHA){
            return SENHA_MENOR_QUE_OITO;
        }

        if (senhaTrim.length() > TAMANHO_MAXIMO_SENHA){
            return SENHA_TAMANHO_INVALIDO;
        }

        return validarFormatoSenha(senhaTrim);
    }

    private static GenericExceptionEnum validarFormatoSenha(String senha){
        return PATTERN_SENHA.matcher(senha).matches() ? VALIDACAO_OK : SENHA_FRACA;
    }

    //Validações do nome
    private static GenericExceptionEnum validarNome(String nome, String tipoUsuario){
        if (nome == null || nome.isBlank()){
            return NOME_VAZIO;
        }

        String nomeTrim = nome.strip();
        if (nomeTrim.length() > TAMANHO_MAXIMO_NOME){
            return NOME_TAMANHO_INVALIDO;
        }

        if(nomeTrim.toLowerCase().replace(nomeTrim.charAt(0), ' ').isBlank()){
            return NOME_INVALIDO;
        }

        TiposUsuario tiposUsuarioInserido = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
        return TiposUsuario.PROFISSIONAL == tiposUsuarioInserido ? validarNomePessoaFisica(nomeTrim) : validarNomePessoaJuridica(nomeTrim);
    }

    private static GenericExceptionEnum validarNomePessoaFisica(String nome){
        return PATTERN_PESSOA_FISICA.matcher(nome).matches() ? VALIDACAO_OK : NOME_INVALIDO;
    }

    private static GenericExceptionEnum validarNomePessoaJuridica(String nome){
        return PATTERN_PESSOA_JURIDICA.matcher(nome).matches() ? VALIDACAO_OK : NOME_INVALIDO;
    }

    //Validação do tipo do usuario
    private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario){
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            return TIPO_USUARIO_VAZIO;
        }

        if (TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip()) == null){
            return TIPO_USUARIO_INVALIDO;
        }
        return VALIDACAO_OK;
    }

    //Validações do raio de procura em km
    private static GenericExceptionEnum validarRaioProcuraKm(String raioProcuraKm){
        if (raioProcuraKm == null || raioProcuraKm.isBlank()){
            return ATRIBUTO_NULL;
        }

        Double raioProcuraKmConvertido = validarRaioProcuraKmConversivel(raioProcuraKm);
        if (raioProcuraKmConvertido == null){
            return RAIO_PROCURA_KM_NAO_NUMERICO;
        }

        if (raioProcuraKmConvertido <= 0){
            return RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO;
        }

        if (raioProcuraKmConvertido > TAMANHO_MAXIMO_RAIO_PROCURA_KM){
            return RAIO_PROCURA_KM_TAMANHO_INVALIDO;
        }

        return VALIDACAO_OK;
    }

    private static Double validarRaioProcuraKmConversivel(String raioProcuraKm){
        try {
            String raioProcuraKmTrim = raioProcuraKm.strip();
            return Double.parseDouble(raioProcuraKmTrim);
        }catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    //Métodos relacionados ao delete
    public static GenericExceptionEnum realizarDelete(String id){
        if(validarId(id) != VALIDACAO_OK) {
            return ERRO_GENERICO;
        }

        int qtdLinhasDeletadas = deletarUsuario(id);
        if(qtdLinhasDeletadas > 0){
            return SUCESSO;
        }
        return descobrirErroGeral(qtdLinhasDeletadas);
    }

    private static int deletarUsuario(String id){
        UsuarioDAO dao = new UsuarioDAO();

        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

        criarInstrucaoDinamica.setCampo("id", id, Types.BIGINT);
        return dao.deleteById(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static List<Usuario> realizarSelect(UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDTO, List<GenericExceptionEnum> errosEncontrados){
        List<Usuario> usuarios;

        if(usuarioDadosDePesquisaDTO == null){
            return lerUsuarios(null, true);
        }

        errosEncontrados.addAll(validarUsuarioSelect(usuarioDadosDePesquisaDTO));
        if (!errosEncontrados.isEmpty()){
            return lerUsuarios(usuarioDadosDePesquisaDTO,true);
        }

        usuarios = lerUsuarios(usuarioDadosDePesquisaDTO,false);
        if (usuarios.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return usuarios;
    }

//    private static List<Usuario> lerUsuariosTeste(UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDto, boolean erroEncontrado){
//        UsuarioDAO dao = new UsuarioDAO();
//
//        if (erroEncontrado){
//            return dao.readAllTeste(new CriarInstrucaoDinamica());
//        }
//
//        CamposUsuario clausulaWhere = CamposUsuario.descobrirCampoUsuario(usuarioDadosDePesquisaDto.clausulaWhereNome().strip().toLowerCase());
//        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
//
//        if(clausulaWhere.getAcao() == BETWEEN){
//            List<Object> valores = new ArrayList<>();
//
//            valores.add(usuarioDadosDePesquisaDto.clausulaWhereValor());
//            valores.add(usuarioDadosDePesquisaDto.clausulaWhereValor2());
//
//            criarInstrucaoDinamica.setWhereMultiplosValores(clausulaWhere.getCampoUsuario(), clausulaWhere.getAcao().getAcao(), " ", valores, clausulaWhere.getDataType());
//        }else {
//            criarInstrucaoDinamica.setWhere(clausulaWhere.getCampoUsuario(), clausulaWhere.getAcao().getAcao(), " ", usuarioDadosDePesquisaDto.clausulaWhereValor(), clausulaWhere.getDataType());
//        }
//
//        criarInstrucaoDinamica.setOrderBy(usuarioDadosDePesquisaDto.orderBy(), usuarioDadosDePesquisaDto.sentidoOrderBy());
//
//        return dao.readAllTeste(criarInstrucaoDinamica);
//    }

    private static List<Usuario> lerUsuarios(UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDTO, boolean erroEncontrado){
        if (erroEncontrado){
            UsuarioDAO dao = new UsuarioDAO();
            return dao.readAll();
        }

        CamposUsuario clausulaWhere = CamposUsuario.descobrirCampoUsuario(usuarioDadosDePesquisaDTO.clausulaWhereNome().strip().toLowerCase());
        if(!clausulaWhere.isAcessivel()){
            Usuario usuario = lerUsuarioUnicoRetorno(clausulaWhere, usuarioDadosDePesquisaDTO.clausulaWhereValor());

            List<Usuario> usuarios = new ArrayList<>();

            if (usuario.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
                usuarios.add(usuario);
            }
            return usuarios;
        }
        return lerUsuarioMultiplosRetornos(clausulaWhere, usuarioDadosDePesquisaDTO);
    }

    private static Usuario lerUsuarioUnicoRetorno(CamposUsuario clausulaWhere, String clausulaWhereValor){
        UsuarioDAO dao = new UsuarioDAO();

        if (clausulaWhere == ID){
            long id = Long.parseLong(clausulaWhereValor.strip());
            return dao.readById(id);
        }

        if (clausulaWhere == EMAIL){
            String emailTratado = clausulaWhereValor.toLowerCase().strip();
            return dao.readByEmail(emailTratado);
        }
        return new Usuario(REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null, null, REGISTRO_NAO_ENCONTRADO.getCodigo());
    }

    private static List<Usuario> lerUsuarioMultiplosRetornos(CamposUsuario clausulaWhere, UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDTO){
        CamposUsuario orderBy = CamposUsuario.descobrirCampoUsuario(usuarioDadosDePesquisaDTO.orderBy().strip().toLowerCase());

        UsuarioDAO dao = new UsuarioDAO();
        if (clausulaWhere == GENERICO){
            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoUsuario(), usuarioDadosDePesquisaDTO.sentidoOrderBy());
        }

        if (clausulaWhere == NOME){
            String nomeTratado = usuarioDadosDePesquisaDTO.clausulaWhereValor().strip();
            return orderBy == GENERICO ? dao.readAllByNome(nomeTratado) :
                    dao.readAllByNomeOrderBy(nomeTratado, orderBy.getCampoUsuario(), usuarioDadosDePesquisaDTO.sentidoOrderBy());
        }

        if(clausulaWhere == TIPO_USUARIO){
            String tipoUsuarioTratado = usuarioDadosDePesquisaDTO.clausulaWhereValor().toUpperCase();
            return orderBy == GENERICO ?
                    dao.readAllByTipoUsuario(tipoUsuarioTratado) :
                    dao.readAllByTipoUsuarioOrderBy(tipoUsuarioTratado, orderBy.getCampoUsuario(), usuarioDadosDePesquisaDTO.sentidoOrderBy());
        }

        if(clausulaWhere == RAIO_PROCURA_KM){
            double raioProcuraKmMin = Double.parseDouble(usuarioDadosDePesquisaDTO.clausulaWhereValor().strip());
            double raioProcuraKmMax = Double.parseDouble(usuarioDadosDePesquisaDTO.clausulaWhereValor2().strip());
            return orderBy == GENERICO ?
                    dao.readAllWhereRaioProcuraKmEntre(raioProcuraKmMin, raioProcuraKmMax) :
                    dao.readAllWhereRaioProcuraKmEntreOrderBy(raioProcuraKmMin, raioProcuraKmMax, orderBy.getCampoUsuario(), usuarioDadosDePesquisaDTO.sentidoOrderBy());
        }

        return new ArrayList<>();
    }

    private static List<GenericExceptionEnum> validarUsuarioSelect(UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        CamposUsuario clausulaWhere = CamposUsuario.descobrirCampoUsuario(usuarioDadosDePesquisaDTO.clausulaWhereNome());
        GenericExceptionEnum clausulaWhereNomeValidacao = ValidacoesComunsService.validarWhere(clausulaWhere);
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(usuarioDadosDePesquisaDTO.clausulaWhereNome(), usuarioDadosDePesquisaDTO.clausulaWhereValor(), usuarioDadosDePesquisaDTO.clausulaWhereValor2()));

        CamposUsuario orderBy = CamposUsuario.descobrirCampoUsuario(usuarioDadosDePesquisaDTO.orderBy());
        GenericExceptionEnum orderByValidacao = ValidacoesComunsService.validarOrderBy(orderBy);
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = ValidacoesComunsService.validarSentidoOrderBy(usuarioDadosDePesquisaDTO.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static List<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor, String clausulaWhereValor2){
        List<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposUsuario clausulaWhere = CamposUsuario.descobrirCampoUsuario(clausulaWhereNome.strip().toLowerCase());
        if (clausulaWhere == GENERICO){
            return errosNasClausulasWhere;
        }

        if (clausulaWhere == ID){
            clausulaWhereValorValidacao = validarId(clausulaWhereValor);
        }

        if (clausulaWhere == EMAIL){
            clausulaWhereValorValidacao = validarEmailBasico(clausulaWhereValor);
        }

        if (clausulaWhere == NOME){
            clausulaWhereValorValidacao = validarNome(clausulaWhereValor, TiposUsuario.EMPRESA_DEMANDANTE.getTipoDoUsuario());
        }

        if (clausulaWhere == TIPO_USUARIO){
            clausulaWhereValorValidacao = validarTipoUsuario(clausulaWhereValor);
        }

        GenericExceptionEnum clausulaWhereValor2Validacao;
        if (clausulaWhere == RAIO_PROCURA_KM){
            clausulaWhereValorValidacao = validarRaioProcuraKm(clausulaWhereValor);
            clausulaWhereValor2Validacao = validarRaioProcuraKm(clausulaWhereValor2);
            if(clausulaWhereValorValidacao != VALIDACAO_OK || clausulaWhereValor2Validacao != VALIDACAO_OK){
                errosNasClausulasWhere.add(RAIOS_PROCURA_KM_NAO_NUMERICO);
                return errosNasClausulasWhere;
            }
        }

        if (clausulaWhereValorValidacao == null){
            clausulaWhereValorValidacao = WHERE_INVALIDO;
        }

        if (clausulaWhereValorValidacao != VALIDACAO_OK){
            errosNasClausulasWhere.add(clausulaWhereValorValidacao);
        }
        return errosNasClausulasWhere;
    }

    //Métodos relacionados ao update
    public static List<GenericExceptionEnum> realizarUpdate(UsuarioParaInsertDTO usuarioParaInsertDTO){
        List<GenericExceptionEnum> erros = validarUpdate(usuarioParaInsertDTO);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarUsuario(usuarioParaInsertDTO);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

//    private static int atualizarUsuarioTeste(UsuarioDadosDTO usuarioDadosDto){
//        UsuarioDAO dao = new UsuarioDAO();
//        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
//
//        criarInstrucaoDinamica.setCampo(EMAIL.getCampoUsuario(), usuarioDadosDto.email(), Types.VARCHAR);
//        criarInstrucaoDinamica.setCampo(NOME.getCampoUsuario(), usuarioDadosDto.nome(), Types.VARCHAR);
//        criarInstrucaoDinamica.setCampo(SENHA.getCampoUsuario(), usuarioDadosDto.senha(), Types.VARCHAR);
//        criarInstrucaoDinamica.setCampo(RAIO_PROCURA_KM.getCampoUsuario(), usuarioDadosDto.raioProcuraKm(), Types.DOUBLE);
//        criarInstrucaoDinamica.setWhere(ID.getCampoUsuario(), IGUAL.getAcao(), VAZIO.getAcao(), usuarioDadosDto.id(), Types.BIGINT);
//
//        return dao.updateByIdTeste(criarInstrucaoDinamica);
//    }

    private static int atualizarUsuario(UsuarioParaInsertDTO usuarioParaInsertDTO){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.updateById(usuarioParaInsertDTO.construirUsuario());
    }

    private static List<GenericExceptionEnum> validarUpdate(UsuarioParaInsertDTO usuarioParaInsertDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum emailValidacao = validarEmailBasico(usuarioParaInsertDTO.email());
        if(emailValidacao != VALIDACAO_OK){
            erros.add(emailValidacao);
        }

        GenericExceptionEnum senhaValidacao = validarSenhaUpdate(usuarioParaInsertDTO.senha());
        if (senhaValidacao != VALIDACAO_OK){
            erros.add(senhaValidacao);
        }

        GenericExceptionEnum nomeValidacao = validarNome(usuarioParaInsertDTO.nome(), usuarioParaInsertDTO.tipoUsuario());
        if (nomeValidacao != VALIDACAO_OK){
            erros.add(nomeValidacao);
        }

        GenericExceptionEnum raioProcuraKmValidacao = validarRaioProcuraKm(usuarioParaInsertDTO.raioProcuraKm());
        if(raioProcuraKmValidacao != VALIDACAO_OK){
            erros.add(raioProcuraKmValidacao);
        }
        return erros;
    }

    public static Usuario exibirUsuarioParaUpdate(String id){
        if(validarId(id) != VALIDACAO_OK){
            return null;
        }

        UsuarioDAO dao = new UsuarioDAO();
        return dao.readById(Long.parseLong(id));
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject){
        List<GenericExceptionEnum> mensagens = validarUsuarioInsert(jsonObject);
        if (mensagens.isEmpty()) {
            int resultado = persistirUsuario(usuarioParaInsertDTO);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static List<GenericExceptionEnum> validarUsuarioInsert(JsonObject jsonObject){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        UsuarioParaInsertDTO usuarioParaInsertDTO = GSON.fromJson(jsonObject, UsuarioParaInsertDTO.class);

        GenericExceptionEnum validacaoEmail = validarEmailInsert(usuarioParaInsertDTO.email());
        if (validacaoEmail != VALIDACAO_OK){
            listaDeErros.add(validacaoEmail);
        }

        GenericExceptionEnum validacaoSenha = validarSenha(usuarioParaInsertDTO.senha());
        if(validacaoSenha != VALIDACAO_OK){
            listaDeErros.add(validacaoSenha);
        }

        GenericExceptionEnum validacaoRaioProcuraKm = validarRaioProcuraKm(usuarioParaInsertDTO.raioProcuraKm());
        if(validacaoRaioProcuraKm != VALIDACAO_OK && validacaoRaioProcuraKm != ATRIBUTO_NULL){
            listaDeErros.add(validacaoRaioProcuraKm);
        }

        GenericExceptionEnum validacaoTipoUsuario = validarTipoUsuario(usuarioParaInsertDTO.tipoUsuario());
        if(validacaoTipoUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoUsuario);
            listaDeErros.add(IMPOSSIVEL_VALIDAR_NOME);
            return listaDeErros;
        }

        GenericExceptionEnum validacaoNome = validarNome(usuarioParaInsertDTO.nome(), usuarioParaInsertDTO.tipoUsuario());
        if(validacaoNome != VALIDACAO_OK){
            listaDeErros.add(validacaoNome);
        }

        if(TiposUsuario.PROFISSIONAL.getTipoDoUsuario().equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario())){
            listaDeErros.addAll(ProfissionalService.)
        }

        return listaDeErros;
    }

//    private static int persistirUsuarioTeste(UsuarioDadosDTO usuarioDadosDto){
//        UsuarioDAO dao = new UsuarioDAO();
//        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
//        Usuario usuario = usuarioDadosDto.construirUsuario();
//
//        if(usuario.getRaioProcuraKm() != ATRIBUTO_NULL.getCodigo()){
//            criarInstrucaoDinamica.setCampo(RAIO_PROCURA_KM.getCampoUsuario(), usuario.getRaioProcuraKm(), Types.DOUBLE);
//        }
//        criarInstrucaoDinamica.setCampo(EMAIL.getCampoUsuario(), usuario.getEmail(), Types.VARCHAR);
//        criarInstrucaoDinamica.setCampo(SENHA.getCampoUsuario(), usuario.getSenha(), Types.VARCHAR);
//        criarInstrucaoDinamica.setCampo(TIPO_USUARIO.getCampoUsuario(), usuario.getTipoUsuario().getTipoDoUsuario(), Types.VARCHAR);
//        criarInstrucaoDinamica.setCampo(NOME.getCampoUsuario(), usuario.getNome(), Types.VARCHAR);
//
//        return dao.insert(usuarioDadosDto.construirUsuario());
//    }

    private static int persistirUsuario(UsuarioParaInsertDTO usuarioParaInsertDTO){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.insert(usuarioParaInsertDTO.construirUsuario());
    }

}
