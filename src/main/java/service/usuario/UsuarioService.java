package service.usuario;


import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.REGISTROS_NAO_ENCONTRADOS;
import static exception.ErrosGerais.REGISTRO_NAO_ENCONTRADO;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGerais.descobrirErroGeral;
import static exception.ErrosGeraisDados.ATRIBUTO_NULL;
import static exception.ErrosGeraisDados.VALIDACAO_OK;
import static exception.ErrosGeraisDados.WHERE_INVALIDO;
import static service.ValidacoesComunsService.validarId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.CriarInstrucaoDinamica;
import dao.GenericDAO;
import dao.UsuarioDAO;
import exception.ErrosDadosUsuario;
import exception.ErrosGerais;
import exception.ErrosGeraisDados;
import exception.GenericExceptionEnum;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.imageio.plugins.tiff.TIFFTagSet;
import model.TiposUsuario;
import model.Usuario;
import service.ValidacoesComunsService;
import service.empresaDemandante.EmpresaDemandanteParaInsertDTO;
import service.empresaDemandante.EmpresaDemandanteService;
import service.fornecedor.FornecedorParaInsertDTO;
import service.fornecedor.FornecedorService;
import service.profissional.ProfissionalParaInsertDTO;
import service.profissional.ProfissionalService;

public final class UsuarioService {

