package service.fornecedor;

import static exception.ErrosDadosFornecedor.TIPO_FORNECEDOR_INVALIDO;
import static exception.ErrosDadosFornecedor.TIPO_FORNECEDOR_VAZIO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.REGISTROS_NAO_ENCONTRADOS;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGerais.descobrirErroGeral;
import static exception.ErrosGeraisDados.ATRIBUTO_NULL;
import static exception.ErrosGeraisDados.CNPJ_FORMATO_INVALIDO;
import static exception.ErrosGeraisDados.CNPJ_INVALIDO;
import static exception.ErrosGeraisDados.CNPJ_TAMANHO_INVALIDO;
import static exception.ErrosGeraisDados.CNPJ_VAZIO;
import static exception.ErrosGeraisDados.RAZAO_SOCIAL_INVALIDA;
import static exception.ErrosGeraisDados.RAZAO_SOCIAL_TAMANHO_INVALIDO;
import static exception.ErrosGeraisDados.RAZAO_SOCIAL_VAZIA;
import static exception.ErrosGeraisDados.TIPO_USUARIO_INCOMPATIVEL;
import static exception.ErrosGeraisDados.VALIDACAO_OK;

import dao.FornecedorDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import model.Fornecedor;
import model.TiposFornecedor;
import model.TiposUsuario;
import service.ValidacoesComunsService;


public final class FornecedorService {

    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
    private static final int TAMANHO_CNPJ = 14;
    private static final Pattern  PATTERN_CNPJ = Pattern.compile("^[0-9-A-Z]{12}[0-9]{2}$");

    private static final Pattern PATTERN_RAZAO_SOCIAL = Pattern.compile("^[\\p{Script=Latin}0-9&,.;'\\-]+[\\p{Script=Latin}0-9&,.;'\\-\\s]+$");

    private static final int TAMANHO_MAXIMO_RAZAO_SOCIAL = 150;

    //Validação do tipoUsuario
    private static GenericExceptionEnum validarTipoUsuario(String tipoUsuario){
        TiposUsuario tiposUsuario = TiposUsuario.descobrirTipoUsuario(tipoUsuario);
        if(tipoUsuario == null || tiposUsuario != TiposUsuario.FORNECEDOR){
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
    private static GenericExceptionEnum validarCnpj(String cnpj){
        if(cnpj == null || cnpj.isBlank()){
            return CNPJ_VAZIO;
        }

        String cnpjTratado = cnpj.strip().toUpperCase()
                .replace(".", "")
                .replace("-", "")
                .replace("/", "");

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
        return 0;//dao.delete(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static List<Fornecedor> realizarSelect(FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO, List<GenericExceptionEnum> errosEncontrados){
        List<Fornecedor> Fornecedores;

        if(fornecedorDadosDePesquisaDTO == null){
            return lerFornecedores(null, true);
        }

//        errosEncontrados.addAll(validarFornecedorSelect(fornecedorDadosDePesquisaDTO));
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
            return null;//dao.readAll();
        }

        CamposFornecedor clausulaWhere = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.clausulaWhereNome().strip().toLowerCase());
//
//        if(!clausulaWhere.isMultiplosRetornos()){
//            Fornecedor fornecedor = lerFornecedorUnicoRetorno(clausulaWhere, fornecedorDadosDePesquisaDTO.clausulaWhereValor());
//
//            List<Fornecedor> fornecedores = new ArrayList<>();
//
//            if (fornecedor.getId() != (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
//                fornecedores.add(fornecedor);
//            }
//            return fornecedores;
//        }
//        return lerFornecedorMultiplosRetornos(clausulaWhere, fornecedorDadosDePesquisaDTO);
        return null;
    }

//    private static Fornecedor lerFornecedorUnicoRetorno(CamposFornecedor clausulaWhere, String clausulaWhereValor){
//        FornecedorDAO dao = new FornecedorDAO();
//
//        if (clausulaWhere == ID){
//            long id = Long.parseLong(clausulaWhereValor.strip());
//            return dao.readById(id);
//        }
//
//        if (clausulaWhere == ID_USUARIO){
//            long idUsuario = Long.parseLong(clausulaWhereValor.strip());
//            return dao.readByIdUsuario(idUsuario);
//        }
//
//        if (clausulaWhere == CNPJ){
//            String cnpjTratado = clausulaWhereValor.strip();
//            return dao.readByCnpj(cnpjTratado);
//        }
//        return new Fornecedor((long)REGISTRO_NAO_ENCONTRADO.getCodigo(), (long)REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, null);
//    }
//
//    private static List<Fornecedor> lerFornecedorMultiplosRetornos(CamposFornecedor clausulaWhere, FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO){
//        CamposFornecedor orderBy = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.orderBy().strip().toLowerCase());
//
//        FornecedorDAO dao = new FornecedorDAO();
//        if (clausulaWhere == GENERICO){
//            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDTO.sentidoOrderBy());
//        }
//
//        if (clausulaWhere == TIPO_FORNECEDOR){
//            String tipoFornecedorTratado = fornecedorDadosDePesquisaDTO.clausulaWhereValor().strip();
//            return orderBy == GENERICO ? dao.readAllByTipoFornecedor(tipoFornecedorTratado) :
//                    dao.readAllByTipoFornecedorOrderBy(tipoFornecedorTratado, orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDTO.sentidoOrderBy());
//        }
//
//        if(clausulaWhere == RAZAO_SOCIAL){
//            String razaoSocialTratada = fornecedorDadosDePesquisaDTO.clausulaWhereValor().toUpperCase();
//            return orderBy == GENERICO ?
//                    dao.readAllByRazaoSocial(razaoSocialTratada) :
//                    dao.readAllByRazaoSocialOrderBy(razaoSocialTratada, orderBy.getCampoFornecedor(), fornecedorDadosDePesquisaDTO.sentidoOrderBy());
//        }
//        return new ArrayList<>();
//    }
//
//    private static List<GenericExceptionEnum> validarFornecedorSelect(FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDTO){
//        List<GenericExceptionEnum> erros = new ArrayList<>();
//
//        CamposFornecedor clausulaWhere = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.clausulaWhereNome());
//        GenericExceptionEnum clausulaWhereNomeValidacao = ValidacoesComunsService.validarWhere(clausulaWhere);
//        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
//            erros.add(clausulaWhereNomeValidacao);
//            return erros;
//        }
//
//        erros.addAll(validarClausulaWhereValor(fornecedorDadosDePesquisaDTO.clausulaWhereNome(), fornecedorDadosDePesquisaDTO.clausulaWhereValor()));
//
//        CamposFornecedor orderBy = CamposFornecedor.descobrirCampoFornecedor(fornecedorDadosDePesquisaDTO.orderBy());
//        GenericExceptionEnum orderByValidacao = ValidacoesComunsService.validarOrderBy(orderBy);
//        if (orderByValidacao != VALIDACAO_OK){
//            erros.add(orderByValidacao);
//        }
//
//        GenericExceptionEnum ordenacaoValidacao = ValidacoesComunsService.validarSentidoOrderBy(fornecedorDadosDePesquisaDTO.sentidoOrderBy());
//        if (ordenacaoValidacao != VALIDACAO_OK){
//            erros.add(ordenacaoValidacao);
//        }
//        return erros;
//    }
//
//    private static List<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor){
//        List<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();
//
//        GenericExceptionEnum clausulaWhereValorValidacao = null;
//
//        CamposFornecedor clausulaWhere = CamposFornecedor.descobrirCampoFornecedor(clausulaWhereNome.strip().toLowerCase());
//        if (clausulaWhere == GENERICO){
//            return errosNasClausulasWhere;
//        }
//
//        if (clausulaWhere == ID || clausulaWhere == ID_USUARIO){
//            clausulaWhereValorValidacao = ValidacoesComunsService.validarId(clausulaWhereValor);
//        }
//
//        if (clausulaWhere == TIPO_FORNECEDOR){
//            clausulaWhereValorValidacao = validarTipoFornecedor(clausulaWhereValor);
//        }
//
//        if (clausulaWhere == CNPJ){
//            clausulaWhereValorValidacao = validarCnpj(clausulaWhereValor, false);
//        }
//
//        if (clausulaWhere == RAZAO_SOCIAL){
//            clausulaWhereValorValidacao = validarRazaoSocial(clausulaWhereValor);
//        }
//
//        if (clausulaWhereValorValidacao == null){
//            clausulaWhereValorValidacao = WHERE_INVALIDO;
//        }
//
//        if (clausulaWhereValorValidacao != VALIDACAO_OK){
//            errosNasClausulasWhere.add(clausulaWhereValorValidacao);
//        }
//        return errosNasClausulasWhere;
//    }

