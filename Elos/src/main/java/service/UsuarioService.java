package service;

import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import model.TiposUsuario;
import exception.ValidacaoDadosUsuario;import model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static exception.ErrosGerais.*;
import static exception.ValidacaoDadosUsuario.*;

public class UsuarioService {

    //Métodos para realizar as ações do UpdateProfissionalServlet
    public static ArrayList<GenericExceptionEnum> realizarUpdate(String idUpdate, String tipoUsuarioUpdate, String emailUpdate, String senhaUpdate, String nomeUpdate, String raioProcuraKmUpdate){
        ArrayList<GenericExceptionEnum> erros = validarUpdate(tipoUsuarioUpdate, emailUpdate, senhaUpdate, nomeUpdate, raioProcuraKmUpdate);

        if(!erros.isEmpty()){
            return erros;
        }

        int resultado = atualizarUsuario(idUpdate,tipoUsuarioUpdate, emailUpdate, senhaUpdate, nomeUpdate, raioProcuraKmUpdate);

        if(resultado <= 0){
            erros.add(ErrosGerais.descobrirErroGeral(resultado));
        }

        return erros;

    }

    private static int atualizarUsuario(String idUpdate, String tipoUsuarioUpdate, String emailUpdate, String senhaUpdate, String nomeUpdate, String raioProcuraKmUpdate){
        Usuario usuario = criarUsuarioValido(idUpdate, emailUpdate, senhaUpdate, nomeUpdate, tipoUsuarioUpdate, raioProcuraKmUpdate);
        UsuarioDAO dao = new UsuarioDAO();

        int resultado = dao.updateById(usuario);

        return resultado;

    }

