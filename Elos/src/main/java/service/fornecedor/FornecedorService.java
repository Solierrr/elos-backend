package service.fornecedor;

import dao.EmpresaDemandanteDAO;
import dao.FornecedorDAO;
import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import model.*;
import service.ValidacoesComunsService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static exception.ErrosDadosFornecedor.*;
import static exception.ErrosGerais.*;
import static exception.ErrosGeraisDados.*;
import static service.fornecedor.CamposFornecedor.*;


public final class FornecedorService {

    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
    private static final int TAMANHO_CNPJ = 14;
    private static final Pattern  PATTERN_CNPJ = Pattern.compile("^[0-9-A-Z]{12}[0-9]{2}$");

    private static final Pattern PATTERN_RAZAO_SOCIAL = Pattern.compile("^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

    private static final int TAMANHO_MAXIMO_RAZAO_SOCIAL = 150;

    //Validação do id usuario
    private static GenericExceptionEnum validarIdUsuario(String idUsuario){
        if(ValidacoesComunsService.validarId(idUsuario) != VALIDACAO_OK){
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

    //Validação do tipo fornecedor
    private static GenericExceptionEnum validarTipoFornecedor(String tipoFornecedor){
        if(tipoFornecedor == null || tipoFornecedor.isBlank()){
            return TIPO_FORNECEDOR_VAZIO;
        }

        TiposFornecedor tiposFornecedorEncontrado = TiposFornecedor.descobrirTipoFornecedor(tipoFornecedor.toUpperCase().strip());
        return tiposFornecedorEncontrado == null ? TIPO_FORNECEDOR_INVALIDO : VALIDACAO_OK;
    }

    //Validações do cnpj
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

    //Validações da razão social
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

        return validarFormatoRazaoSocial(razaoSocialTratada);
    }

    private static GenericExceptionEnum validarFormatoRazaoSocial(String razaoSocial){
        return PATTERN_RAZAO_SOCIAL.matcher(razaoSocial).matches() ? VALIDACAO_OK : RAZAO_SOCIAL_INVALIDA;
    }

    //Métodos relacionados ao delete
    public static GenericExceptionEnum realizarDelete(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK) {
            return ERRO_GENERICO;
        }

        int qtdLinhasDeletadas = deletarFornecedor(id);
        if(qtdLinhasDeletadas > 0){
            return SUCESSO;
        }
        return descobrirErroGeral(qtdLinhasDeletadas);
    }

    private static int deletarFornecedor(String id){
        FornecedorDAO dao = new FornecedorDAO();
        return dao.deleteById(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static List<Fornecedor> realizarSelect(FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO, List<GenericExceptionEnum> errosEncontrados){
        List<Fornecedor> Fornecedores;

        if(fornecedorDadosDePesquisaDTO == null){
            return lerFornecedores(null, true);
        }

        errosEncontrados.addAll(validarFornecedorSelect(fornecedorDadosDePesquisaDTO));
        if (!errosEncontrados.isEmpty()){
            return lerFornecedores(fornecedorDadosDePesquisaDTO,true);
        }

        Fornecedores = lerFornecedores(fornecedorDadosDePesquisaDTO,false);
        if (Fornecedores.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return Fornecedores;
    }

    private static List<Fornecedor> lerFornecedores(FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO, boolean erroEncontrado){
        if (erroEncontrado){
            FornecedorDAO dao = new FornecedorDAO();
            return dao.readAll();
        }

        CamposFornecedor clausulaWhere = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.clausulaWhereNome().strip().toLowerCase());

        if(!clausulaWhere.isMultiplosRetornos()){
            Fornecedor fornecedor = lerFornecedorUnicoRetorno(clausulaWhere, fornecedorDadosDePesquisaDTO.clausulaWhereValor());

            List<Fornecedor> fornecedores = new ArrayList<>();

            if (fornecedor.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
                fornecedores.add(fornecedor);
            }
            return fornecedores;
        }
        return lerFornecedorMultiplosRetornos(clausulaWhere, fornecedorDadosDePesquisaDTO);
    }

    private static Fornecedor lerFornecedorUnicoRetorno(CamposFornecedor clausulaWhere, String clausulaWhereValor){
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

    private static List<Fornecedor> lerFornecedorMultiplosRetornos(CamposFornecedor clausulaWhere, FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO){
        CamposFornecedor orderBy = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.orderBy().strip().toLowerCase());

        FornecedorDAO dao = new FornecedorDAO();
        if (clausulaWhere == GENERICO){
            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDTO.sentidoOrderBy());
        }

        if (clausulaWhere == TIPO_FORNECEDOR){
            String tipoFornecedorTratado = fornecedorDadosDePesquisaDTO.clausulaWhereValor().strip();
            return orderBy == GENERICO ? dao.readAllByTipoFornecedor(tipoFornecedorTratado) :
                    dao.readAllByTipoFornecedorOrderBy(tipoFornecedorTratado, orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDTO.sentidoOrderBy());
        }

        if(clausulaWhere == RAZAO_SOCIAL){
            String razaoSocialTratada = fornecedorDadosDePesquisaDTO.clausulaWhereValor().toUpperCase();
            return orderBy == GENERICO ?
                    dao.readAllByRazaoSocial(razaoSocialTratada) :
                    dao.readAllByRazaoSocialOrderBy(razaoSocialTratada, orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDTO.sentidoOrderBy());
        }
        return new ArrayList<>();
    }

    private static List<GenericExceptionEnum> validarFornecedorSelect(FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        CamposFornecedor clausulaWhere = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.clausulaWhereNome());
        GenericExceptionEnum clausulaWhereNomeValidacao = ValidacoesComunsService.validarWhere(clausulaWhere);
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(fornecedorDadosDePesquisaDTO.clausulaWhereNome(), fornecedorDadosDePesquisaDTO.clausulaWhereValor()));

        CamposFornecedor orderBy = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.orderBy());
        GenericExceptionEnum orderByValidacao = ValidacoesComunsService.validarOrderBy(orderBy);
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = ValidacoesComunsService.validarSentidoOrderBy(fornecedorDadosDePesquisaDTO.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static List<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor){
        List<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposFornecedor clausulaWhere = CamposFornecedor.descobrirCampoFornecedor(clausulaWhereNome.strip().toLowerCase());
        if (clausulaWhere == GENERICO){
            return errosNasClausulasWhere;
        }

        if (clausulaWhere == ID || clausulaWhere == ID_USUARIO){
            clausulaWhereValorValidacao = ValidacoesComunsService.validarId(clausulaWhereValor);
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
    public static List<GenericExceptionEnum> realizarUpdate(FornecedorDadosDTO fornecedorDadosDTO){
        List<GenericExceptionEnum> erros = validarUpdate(fornecedorDadosDTO);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarFornecedor(fornecedorDadosDTO);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarFornecedor(FornecedorDadosDTO fornecedorDadosDTO){
        FornecedorDAO dao = new FornecedorDAO();
        return dao.updateById(fornecedorDadosDTO.construirFornecedor());
    }

    private static List<GenericExceptionEnum> validarUpdate(FornecedorDadosDTO fornecedorDadosDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum tipoFornecedorValidacao = validarTipoFornecedor(fornecedorDadosDTO.tipoFornecedor());
        if(tipoFornecedorValidacao != VALIDACAO_OK){
            erros.add(tipoFornecedorValidacao);
        }

        GenericExceptionEnum razaoSocialValidacao = validarRazaoSocial(fornecedorDadosDTO.razaoSocial());
        if (razaoSocialValidacao != VALIDACAO_OK){
            erros.add(razaoSocialValidacao);
        }
        return erros;
    }

    public static Fornecedor exibirFornecedorParaUpdate(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK){
            return null;
        }

        FornecedorDAO dao = new FornecedorDAO();
        return dao.readById(Long.parseLong(id));
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(FornecedorDadosDTO fornecedorDadosDTO){
        List<GenericExceptionEnum> mensagens = validarFornecedorInsert(fornecedorDadosDTO);
        if (mensagens.isEmpty()) {
            int resultado = persistirFornecedor(fornecedorDadosDTO);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static List<GenericExceptionEnum> validarFornecedorInsert(FornecedorDadosDTO fornecedorDadosDTO){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(fornecedorDadosDTO.idUsuario());
        if (validacaoIdUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoIdUsuario);
        }

        GenericExceptionEnum validacaoTipoFornecedor = validarTipoFornecedor(fornecedorDadosDTO.tipoFornecedor());
        if(validacaoTipoFornecedor != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoFornecedor);
        }

        GenericExceptionEnum validacaoCnpj = validarCnpj(fornecedorDadosDTO.cnpj(), true);
        if(validacaoCnpj != VALIDACAO_OK && validacaoCnpj != ATRIBUTO_NULL){
            listaDeErros.add(validacaoCnpj);
        }

        GenericExceptionEnum validacaoRazaoSocial = validarRazaoSocial(fornecedorDadosDTO.razaoSocial());
        if(validacaoRazaoSocial != VALIDACAO_OK){
            listaDeErros.add(validacaoRazaoSocial);
        }
        return listaDeErros;
    }

    private static int persistirFornecedor(FornecedorDadosDTO fornecedorDadosDTO){
        FornecedorDAO dao = new FornecedorDAO();
        return dao.insert(fornecedorDadosDTO.construirFornecedor());
    }
}