    //Métodos relacionados ao update
    public static List<GenericExceptionEnum> realizarUpdate(FornecedorParaInsertDTO fornecedorParaInsertDTO){
        List<GenericExceptionEnum> erros = validarUpdate(fornecedorParaInsertDTO);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarFornecedor(fornecedorParaInsertDTO);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarFornecedor(FornecedorParaInsertDTO fornecedorParaInsertDTO){
        FornecedorDAO dao = new FornecedorDAO();
//        return dao.update(fornecedorParaInsertDTO.construirFornecedor());
        return 0;
    }

    private static List<GenericExceptionEnum> validarUpdate(FornecedorParaInsertDTO fornecedorParaInsertDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum tipoFornecedorValidacao = validarTipoFornecedor(fornecedorParaInsertDTO.tipoFornecedor());
        if(tipoFornecedorValidacao != VALIDACAO_OK){
            erros.add(tipoFornecedorValidacao);
        }

        GenericExceptionEnum razaoSocialValidacao = validarRazaoSocial(fornecedorParaInsertDTO.razaoSocial());
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
//        return dao.readById(Long.parseLong(id));
        return null;
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(FornecedorParaInsertDTO fornecedorParaInsertDTO){
        List<GenericExceptionEnum> mensagens = validarFornecedorInsert(fornecedorParaInsertDTO);
        if (mensagens.isEmpty()) {
            int resultado = 2;//persistirFornecedor(fornecedorParaInsertDTO);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    public static List<GenericExceptionEnum> validarFornecedorInsert(FornecedorParaInsertDTO fornecedorParaInsertDTO){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoTipoUsuario = validarTipoUsuario(fornecedorParaInsertDTO.tipoUsuario());
        if (validacaoTipoUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoUsuario);
        }

        GenericExceptionEnum validacaoTipoFornecedor = validarTipoFornecedor(fornecedorParaInsertDTO.tipoFornecedor());
        if(validacaoTipoFornecedor != VALIDACAO_OK){
            listaDeErros.add(validacaoTipoFornecedor);
        }

        GenericExceptionEnum validacaoCnpj = validarCnpj(fornecedorParaInsertDTO.cnpj());
        if(validacaoCnpj != VALIDACAO_OK && validacaoCnpj != ATRIBUTO_NULL){
            listaDeErros.add(validacaoCnpj);
        }

        GenericExceptionEnum validacaoRazaoSocial = validarRazaoSocial(fornecedorParaInsertDTO.razaoSocial());
        if(validacaoRazaoSocial != VALIDACAO_OK){
            listaDeErros.add(validacaoRazaoSocial);
        }
        return listaDeErros;
    }

}
