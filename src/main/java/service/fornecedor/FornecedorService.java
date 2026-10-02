package service.fornecedor;

import dao.EmpresaDemandanteDAO;
import dao.FornecedorDAO;
import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import model.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static exception.ErrosDadosFornecedor.*;
import static exception.ErrosGerais.*;
import static exception.ErrosGeraisDados.*;
import static service.fornecedor.CamposFornecedorAcessiveis.*;


public class FornecedorService {

    private static final int TAMANHO_CNPJ = 14;

    private static final Pattern  PATTERN_CNPJ = Pattern.compile("^[0-9-A-Z]{12}[0-9]{2}$");

    private static final Pattern PATTERN_RAZAO_SOCIAL = Pattern.compile("^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

    private static final int TAMANHO_MAXIMO_RAZAO_SOCIAL = 150;

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

        CamposFornecedorAcessiveis campoDoWhere = CamposFornecedorAcessiveis.descobrirCampoFornecedor(where.strip().toLowerCase());
        return campoDoWhere == INVALIDO ? WHERE_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarOrderBy(String ordenacao){
        if (ordenacao == null || ordenacao.isBlank()){return ORDER_BY_INVALIDO;}

        CamposFornecedorAcessiveis ordenacaoCampo = CamposFornecedorAcessiveis.descobrirCampoFornecedor(ordenacao.strip().toLowerCase());
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

    private static GenericExceptionEnum validarIdUsuario(String idUsuario){
        if(validarId(idUsuario) != VALIDACAO_OK){
            return ID_USUARIO_INVALIDO;
        }

        long idUsuarioConvertido = Long.parseLong(idUsuario);
        UsuarioDAO dao = new UsuarioDAO();

        Usuario usuario = dao.readById(idUsuarioConvertido);
        if(usuario != null && usuario.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return ID_USUARIO_NAO_REGISTRADO;
        }

        if(usuario != null && usuario.getTipoUsuario() != TiposUsuario.FORNECEDOR){
            return TIPO_USUARIO_INCOMPATIVEL;
        }
        return VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarTipoFornecedor(String tipoFornecedor){
        if(tipoFornecedor == null || tipoFornecedor.isBlank()){
            return TIPO_FORNECEDOR_VAZIO;
        }

        TiposFornecedor tiposFornecedorEncontrado = TiposFornecedor.descobrirTipoFornecedor(tipoFornecedor.toUpperCase().strip());
        return tiposFornecedorEncontrado == null ? TIPO_FORNECEDOR_INVALIDO : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarCnpj(String cnpj, boolean insert){
        if(cnpj == null || cnpj.isBlank()){
            return CNPJ_VAZIO;
        }

        String cnpjTratado = cnpj.strip().toUpperCase();

        if(cnpjTratado.length() != TAMANHO_CNPJ){
            return CNPJ_TAMANHO_INVALIDO;
        }

        if(cnpjTratado.replace(cnpjTratado.charAt(0), ' ').isBlank()){
            return CNPJ_INVALIDO;
        }

        GenericExceptionEnum validacaoFormatoCnpj = validarFormatoCnpj(cnpjTratado);
        if(validacaoFormatoCnpj != VALIDACAO_OK){
            return validacaoFormatoCnpj;
        }

        GenericExceptionEnum validacaoDigitosCnpj = validarDigitosCnpj(cnpjTratado);
        if(validacaoDigitosCnpj != VALIDACAO_OK){
            return validacaoDigitosCnpj;
        }
        return insert ? validarCnpjUnico(cnpjTratado) : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarCnpjUnico(String cpnj){
        FornecedorDAO fornecedorDao = new FornecedorDAO();
        Fornecedor fornecedor = fornecedorDao.readByCnpj(cpnj);
        if(fornecedor == null || fornecedor.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return CNPJ_INVALIDO;
        }

        EmpresaDemandanteDAO empresaDemandanteDAO = new EmpresaDemandanteDAO();
        EmpresaDemandante empresaDemandante = empresaDemandanteDAO.readByCnpj(cpnj);
        if(empresaDemandante == null || empresaDemandante.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return CNPJ_INVALIDO;
        }
        return VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarFormatoCnpj(String cnpj){
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

    private static GenericExceptionEnum validarRazaoSocial(String razaoSocial){
        if(razaoSocial == null || razaoSocial.isBlank()){
            return RAZAO_SOCIAL_VAZIA;
        }

        String razaoSocialTratada = razaoSocial.strip();

        if(razaoSocialTratada.length() > TAMANHO_MAXIMO_RAZAO_SOCIAL){
            return RAZAO_SOCIAL_TAMANHO_INVALIDO;
        }

        if(razaoSocialTratada.toLowerCase().replace(razaoSocialTratada.charAt(0), ' ').isBlank()){
            return RAZAO_SOCIAL_INVALIDA;
        }

        return validarEstruturaRazaoSocial(razaoSocialTratada);
    }

    private static GenericExceptionEnum validarEstruturaRazaoSocial(String razaoSocial){
        return PATTERN_RAZAO_SOCIAL.matcher(razaoSocial).matches() ? VALIDACAO_OK : RAZAO_SOCIAL_INVALIDA;
    }

    //Métodos relacionados ao delete
    public static ArrayList<GenericExceptionEnum> realizarDelete(String id){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();

        if(validarId(id) != VALIDACAO_OK) {
            erros.add(ERRO_GENERICO);
            return erros;
        }

        int qtdLinhasDeletadas = deletarFornecedor(id);
        if(qtdLinhasDeletadas > 0){
            return erros;
        }

        erros.add(descobrirErroGeral(qtdLinhasDeletadas));
        return erros;
    }

    private static int deletarFornecedor(String id){
        FornecedorDAO dao = new FornecedorDAO();
        return dao.deleteById(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static ArrayList<Fornecedor> realizarSelect(FornecedorDadosDePesquisaDto fornecedorDadosDePesquisaDto, ArrayList<GenericExceptionEnum> errosEncontrados){
        ArrayList<Fornecedor> Fornecedores;

        if(fornecedorDadosDePesquisaDto == null){
            return lerFornecedores(null, true);
        }

        errosEncontrados.addAll(validarFornecedorSelect(fornecedorDadosDePesquisaDto));
        if (!errosEncontrados.isEmpty()){
            return lerFornecedores(fornecedorDadosDePesquisaDto,true);
        }

        Fornecedores = lerFornecedores(fornecedorDadosDePesquisaDto,false);
        if (Fornecedores.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return Fornecedores;
    }

    private static ArrayList<Fornecedor> lerFornecedores(FornecedorDadosDePesquisaDto fornecedorDadosDePesquisaDto, boolean erroEncontrado){
        if (erroEncontrado){
            FornecedorDAO dao = new FornecedorDAO();
            return dao.readAll();
        }

        CamposFornecedorAcessiveis clausulaWhere = CamposFornecedorAcessiveis.descobrirCampoFornecedor(fornecedorDadosDePesquisaDto.clausulaWhereNome().strip().toLowerCase());

        if(!clausulaWhere.isMultiplosRetornos()){
            Fornecedor fornecedor = lerFornecedorUnicoRetorno(clausulaWhere, fornecedorDadosDePesquisaDto.clausulaWhereValor());

            ArrayList<Fornecedor> fornecedores = new ArrayList<>();

            if (fornecedor.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
                fornecedores.add(fornecedor);
            }
            return fornecedores;
        }
        return lerFornecedorMultiplosRetornos(clausulaWhere, fornecedorDadosDePesquisaDto);
    }

    private static Fornecedor lerFornecedorUnicoRetorno(CamposFornecedorAcessiveis clausulaWhere, String clausulaWhereValor){
        FornecedorDAO dao = new FornecedorDAO();

        if (clausulaWhere == ID){
            long id = Long.parseLong(clausulaWhereValor.strip());
            return dao.readById(id);
        }

        if (clausulaWhere == ID_USUARIO){
            long idUsuario = Long.parseLong(clausulaWhereValor.strip());
            return dao.readByIdUsuario(idUsuario);
        }

        if (clausulaWhere == CNPJ){
            String cnpjTratado = clausulaWhereValor.strip();
            return dao.readByCnpj(cnpjTratado);
        }
        return new Fornecedor(REGISTRO_NAO_ENCONTRADO.getCodigo(), REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null);
    }

    private static ArrayList<Fornecedor> lerFornecedorMultiplosRetornos(CamposFornecedorAcessiveis clausulaWhere, FornecedorDadosDePesquisaDto fornecedorDadosDePesquisaDto){
        CamposFornecedorAcessiveis orderBy = CamposFornecedorAcessiveis.descobrirCampoFornecedor(fornecedorDadosDePesquisaDto.orderBy().strip().toLowerCase());

        FornecedorDAO dao = new FornecedorDAO();
        if (clausulaWhere == GENERICO){
            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDto.sentidoOrderBy());
        }

        if (clausulaWhere == TIPO_FORNECEDOR){
            String tipoFornecedorTratado = fornecedorDadosDePesquisaDto.clausulaWhereValor().strip();
            return orderBy == GENERICO ? dao.readAllByTipoFornecedor(tipoFornecedorTratado) :
                    dao.readAllByTipoFornecedorOrderBy(tipoFornecedorTratado, orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDto.sentidoOrderBy());
        }

        if(clausulaWhere == RAZAO_SOCIAL){
            String razaoSocialTratada = fornecedorDadosDePesquisaDto.clausulaWhereValor().toUpperCase();
            return orderBy == GENERICO ?
                    dao.readAllByRazaoSocial(razaoSocialTratada) :
                    dao.readAllByRazaoSocialOrderBy(razaoSocialTratada, orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDto.sentidoOrderBy());
        }
        return new ArrayList<>();
    }

    private static ArrayList<GenericExceptionEnum> validarFornecedorSelect(FornecedorDadosDePesquisaDto FornecedorDadosDePesquisaDto){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum clausulaWhereNomeValidacao = validarWhere(FornecedorDadosDePesquisaDto.clausulaWhereNome());
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(FornecedorDadosDePesquisaDto.clausulaWhereNome(), FornecedorDadosDePesquisaDto.clausulaWhereValor()));

        GenericExceptionEnum orderByValidacao = validarOrderBy(FornecedorDadosDePesquisaDto.orderBy());
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = validarSentidoOrderBy(FornecedorDadosDePesquisaDto.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static ArrayList<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor){
        ArrayList<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposFornecedorAcessiveis clausulaWhere = CamposFornecedorAcessiveis.descobrirCampoFornecedor(clausulaWhereNome.strip().toLowerCase());
        if (clausulaWhere == GENERICO){
            return errosNasClausulasWhere;
        }

        if (clausulaWhere == ID || clausulaWhere == ID_USUARIO){
            clausulaWhereValorValidacao = validarId(clausulaWhereValor);
        }

        if (clausulaWhere == TIPO_FORNECEDOR){
            clausulaWhereValorValidacao = validarTipoFornecedor(clausulaWhereValor);
        }

        if (clausulaWhere == CNPJ){
            clausulaWhereValorValidacao = validarCnpj(clausulaWhereValor, false);
        }

        if (clausulaWhere == RAZAO_SOCIAL){
            clausulaWhereValorValidacao = validarRazaoSocial(clausulaWhereValor);
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
    public static ArrayList<GenericExceptionEnum> realizarUpdate(FornecedorDadosDto FornecedorDadosDto){
        ArrayList<GenericExceptionEnum> erros = validarUpdate(FornecedorDadosDto);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarFornecedor(FornecedorDadosDto);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarFornecedor(FornecedorDadosDto FornecedorDadosDto){
        FornecedorDAO dao = new FornecedorDAO();
        return dao.updateById(FornecedorDadosDto.construirFornecedor());
    }

    private static ArrayList<GenericExceptionEnum> validarUpdate(FornecedorDadosDto FornecedorDadosDto){
        ArrayList<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum tipoFornecedorValidacao = validarTipoFornecedor(FornecedorDadosDto.tipoFornecedor());
        if(tipoFornecedorValidacao != VALIDACAO_OK){
            erros.add(tipoFornecedorValidacao);
        }

        GenericExceptionEnum razaoSocialValidacao = validarRazaoSocial(FornecedorDadosDto.razaoSocial());
        if (razaoSocialValidacao != VALIDACAO_OK){
            erros.add(razaoSocialValidacao);
        }
        return erros;
    }

    public static Fornecedor exibirFornecedorParaUpdate(String id){
        if(validarId(id) != VALIDACAO_OK){
            return null;
        }

        FornecedorDAO dao = new FornecedorDAO();
        return dao.readById(Long.parseLong(id));
    }

    //Métodos relacionados ao insert
    public static ArrayList<GenericExceptionEnum> realizarInsert(FornecedorDadosDto FornecedorDadosDto){
        ArrayList<GenericExceptionEnum> mensagens = validarFornecedorInsert(FornecedorDadosDto);
        if (mensagens.isEmpty()) {
            int resultado = persistirFornecedor(FornecedorDadosDto);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static ArrayList<GenericExceptionEnum> validarFornecedorInsert(FornecedorDadosDto FornecedorDadosDto){
        ArrayList<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(FornecedorDadosDto.idUsuario());
        if (validacaoIdUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoIdUsuario);
        }

        GenericExceptionEnum validacaoTipoFornecedor = validarTipoFornecedor(FornecedorDadosDto.tipoFornecedor());
        if(validacaoTipoFornecedor != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoFornecedor);
        }

        GenericExceptionEnum validacaoCnpj = validarCnpj(FornecedorDadosDto.cnpj(), true);
        if(validacaoCnpj != VALIDACAO_OK && validacaoCnpj != ATRIBUTO_NULL){
            listaDeErros.add(validacaoCnpj);
        }

        GenericExceptionEnum validacaoRazaoSocial = validarRazaoSocial(FornecedorDadosDto.razaoSocial());
        if(validacaoRazaoSocial != VALIDACAO_OK){
            listaDeErros.add(validacaoRazaoSocial);
        }
        return listaDeErros;
    }

    private static int persistirFornecedor(FornecedorDadosDto FornecedorDadosDto){
        FornecedorDAO dao = new FornecedorDAO();
        return dao.insert(FornecedorDadosDto.construirFornecedor());
    }
}