    //Atributo usado para operações json no código
    private static final Gson GSON = new Gson();

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
    private static GenericExceptionEnum validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return ErrosGeraisDados.EMAIL_VAZIO;
        }

        String emailTratado = email.toLowerCase().strip();
        if(emailTratado.length() > TAMANHO_MAXIMO_EMAIL){
            return ErrosGeraisDados.EMAIL_TAMANHO_INVALIDO;
        }
        return validarFormatoEmail(emailTratado) != VALIDACAO_OK ? ErrosGeraisDados.EMAIL_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarFormatoEmail(String email){
        return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : ErrosGeraisDados.EMAIL_INVALIDO;
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
            return ErrosGeraisDados.SENHA_VAZIA;
        }

        String senhaTrim = senha.strip();
        if (senhaTrim.length() < TAMANHO_MINIMO_SENHA){
            return ErrosGeraisDados.SENHA_MENOR_QUE_OITO;
        }

        if (senhaTrim.length() > TAMANHO_MAXIMO_SENHA){
            return ErrosGeraisDados.SENHA_TAMANHO_INVALIDO;
        }

        return validarFormatoSenha(senhaTrim);
    }

    private static GenericExceptionEnum validarFormatoSenha(String senha){
        return PATTERN_SENHA.matcher(senha).matches() ? VALIDACAO_OK : ErrosGeraisDados.SENHA_FRACA;
    }

    //Validações do nome
    private static GenericExceptionEnum validarNome(String nome, String tipoUsuario){
        if (nome == null || nome.isBlank()){
            return ErrosDadosUsuario.NOME_VAZIO;
        }

        String nomeTrim = nome.strip();
        if (nomeTrim.length() > TAMANHO_MAXIMO_NOME){
            return ErrosDadosUsuario.NOME_TAMANHO_INVALIDO;
        }

        if(nomeTrim.toLowerCase().replace(nomeTrim.charAt(0), ' ').isBlank()){
            return ErrosDadosUsuario.NOME_INVALIDO;
        }

        TiposUsuario tiposUsuarioInserido = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
        return TiposUsuario.PROFISSIONAL == tiposUsuarioInserido ? validarNomePessoaFisica(nomeTrim) : validarNomePessoaJuridica(nomeTrim);
    }

    private static GenericExceptionEnum validarNomePessoaFisica(String nome){
        return PATTERN_PESSOA_FISICA.matcher(nome).matches() ? VALIDACAO_OK : ErrosDadosUsuario.NOME_INVALIDO;
    }

    private static GenericExceptionEnum validarNomePessoaJuridica(String nome){
        return PATTERN_PESSOA_JURIDICA.matcher(nome).matches() ? VALIDACAO_OK : ErrosDadosUsuario.NOME_INVALIDO;
    }

    //Validação do tipo do usuario
    private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario){
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            return ErrosDadosUsuario.TIPO_USUARIO_VAZIO;
        }

        if (TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip()) == null){
            return ErrosDadosUsuario.TIPO_USUARIO_INVALIDO;
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
            return ErrosDadosUsuario.RAIO_PROCURA_KM_NAO_NUMERICO;
        }

        if (raioProcuraKmConvertido <= 0){
            return ErrosDadosUsuario.RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO;
        }

        if (raioProcuraKmConvertido > TAMANHO_MAXIMO_RAIO_PROCURA_KM){
            return ErrosDadosUsuario.RAIO_PROCURA_KM_TAMANHO_INVALIDO;
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
//    public GenericExceptionEnum realizarDelete(String id){
//        if(validarId(id) != VALIDACAO_OK) {
//            return ERRO_GENERICO;
//        }
//
//        GenericExceptionEnum resultado = deletarUsuario(id);
//
//
//        return resultado == SUCESSO ? SUCESSO : SUCESSO; //descobrirErroGeral(resultado);
//    }
//
//    private  GenericExceptionEnum deletarUsuario(String id) throws SQLException {
//        UsuarioDAO dao = new UsuarioDAO();
//
//        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
//
//        criarInstrucaoDinamica.setCampo("id", id, Types.BIGINT);
//        return dao.delete(criarInstrucaoDinamica, null);
//    }

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


    private static List<Usuario> lerUsuarios(UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDTO, boolean erroEncontrado){
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

//        CamposUsuario clausulaWhere = CamposUsuario.descobrirCampoUsuario(clausulaWhereNome.strip().toLowerCase());
//        if (clausulaWhere == GENERICO){
//            return errosNasClausulasWhere;
//        }
//
//        if (clausulaWhere == ID){
//            clausulaWhereValorValidacao = validarId(clausulaWhereValor);
//        }
//
//        if (clausulaWhere == EMAIL){
//            clausulaWhereValorValidacao = validarEmailBasico(clausulaWhereValor);
//        }
//
//        if (clausulaWhere == NOME){
//            clausulaWhereValorValidacao = validarNome(clausulaWhereValor, TiposUsuario.EMPRESA_DEMANDANTE.getTipoDoUsuario());
//        }
//
//        if (clausulaWhere == TIPO_USUARIO){
//            clausulaWhereValorValidacao = validarTipoUsuario(clausulaWhereValor);
//        }
//
//        GenericExceptionEnum clausulaWhereValor2Validacao;
//        if (clausulaWhere == RAIO_PROCURA_KM){
//            clausulaWhereValorValidacao = validarRaioProcuraKm(clausulaWhereValor);
//            clausulaWhereValor2Validacao = validarRaioProcuraKm(clausulaWhereValor2);
//            if(clausulaWhereValorValidacao != VALIDACAO_OK || clausulaWhereValor2Validacao != VALIDACAO_OK){
//                errosNasClausulasWhere.add(RAIOS_PROCURA_KM_NAO_NUMERICO);
//                return errosNasClausulasWhere;
//            }
//        }
//
//        if (clausulaWhereValorValidacao == null){
//            clausulaWhereValorValidacao = WHERE_INVALIDO;
//        }
//
//        if (clausulaWhereValorValidacao != VALIDACAO_OK){
//            errosNasClausulasWhere.add(clausulaWhereValorValidacao);
//        }
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

    private static int atualizarUsuario(UsuarioParaInsertDTO usuarioParaInsertDTO){
        UsuarioDAO dao = new UsuarioDAO();
//        return dao.updateById(usuarioParaInsertDTO.construirUsuario());

        return 0;
    }

    private static List<GenericExceptionEnum> validarUpdate(UsuarioParaInsertDTO usuarioParaInsertDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum emailValidacao = validarEmail(usuarioParaInsertDTO.email());
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
//        return dao.readById(Long.parseLong(id));
        return new Usuario(null, null, null, null, null, null);
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(JsonObject jsonObject){
        List<GenericExceptionEnum> mensagens = validarUsuarioInsert(jsonObject);
        if (mensagens.isEmpty()) {
            GenericExceptionEnum resultado = persistirUsuario(jsonObject);
            if (resultado != SUCESSO) {
                mensagens.add(resultado);
            }
        }
        return mensagens;
    }

    private static List<GenericExceptionEnum> validarUsuarioInsert(JsonObject jsonObject){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        UsuarioParaInsertDTO usuarioParaInsertDTO = GSON.fromJson(jsonObject, UsuarioParaInsertDTO.class);

        GenericExceptionEnum validacaoEmail = validarEmail(usuarioParaInsertDTO.email());
        if (validacaoEmail != VALIDACAO_OK)
            listaDeErros.add(validacaoEmail);

        GenericExceptionEnum validacaoSenha = validarSenha(usuarioParaInsertDTO.senha());
        if(validacaoSenha != VALIDACAO_OK)
            listaDeErros.add(validacaoSenha);

        GenericExceptionEnum validacaoRaioProcuraKm = validarRaioProcuraKm(usuarioParaInsertDTO.raioProcuraKm());
        if(validacaoRaioProcuraKm != VALIDACAO_OK && validacaoRaioProcuraKm != ATRIBUTO_NULL)
            listaDeErros.add(validacaoRaioProcuraKm);


        GenericExceptionEnum validacaoTipoUsuario = validarTipoUsuario(usuarioParaInsertDTO.tipoUsuario());
        if(validacaoTipoUsuario != VALIDACAO_OK)
            listaDeErros.add(validacaoTipoUsuario);


        GenericExceptionEnum validacaoNome = validarNome(usuarioParaInsertDTO.nome(), usuarioParaInsertDTO.tipoUsuario());
        if(validacaoNome != VALIDACAO_OK)
            listaDeErros.add(validacaoNome);

        if(usuarioParaInsertDTO.tipoUsuario() == null || usuarioParaInsertDTO.tipoUsuario().isBlank())
            return listaDeErros;

        if(TiposUsuario.PROFISSIONAL.getTipoDoUsuario().equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario()))
            listaDeErros.addAll(ProfissionalService.validarProfissionalInsert(GSON.fromJson(jsonObject, ProfissionalParaInsertDTO.class)));


        if(TiposUsuario.FORNECEDOR.getTipoDoUsuario().equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario()))
            listaDeErros.addAll(FornecedorService.validarFornecedorInsert(GSON.fromJson(jsonObject, FornecedorParaInsertDTO.class)));

        if(TiposUsuario.EMPRESA_DEMANDANTE.getTipoDoUsuario().equalsIgnoreCase(usuarioParaInsertDTO.tipoUsuario()))
            listaDeErros.addAll(EmpresaDemandanteService.validarEmpresaDemandanteInsert(GSON.fromJson(jsonObject, EmpresaDemandanteParaInsertDTO.class)));

        return listaDeErros;
    }

    private static GenericExceptionEnum persistirUsuario(JsonObject jsonObject){
        UsuarioDAO dao = new UsuarioDAO();
        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();
        Usuario usuario = GSON.fromJson(jsonObject, Usuario.class);

        if(usuario.getRaioProcuraKm() != null)
            criarInstrucaoDinamica.setCampo(CamposUsuario.RAIO_PROCURA_KM.getCampoUsuario(), usuario.getRaioProcuraKm(), Types.DOUBLE);

        criarInstrucaoDinamica.setCampo(CamposUsuario.EMAIL.getCampoUsuario(), usuario.getEmail(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(CamposUsuario.SENHA.getCampoUsuario(), usuario.getSenha(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(CamposUsuario.TIPO_USUARIO.getCampoUsuario(), usuario.getTipoUsuario().getTipoDoUsuario(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo(CamposUsuario.NOME.getCampoUsuario(), usuario.getNome(), Types.VARCHAR);

        CriarInstrucaoDinamica criarInstrucaoDinamicaCadastroFilha = new CriarInstrucaoDinamica();

        if(usuario.getTipoUsuario() == TiposUsuario.EMPRESA_DEMANDANTE){
            
        }

        return SUCESSO;//dao.insert(criarInstrucaoDinamica);
    }



}
