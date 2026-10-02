package service.usuario;

import static exception.ErrosGerais.*;
import static exception.ErrosDadosUsuario.*;
import static exception.ErrosGeraisDados.*;
import static service.usuario.CamposUsuarioAcessiveis.*;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import model.TiposUsuario;
import model.Usuario;

public class UsuarioService {

    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
    private static final double TAMANHO_MAXIMO_RAIO_PROCURA_KM = 999.99;

    private static final int TAMANHO_MAXIMO_NOME = 150;

    private static final Pattern PATTERN_PESSOA_FISICA = Pattern.compile("^[\\p{Script=Latin}']+[\\p{Script=Latin}'\\s\\-]+$");
    private static final Pattern PATTERN_PESSOA_JURIDICA = Pattern.compile("^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

    private static final int TAMANHO_MINIMO_SENHA = 8;
    private static final int TAMANHO_MAXIMO_SENHA = 60;
    private static final Pattern PATTERN_SENHA = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    private static final int TAMANHO_MAXIMO_EMAIL = 150;

    //Validação
    private static GenericExceptionEnum validarSentidoOrderBy(String sentidoOrderBy){
        if (sentidoOrderBy == null || sentidoOrderBy.isBlank()){return SENTIDO_ORDER_BY_INVALIDO;}

        String ordenacaoTratada = sentidoOrderBy.strip().toLowerCase();
        switch(ordenacaoTratada) {
            case "asc", "desc":
                return VALIDACAO_OK;
            default:
                return SENTIDO_ORDER_BY_INVALIDO;
        }
    }

    private static GenericExceptionEnum validarWhere(String where){
        if (where == null || where.isBlank()){return WHERE_INVALIDO;}

        CamposUsuarioAcessiveis campoDoWhere = CamposUsuarioAcessiveis.descobrirCampoUsuario(where.strip().toLowerCase());
        return campoDoWhere == INVALIDO ? WHERE_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarOrderBy(String ordenacao){
        if (ordenacao == null || ordenacao.isBlank()){return ORDER_BY_INVALIDO;}

        CamposUsuarioAcessiveis ordenacaoCampo = CamposUsuarioAcessiveis.descobrirCampoUsuario(ordenacao.strip().toLowerCase());
        return ordenacaoCampo == INVALIDO ? ORDER_BY_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarId(String id){
        try {
            String idTratado = id.strip();
            Long.parseLong(idTratado);
            return VALIDACAO_OK;
        } catch (NumberFormatException numberFormatException){
            return ID_INVALIDO;
        }
    }

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

    private static GenericExceptionEnum validarEmailInsert(String email) {
        GenericExceptionEnum validarEmailBasico = validarFormatoEmail(email);
        if(validarEmailBasico != VALIDACAO_OK){
            return validarEmailBasico;
        }

        String emailTratado = email.toLowerCase().strip();
        return validarEmailNaoCadastrado(emailTratado) != VALIDACAO_OK ? EMAIL_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarFormatoEmail(String email){
        return PATTERN_EMAIL.matcher(email).matches() ? VALIDACAO_OK : EMAIL_INVALIDO;
    }

    private static GenericExceptionEnum validarEmailNaoCadastrado(String email){
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.readByEmail(email);
        if(usuario != null && usuario.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return EMAIL_INVALIDO;
        }
        return VALIDACAO_OK;
    }

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

        Matcher matcher = PATTERN_SENHA.matcher(senhaTrim);
        return matcher.matches() ? VALIDACAO_OK : SENHA_FRACA;
    }

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

        boolean nomeValido = TiposUsuario.PROFISSIONAL == tiposUsuarioInserido ?
                                                PATTERN_PESSOA_FISICA.matcher(nomeTrim).matches() :
                                                PATTERN_PESSOA_JURIDICA.matcher(nomeTrim).matches();

        return nomeValido ? VALIDACAO_OK : NOME_INVALIDO;
    }

    private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario){
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            return TIPO_USUARIO_VAZIO;
        }

        if (TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().strip()) == null){
            return TIPO_USUARIO_INVALIDO;
        }
        return VALIDACAO_OK;
    }

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
    public static ArrayList<GenericExceptionEnum> realizarDelete(String id){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();

        if(validarId(id) != VALIDACAO_OK) {
            erros.add(ERRO_GENERICO);
            return erros;
        }

        int qtdLinhasDeletadas = deletarUsuario(id);
        if(qtdLinhasDeletadas > 0){
            return erros;
        }

        erros.add(descobrirErroGeral(qtdLinhasDeletadas));
        return erros;
    }

    private static int deletarUsuario(String id){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.deleteById(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static ArrayList<Usuario> realizarSelect(UsuarioDadosDePesquisaDto usuarioDadosDePesquisaDto, ArrayList<GenericExceptionEnum> errosEncontrados){
        ArrayList<Usuario> usuarios;

        if(usuarioDadosDePesquisaDto == null){
            return lerUsuarios(null, true);
        }

        errosEncontrados.addAll(validarUsuarioSelect(usuarioDadosDePesquisaDto));
        if (!errosEncontrados.isEmpty()){
            return lerUsuarios(usuarioDadosDePesquisaDto,true);
        }

        usuarios = lerUsuarios(usuarioDadosDePesquisaDto,false);
        if (usuarios.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return usuarios;
    }

    private static ArrayList<Usuario> lerUsuarios(UsuarioDadosDePesquisaDto usuarioDadosDePesquisaDto, boolean erroEncontrado){
        if (erroEncontrado){
            UsuarioDAO dao = new UsuarioDAO();
            return dao.readAll();
        }

        CamposUsuarioAcessiveis clausulaWhere = CamposUsuarioAcessiveis.descobrirCampoUsuario(usuarioDadosDePesquisaDto.clausulaWhereNome().strip().toLowerCase());

        if(!clausulaWhere.isMultiplosRetornos()){
            Usuario usuario = lerUsuarioUnicoRetorno(clausulaWhere, usuarioDadosDePesquisaDto.clausulaWhereValor());

            ArrayList<Usuario> usuarios = new ArrayList<>();

            if (usuario.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
                usuarios.add(usuario);
            }
            return usuarios;
        }
        return lerUsuarioMultiplosRetornos(clausulaWhere, usuarioDadosDePesquisaDto);
    }

    private static Usuario lerUsuarioUnicoRetorno(CamposUsuarioAcessiveis clausulaWhere, String clausulaWhereValor){
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

    private static ArrayList<Usuario> lerUsuarioMultiplosRetornos(CamposUsuarioAcessiveis clausulaWhere, UsuarioDadosDePesquisaDto usuarioDadosDePesquisaDto){
        CamposUsuarioAcessiveis orderBy = CamposUsuarioAcessiveis.descobrirCampoUsuario(usuarioDadosDePesquisaDto.orderBy().strip().toLowerCase());

        UsuarioDAO dao = new UsuarioDAO();
        if (clausulaWhere == GENERICO){
            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoUsuario(), usuarioDadosDePesquisaDto.sentidoOrderBy());
        }

        if (clausulaWhere == NOME){
            String nomeTratado = usuarioDadosDePesquisaDto.clausulaWhereValor().strip();
            return orderBy == GENERICO ? dao.readAllByNome(nomeTratado) :
                                         dao.readAllByNomeOrderBy(nomeTratado, orderBy.getCampoUsuario(), usuarioDadosDePesquisaDto.sentidoOrderBy());
        }

        if(clausulaWhere == TIPO_USUARIO){
            String tipoUsuarioTratado = usuarioDadosDePesquisaDto.clausulaWhereValor().toUpperCase();
            return orderBy == GENERICO ?
                    dao.readAllByTipoUsuario(tipoUsuarioTratado) :
                    dao.readAllByTipoUsuarioOrderBy(tipoUsuarioTratado, orderBy.getCampoUsuario(), usuarioDadosDePesquisaDto.sentidoOrderBy());
        }

        if(clausulaWhere == RAIO_PROCURA_KM){
            double raioProcuraKmMin = Double.parseDouble(usuarioDadosDePesquisaDto.clausulaWhereValor().strip());
            double raioProcuraKmMax = Double.parseDouble(usuarioDadosDePesquisaDto.clausulaWhereValor2().strip());
            return orderBy == GENERICO ?
                    dao.readAllWhereRaioProcuraKmEntre(raioProcuraKmMin, raioProcuraKmMax) :
                    dao.readAllWhereRaioProcuraKmEntreOrderBy(raioProcuraKmMin, raioProcuraKmMax, orderBy.getCampoUsuario(), usuarioDadosDePesquisaDto.sentidoOrderBy());
        }

        return new ArrayList<>();
    }

    private static ArrayList<GenericExceptionEnum> validarUsuarioSelect(UsuarioDadosDePesquisaDto usuarioDadosDePesquisaDto){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum clausulaWhereNomeValidacao = validarWhere(usuarioDadosDePesquisaDto.clausulaWhereNome());
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(usuarioDadosDePesquisaDto.clausulaWhereNome(), usuarioDadosDePesquisaDto.clausulaWhereValor(), usuarioDadosDePesquisaDto.clausulaWhereValor2()));

        GenericExceptionEnum orderByValidacao = validarOrderBy(usuarioDadosDePesquisaDto.orderBy());
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = validarSentidoOrderBy(usuarioDadosDePesquisaDto.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static ArrayList<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor, String clausulaWhereValor2){
        ArrayList<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposUsuarioAcessiveis clausulaWhere = CamposUsuarioAcessiveis.descobrirCampoUsuario(clausulaWhereNome.strip().toLowerCase());
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
    public static ArrayList<GenericExceptionEnum> realizarUpdate(UsuarioDadosDto usuarioDadosDto){
        ArrayList<GenericExceptionEnum> erros = validarUpdate(usuarioDadosDto);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarUsuario(usuarioDadosDto);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarUsuario(UsuarioDadosDto usuarioDadosDto){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.updateById(usuarioDadosDto.construirUsuario());
    }

    private static ArrayList<GenericExceptionEnum> validarUpdate(UsuarioDadosDto usuarioDadosDto){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum emailValidacao = validarEmailBasico(usuarioDadosDto.email());
        if(emailValidacao != VALIDACAO_OK){
            erros.add(emailValidacao);
        }

        GenericExceptionEnum senhaValidacao = validarSenhaUpdate(usuarioDadosDto.senha());
        if (senhaValidacao != VALIDACAO_OK){
            erros.add(senhaValidacao);
        }

        GenericExceptionEnum nomeValidacao = validarNome(usuarioDadosDto.nome(), usuarioDadosDto.tipoUsuario());
        if (nomeValidacao != VALIDACAO_OK){
            erros.add(nomeValidacao);
        }

        GenericExceptionEnum raioProcuraKmValidacao = validarRaioProcuraKm(usuarioDadosDto.raioProcuraKm());
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
    public static ArrayList<GenericExceptionEnum> realizarInsert(UsuarioDadosDto usuarioDadosDto){
        ArrayList<GenericExceptionEnum> mensagens = validarUsuarioInsert(usuarioDadosDto);
        if (mensagens.isEmpty()) {
            int resultado = persistirUsuario(usuarioDadosDto);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static ArrayList<GenericExceptionEnum> validarUsuarioInsert(UsuarioDadosDto usuarioDadosDto){
        ArrayList<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoEmail = validarEmailInsert(usuarioDadosDto.email());
        if (validacaoEmail != VALIDACAO_OK){
            listaDeErros.add(validacaoEmail);
        }

        GenericExceptionEnum validacaoSenha = validarSenha(usuarioDadosDto.senha());
        if(validacaoSenha != VALIDACAO_OK){
            listaDeErros.add(validacaoSenha);
        }

        GenericExceptionEnum validacaoRaioProcuraKm = validarRaioProcuraKm(usuarioDadosDto.raioProcuraKm());
        if(validacaoRaioProcuraKm != VALIDACAO_OK && validacaoRaioProcuraKm != ATRIBUTO_NULL){
            listaDeErros.add(validacaoRaioProcuraKm);
        }

        GenericExceptionEnum validacaoTipoUsuario = validarTipoUsuario(usuarioDadosDto.tipoUsuario());
        if(validacaoTipoUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoUsuario);
            listaDeErros.add(IMPOSSIVEL_VALIDAR_NOME);
            return listaDeErros;
        }

        GenericExceptionEnum validacaoNome = validarNome(usuarioDadosDto.nome(), usuarioDadosDto.tipoUsuario());
        if(validacaoNome != VALIDACAO_OK){
            listaDeErros.add(validacaoNome);
        }
        return listaDeErros;
    }

    private static int persistirUsuario(UsuarioDadosDto usuarioDadosDto){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.insert(usuarioDadosDto.construirUsuario());
    }

}