    private static ArrayList<GenericExceptionEnum> validarUpdate(String tipoUsuarioUpdate,String emailUpdate, String senhaUpdate, String nomeUpdate, String raioProcuraKmUpdate){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();
        ValidacaoDadosUsuario emailValidacao = validarEmailBasico(emailUpdate);

        if(emailValidacao != VALIDACAO_OK){
            erros.add(emailValidacao);
        }

        ValidacaoDadosUsuario senhaValidacao = validarSenhaUpdate(senhaUpdate);

        if (senhaValidacao != VALIDACAO_OK){
            erros.add(senhaValidacao);
        }

        ValidacaoDadosUsuario nomeValidacao = validarNome(nomeUpdate, tipoUsuarioUpdate);

        if (nomeValidacao != VALIDACAO_OK){
            erros.add(nomeValidacao);
        }

        ValidacaoDadosUsuario raioProcuraKmValidacao = validarRaioProcuraKm(raioProcuraKmUpdate);

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

    //Métodos para realizar as ações do DeleteEmpresaDemandanteServlet
    public static GenericExceptionEnum realizarDelete(String id){
        ValidacaoDadosUsuario erro = validarId(id);

        if(erro != VALIDACAO_OK) {
            return erro;
        }

        int resultado = deletarUsuario(id);

        if(resultado > 0){
            return null;
        }

        return descobrirErroGeral(resultado);

    }

    private static int deletarUsuario(String id){
        long idConvertido = Long.parseLong(id.trim());
        UsuarioDAO dao = new UsuarioDAO();

        return dao.deleteById(idConvertido);

    }

    //Métodos para realizar as ações do ReadUsuarioService
    public static List<Usuario> realizarSelect(String clausulaWhereNome, String clausulaWhereValor, String clausulaWhereValor2, String orderBy, String ordenacao, ArrayList<GenericExceptionEnum> errosEncontrados){
        List<Usuario> usuarios = new ArrayList<>();

        if(clausulaWhereNome == null && clausulaWhereValor == null && clausulaWhereValor2 == null && orderBy == null && ordenacao == null){
            return lerUsuarios(null, null, null, null, null, true);
        }

        errosEncontrados.addAll(validarUsuarioSelect(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, ordenacao, orderBy));

        if (!errosEncontrados.isEmpty()){
            return lerUsuarios(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, orderBy, ordenacao,true);
        }

        usuarios = lerUsuarios(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, orderBy, ordenacao,false);

        if (usuarios.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }

        return usuarios;

    }

    private static List<Usuario> lerUsuarios(String clausulaWhereNome, String clausulaWhereValor, String clausulaWhereValor2, String orderBy, String ordenacao, boolean erroEncontrado){
        List<Usuario> usuarios = new ArrayList<>();
        UsuarioDAO dao = new UsuarioDAO();

        if (erroEncontrado){
            return dao.readAll();
        }

        String clausulaWhereNomeTratado = clausulaWhereNome.trim().toLowerCase().replaceAll("á","a")
                .replaceAll(" em","")
                .replaceAll(" de","")
                .replaceAll(" ", "_");

        String orderByTratado = orderBy.trim().toLowerCase().replaceAll("á","a")
                .replaceAll(" em","")
                .replaceAll(" de","")
                .replaceAll(" ", "_");

        if (clausulaWhereNomeTratado.equalsIgnoreCase("id")){
            long id = Long.parseLong(clausulaWhereValor.trim());
            usuarios.add(dao.readById(id));

            return usuarios;

        }

        if (clausulaWhereNomeTratado.equalsIgnoreCase("email")){
            String emailTratado = clausulaWhereValor.toLowerCase().trim();
            usuarios.add(dao.readByEmail(emailTratado));

            return usuarios;

        }

        if (clausulaWhereNomeTratado.equalsIgnoreCase("nome")){
            String nomeTratado = clausulaWhereValor.trim();
            usuarios.add(dao.readByNome(nomeTratado));

            return usuarios;

        }

        if (clausulaWhereNomeTratado.equalsIgnoreCase("nenhuma")){
            usuarios = orderByTratado.equalsIgnoreCase("nenhuma") ? dao.readAll() : dao.readAllOrderBy(orderByTratado, ordenacao);

            return usuarios;

        }

        if(clausulaWhereNomeTratado.equalsIgnoreCase("tipo_usuario")){
            String tipoUsuarioTratado = clausulaWhereValor.toUpperCase();

            usuarios = orderByTratado.equalsIgnoreCase("nenhuma") ?
                            dao.readAllByTipoUsuario(tipoUsuarioTratado) :
                            dao.readAllByTipoUsuarioOrderBy(tipoUsuarioTratado, orderByTratado, ordenacao);

            return usuarios;

        }

        if(clausulaWhereNomeTratado.equalsIgnoreCase("raio_procura_km")){
            double raioProcuraKmMin = Double.parseDouble(clausulaWhereValor.trim());
            double raioProcuraKmMax = Double.parseDouble(clausulaWhereValor2.trim());

            return orderByTratado.equalsIgnoreCase("nenhuma") ?
                    dao.readAllWhereRaioProcuraKmEntre(raioProcuraKmMin, raioProcuraKmMax) :
                    dao.readAllWhereRaioProcuraKmEntreOrderBy(raioProcuraKmMin, raioProcuraKmMax, orderBy, ordenacao);
        }

        return dao.readAll();

    }

    private static ArrayList<GenericExceptionEnum> validarUsuarioSelect(String clausulaWhereNome, String clausulaWhereValor, String clausulaWhereValor2, String ordenacao, String orderBy){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();
        ValidacaoDadosUsuario clausulaWhereNomeValidacao = validarWhere(clausulaWhereNome);

        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);

            return erros;

        }

        erros.addAll(validarClausulaWhereValor(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2));
        ValidacaoDadosUsuario orderByValidacao = validarOrderBy(orderBy);

        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        ValidacaoDadosUsuario ordenacaoValidacao = validarSentidoOrdenacao(ordenacao);

        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }

        return erros;

    }

    private static ArrayList<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor, String clausulaWhereValor2){
        ArrayList<GenericExceptionEnum> dadosValidados = new ArrayList<>();
        ValidacaoDadosUsuario dadoValidado = null;
        ValidacaoDadosUsuario dadoValidado2 = null;

        String clausulaWhereNomeTratado = clausulaWhereNome.trim().toLowerCase().replaceAll("á","a")
                .replaceAll(" em","")
                .replaceAll(" de","")
                .replaceAll(" ", "_");

        if (clausulaWhereNomeTratado.equalsIgnoreCase("id")){
            dadoValidado = validarId(clausulaWhereValor);
        } else if(clausulaWhereNomeTratado.equalsIgnoreCase("email")){
            dadoValidado = validarEmailBasico(clausulaWhereValor);
        } else if(clausulaWhereNomeTratado.equalsIgnoreCase("nome")){
            dadoValidado = validarNome(clausulaWhereValor, "PROFISSIONAL") == VALIDACAO_OK ? validarNome(clausulaWhereValor, "PROFISSIONAL") : validarNome(clausulaWhereValor, "FORNECEDOR");
        } else if(clausulaWhereNomeTratado.equalsIgnoreCase("tipo_usuario")){
            dadoValidado = validarTipoUsuario(clausulaWhereValor);
        } else if(clausulaWhereNomeTratado.equalsIgnoreCase("raio_procura_km")){
            dadoValidado = validarRaioProcuraKm(clausulaWhereValor);
            dadoValidado2 = validarRaioProcuraKm(clausulaWhereValor2);

            if(dadoValidado != VALIDACAO_OK){
                dadosValidados.add(RAIOS_PROCURA_KM_NAO_NUMERICO);

                return dadosValidados;

            }
        } else if (clausulaWhereNomeTratado.equalsIgnoreCase("nenhuma")){
            return dadosValidados;

        }else {
            dadoValidado = DADO_INVALIDO_GENERICO;
        }

        if (dadoValidado != VALIDACAO_OK){
            dadosValidados.add(dadoValidado);
        }

        if (dadoValidado2 != null && dadoValidado2 != VALIDACAO_OK){
            dadosValidados.add(dadoValidado2);
        }

        return dadosValidados;

    }

    //Métodos para realizar as ações do InsertEnderecoServlet
    public static ArrayList<GenericExceptionEnum> realizarInsert(String email, String senha, String nome, String tipoUsuario, String raioProcuraKm){
        ArrayList<GenericExceptionEnum> mensagens = validarUsuarioInsert(email, senha, nome, tipoUsuario, raioProcuraKm);

        if (mensagens.isEmpty()) {
            int resultado = persistirUsuario(email, senha, nome, tipoUsuario, raioProcuraKm);

            if (resultado != 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }

        return mensagens;

    }

    private static ArrayList<GenericExceptionEnum> validarUsuarioInsert(String email, String senha, String nome, String tipoUsuario, String raioProcuraKm){
        ArrayList<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        ValidacaoDadosUsuario validacaoEmail = validarEmailInsert(email);
        ValidacaoDadosUsuario validacaoSenha = validarSenha(senha);
        ValidacaoDadosUsuario validacaoTipoUsuario = validarTipoUsuario(tipoUsuario);
        ValidacaoDadosUsuario validacaoNome = validarNome(nome, tipoUsuario);
        ValidacaoDadosUsuario validacaoRaioProcuraKm = validarRaioProcuraKm(raioProcuraKm);

        if (validacaoEmail != VALIDACAO_OK){
            listaDeErros.add(validacaoEmail);
        }

        if(validacaoSenha != VALIDACAO_OK){
            listaDeErros.add(validacaoSenha);
        }

        if(validacaoTipoUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoUsuario);

            listaDeErros.add(IMPOSSIVEL_VALIDAR_NOME);
            validacaoNome = VALIDACAO_OK;
        }

        if(validacaoNome != VALIDACAO_OK){
            listaDeErros.add(validacaoNome);
        }

        if(validacaoRaioProcuraKm != VALIDACAO_OK && validacaoRaioProcuraKm != ATRIBUTO_NULL){
            listaDeErros.add(validacaoRaioProcuraKm);
        }

        return listaDeErros;

    }

    private static int persistirUsuario(String email, String senha, String nome, String tipoUsuario, String raioProcuraKm){
        Usuario usuario = criarUsuarioValido(null, email, senha, nome, tipoUsuario, raioProcuraKm);
        UsuarioDAO dao = new UsuarioDAO();

        return dao.insert(usuario);
    }

    private static Usuario criarUsuarioValido(String id, String email, String senha, String nome, String tipoUsuario, String raioProcuraKm){
        long idTratado = id != null && !id.isBlank()? Long.parseLong(id.trim()) : ATRIBUTO_NULL.getCodigo();
        String emailTratado = email.toLowerCase().trim();
        String senhaTratada = senha == null || senha.isBlank() ? null : senha.trim();
        String nomeTratado = nome.trim();
        TiposUsuario tipoUsuarioTratado = TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().trim());
        double raioProcuraKmTratado = raioProcuraKm == null || raioProcuraKm.isEmpty() ? ATRIBUTO_NULL.getCodigo() : Double.parseDouble(raioProcuraKm.trim());

        return new Usuario(idTratado, emailTratado, senhaTratada, nomeTratado, tipoUsuarioTratado, raioProcuraKmTratado);
    }

    //Métodos auxiliares
    private static ValidacaoDadosUsuario validarSentidoOrdenacao(String ordenacao){
        if (ordenacao == null || ordenacao.isBlank()){return ORDENACAO_INVALIDA;}

        String ordenacaoTratada = ordenacao.trim().toLowerCase().replaceAll("á","a")
                .replaceAll(" em","")
                .replaceAll(" de","")
                .replaceAll(" ", "_");

        switch(ordenacaoTratada) {
            case "asc", "desc":
                return VALIDACAO_OK;
            default:
                return ORDENACAO_INVALIDA;

        }
    }

    private static ValidacaoDadosUsuario validarWhere(String where){
        if (where == null || where.isBlank()){return WHERE_INVALIDO;}

        String whereTratada = where.trim().toLowerCase().replaceAll("á","a")
                .replaceAll(" em","")
                .replaceAll(" de","")
                .replaceAll(" ", "_");

        switch(whereTratada) {
            case "id", "email", "nome", "tipo_usuario", "raio_procura_km", "nenhuma":
                return VALIDACAO_OK;
            default:
                return WHERE_INVALIDO;

        }
    }

    private static ValidacaoDadosUsuario validarOrderBy(String ordenacao){
        if (ordenacao == null || ordenacao.isBlank()){return ORDER_BY_INVALIDO;}

        String ordenacaoTratada = ordenacao.trim().toLowerCase().replaceAll("á","a")
                                                                .replaceAll(" em","")
                                                                .replaceAll(" de","")
                                                                .replaceAll(" ", "_");

        switch(ordenacaoTratada) {
            case "id", "email", "nome", "tipo_usuario", "raio_procura_km", "nenhuma":
                return VALIDACAO_OK;
            default:
                return ORDER_BY_INVALIDO;

        }
    }

    private static ValidacaoDadosUsuario validarId(String id){
        try {
            String idTratado = id.trim();
            long idConvertido = Long.parseLong(idTratado);

            return VALIDACAO_OK;

        } catch (NumberFormatException numberFormatException){
            return ID_INVALIDO;

        }
    }

    private static ValidacaoDadosUsuario validarEmailBasico(String email) {
        if (email == null || email.isBlank()) {
            return EMAIL_VAZIO;

        }

        String emailTratado = email.toLowerCase().trim();

        if(emailTratado.length() > 150){
            return EMAIL_TAMANHO_INVALIDO;

        }

        ValidacaoDadosUsuario validacaoFormato = validarFormatoEmail(emailTratado);

        if (validacaoFormato != VALIDACAO_OK){
            return validacaoFormato;

        }

        return VALIDACAO_OK;

    }

    private static ValidacaoDadosUsuario validarEmailInsert(String email) {
        ValidacaoDadosUsuario validarEmailBasico = validarFormatoEmail(email);

        if(validarEmailBasico != VALIDACAO_OK){
            return validarEmailBasico;

        }

        String emailTratado = email.toLowerCase().trim();

        ValidacaoDadosUsuario emailNaoCadastrado = validarEmailNaoCadastrado(emailTratado);

        if(emailNaoCadastrado != VALIDACAO_OK){
            return emailNaoCadastrado;

        }

        return VALIDACAO_OK;

    }

    private static ValidacaoDadosUsuario validarFormatoEmail(String email){
        Pattern pattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
        Matcher matcher = pattern.matcher(email);

        if(!matcher.matches()){
            return EMAIL_INVALIDO;
        }

        return VALIDACAO_OK;

    }

    private static ValidacaoDadosUsuario validarEmailNaoCadastrado(String email){
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.readByEmail(email);

        if(usuario != null && usuario.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return EMAIL_INVALIDO;

        }

        return VALIDACAO_OK;

    }

    private static ValidacaoDadosUsuario validarSenhaUpdate(String senha){
        if (senha == null || senha.isBlank()){
            return VALIDACAO_OK;

        }

        String senhaTrim = senha.trim();

        return validarSenha(senhaTrim);

    }

    private static ValidacaoDadosUsuario validarSenha(String senha){
        if (senha == null || senha.isBlank()){
            return SENHA_VAZIA;

        }

        String senhaTrim = senha.trim();

        if (senhaTrim.length() < 8){
            return SENHA_MENOR_QUE_OITO;

        }

        if (senhaTrim.length() > 60){
            return SENHA_TAMANHO_INVALIDO;

        }

        Pattern pattern = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}");
        Matcher matcher = pattern.matcher(senhaTrim);

        return matcher.matches() ? VALIDACAO_OK : SENHA_FRACA;

    }

    private static ValidacaoDadosUsuario validarNome(String nome, String tipoUsuario){
        if (nome == null || nome.isBlank()){
            return NOME_VAZIO;

        }

        String nomeTrim = nome.trim();

        if (nomeTrim.length() > 150){
            return NOME_TAMANHO_INVALIDO;

        }

        Pattern pattern;
        Matcher matcher;

        if ("PROFISSIONAL".equalsIgnoreCase(tipoUsuario)){
            pattern  = Pattern.compile("^[\\p{Script=Latin}\\']+[\\p{Script=Latin}\\'\\x20\\-]+$");
            matcher = pattern.matcher(nomeTrim);
        }
        else {
            pattern  = Pattern.compile("^[\\p{Script=Latin}0-9&,.;\\'\\-]+[\\p{Script=Latin}0-9&,.;\\'\\-\\x20]+$");
            matcher = pattern.matcher(nomeTrim);
        }

        return matcher.matches() ? VALIDACAO_OK : NOME_INVALIDO;

    }

    private static ValidacaoDadosUsuario validarTipoUsuario(String tipoUsuario){
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            return TIPO_USUARIO_VAZIO;
        }

        TiposUsuario tipoUsuarioToUpperCase = TiposUsuario.descobrirTipoUsuario(tipoUsuario.toUpperCase().trim());

        if (tipoUsuarioToUpperCase == null){
            return TIPO_USUARIO_INVALIDO;
        }
        return VALIDACAO_OK;
    }

    private static ValidacaoDadosUsuario validarRaioProcuraKm(String raioProcuraKm){
        if (raioProcuraKm == null || raioProcuraKm.isBlank()){
            return ATRIBUTO_NULL;
        }

        Double raioProcuraKmConvertido = validarRaioProcuraKmConversivel(raioProcuraKm);

        if (raioProcuraKmConvertido == null){
            return RAIO_PROCURA_KM_NAO_NUMERICO;
        }

        if (raioProcuraKmConvertido < 0){
            return RAIO_PROCURA_KM_MENOR_OU_IGUAL_QUE_ZERO;
        }

        if (raioProcuraKmConvertido > 999.99){
            return RAIO_PROCURA_KM_TAMANHO_INVALIDO;

        }

        return VALIDACAO_OK;

    }

    private static Double validarRaioProcuraKmConversivel(String raioProcuraKm){
        try {
            String raioProcuraKmTrim = raioProcuraKm.trim();

            return Double.parseDouble(raioProcuraKmTrim);

        }catch (NumberFormatException numberFormatException) {
            return null;

        }
    }
}