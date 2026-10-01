package service.empresaDemandante;

import dao.EmpresaDemandanteDAO;
import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import model.EmpresaDemandante;
import model.TiposUsuario;
import model.Usuario;
import service.ValidacoesComunsService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static exception.ErrosDadosEmpresaDemandante.EH_MANDANTE_INVALIDO;
import static exception.ErrosGerais.*;
import static exception.ErrosGeraisDados.*;
import static service.empresaDemandante.CamposEmpresaDemandante.*;

public final class EmpresaDemandanteService {

    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
    private static final int TAMANHO_CNPJ = 14;
    private static final Pattern PATTERN_CNPJ = Pattern.compile("^[0-9-A-Z]{12}[0-9]{2}$");

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
        if(usuario != null && usuario.getId() == (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return ID_USUARIO_NAO_REGISTRADO;
        }

        if(usuario != null && usuario.getTipoUsuario() != TiposUsuario.EMPRESA_DEMANDANTE){
            return TIPO_USUARIO_INCOMPATIVEL;
        }
        return VALIDACAO_OK;
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
        EmpresaDemandanteDAO fornecedorDao = new EmpresaDemandanteDAO();
        EmpresaDemandante fornecedor = fornecedorDao.readByCnpj(cpnj);
        if(fornecedor == null || fornecedor.getId() == (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return CNPJ_INVALIDO;
        }

        EmpresaDemandanteDAO empresaDemandanteDAO = new EmpresaDemandanteDAO();
        EmpresaDemandante empresaDemandante = empresaDemandanteDAO.readByCnpj(cpnj);
        if(empresaDemandante == null || empresaDemandante.getId() == (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
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

    //Validação do eh mandante
    private static GenericExceptionEnum validarEhMandante(String ehMandante){
        if(ehMandante == null || ehMandante.isBlank()){
            return EH_MANDANTE_INVALIDO;
        }

        if("true".equalsIgnoreCase(ehMandante.toLowerCase().trim()) || "false".equalsIgnoreCase(ehMandante.toLowerCase().trim())){
            return VALIDACAO_OK;
        }
        return EH_MANDANTE_INVALIDO;
    }

    //Métodos relacionados ao delete
    public static GenericExceptionEnum realizarDelete(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK) {
            return ERRO_GENERICO;
        }

        int qtdLinhasDeletadas = deletarEmpresaDemandante(id);
        if(qtdLinhasDeletadas > 0){
            return SUCESSO;
        }
        return descobrirErroGeral(qtdLinhasDeletadas);
    }

    private static int deletarEmpresaDemandante(String id){
        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();
        return dao.delete(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static List<EmpresaDemandante> realizarSelect(EmpresaDemandanteDadosDePesquisaDTO empresaDemandanteDadosDePesquisaDto, List<GenericExceptionEnum> errosEncontrados){
        List<EmpresaDemandante> EmpresasDemandantes;

        if(empresaDemandanteDadosDePesquisaDto == null){
            return lerEmpresasDemandantes(null, true);
        }

        errosEncontrados.addAll(validarEmpresaDemandanteSelect(empresaDemandanteDadosDePesquisaDto));
        if (!errosEncontrados.isEmpty()){
            return lerEmpresasDemandantes(empresaDemandanteDadosDePesquisaDto,true);
        }

        EmpresasDemandantes = lerEmpresasDemandantes(empresaDemandanteDadosDePesquisaDto,false);
        if (EmpresasDemandantes.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return EmpresasDemandantes;
    }

    private static List<EmpresaDemandante> lerEmpresasDemandantes(EmpresaDemandanteDadosDePesquisaDTO empresaDemandanteDadosDePesquisaDto, boolean erroEncontrado){
        if (erroEncontrado){
            EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();
            return dao.readAll();
        }

        CamposEmpresaDemandante clausulaWhere = CamposEmpresaDemandante.descobrirCampoEmpresaDemandante(empresaDemandanteDadosDePesquisaDto.clausulaWhereNome().strip().toLowerCase());

        if(!clausulaWhere.isMultiplosRetornos()){
            EmpresaDemandante fornecedor = lerEmpresaDemandanteUnicoRetorno(clausulaWhere, empresaDemandanteDadosDePesquisaDto.clausulaWhereValor());

            List<EmpresaDemandante> fornecedores = new ArrayList<>();

            if (fornecedor.getId() != (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
                fornecedores.add(fornecedor);
            }
            return fornecedores;
        }
        return lerEmpresaDemandanteMultiplosRetornos(clausulaWhere, empresaDemandanteDadosDePesquisaDto);
    }

    private static EmpresaDemandante lerEmpresaDemandanteUnicoRetorno(CamposEmpresaDemandante clausulaWhere, String clausulaWhereValor){
        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();

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
        return new EmpresaDemandante((long)REGISTRO_NAO_ENCONTRADO.getCodigo(), (long)REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);
    }

    private static List<EmpresaDemandante> lerEmpresaDemandanteMultiplosRetornos(CamposEmpresaDemandante clausulaWhere, EmpresaDemandanteDadosDePesquisaDTO empresaDemandanteDadosDePesquisaDto){
        CamposEmpresaDemandante orderBy = CamposEmpresaDemandante.descobrirCampoEmpresaDemandante(empresaDemandanteDadosDePesquisaDto.orderBy().strip().toLowerCase());

//        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();
//        if (clausulaWhere == GENERICO){
//            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoEmpresaDemandante(), empresaDemandanteDadosDePesquisaDto.sentidoOrderBy());
//        }
//
//        if (clausulaWhere == EH_MANDANTE){
//            String tipoEmpresaDemandanteTratado = empresaDemandanteDadosDePesquisaDto.clausulaWhereValor().strip();
//            return orderBy == GENERICO ? dao.readAllByTipoEmpresaDemandante(tipoEmpresaDemandanteTratado) :
//                    dao.readAllByTipoEmpresaDemandanteOrderBy(tipoEmpresaDemandanteTratado, orderBy.getCampoEmpresaDemandante(), empresaDemandanteDadosDePesquisaDto.sentidoOrderBy());
//        }
//
//        if(clausulaWhere == RAZAO_SOCIAL){
//            String razaoSocialTratada = empresaDemandanteDadosDePesquisaDto.clausulaWhereValor().toUpperCase();
//            return orderBy == GENERICO ?
//                    dao.readAllByRazaoSocial(razaoSocialTratada) :
//                    dao.readAllByRazaoSocialOrderBy(razaoSocialTratada, orderBy.getCampoEmpresaDemandante(), empresaDemandanteDadosDePesquisaDto.sentidoOrderBy());
//        }
        return new ArrayList<>();
    }

    private static List<GenericExceptionEnum> validarEmpresaDemandanteSelect(EmpresaDemandanteDadosDePesquisaDTO empresaDemandanteDadosDePesquisaDto){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        CamposEmpresaDemandante clausulaWhere = CamposEmpresaDemandante.descobrirCampoEmpresaDemandante(empresaDemandanteDadosDePesquisaDto.clausulaWhereNome());
        GenericExceptionEnum clausulaWhereNomeValidacao = ValidacoesComunsService.validarWhere(clausulaWhere);
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(empresaDemandanteDadosDePesquisaDto.clausulaWhereNome(), empresaDemandanteDadosDePesquisaDto.clausulaWhereValor()));

        CamposEmpresaDemandante orderBy = CamposEmpresaDemandante.descobrirCampoEmpresaDemandante(empresaDemandanteDadosDePesquisaDto.orderBy());
        GenericExceptionEnum orderByValidacao = ValidacoesComunsService.validarOrderBy(orderBy);
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = ValidacoesComunsService.validarSentidoOrderBy(empresaDemandanteDadosDePesquisaDto.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static List<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor){
        List<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposEmpresaDemandante clausulaWhere = CamposEmpresaDemandante.descobrirCampoEmpresaDemandante(clausulaWhereNome.strip().toLowerCase());
        if (clausulaWhere == GENERICO){
            return errosNasClausulasWhere;
        }

        if (clausulaWhere == ID || clausulaWhere == ID_USUARIO){
            clausulaWhereValorValidacao = ValidacoesComunsService.validarId(clausulaWhereValor);
        }

        if (clausulaWhere == CNPJ){
            clausulaWhereValorValidacao = validarCnpj(clausulaWhereValor, false);
        }

        if (clausulaWhere == RAZAO_SOCIAL){
            clausulaWhereValorValidacao = validarRazaoSocial(clausulaWhereValor);
        }

        if (clausulaWhere == EH_MANDANTE){
            clausulaWhereValorValidacao = validarEhMandante(clausulaWhereValor);
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
    public static List<GenericExceptionEnum> realizarUpdate(EmpresaDemandanteDadosDTO empresaDemandanteDadosDTO){
        List<GenericExceptionEnum> erros = validarUpdate(empresaDemandanteDadosDTO);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarEmpresaDemandante(empresaDemandanteDadosDTO);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarEmpresaDemandante(EmpresaDemandanteDadosDTO empresaDemandanteDadosDTO){
        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();
        return dao.update(empresaDemandanteDadosDTO.construirEmpresaDemandante());
    }

    private static List<GenericExceptionEnum> validarUpdate(EmpresaDemandanteDadosDTO empresaDemandanteDadosDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum ehMandanteValidacao = validarEhMandante(empresaDemandanteDadosDTO.ehMandante());
        if(ehMandanteValidacao != VALIDACAO_OK){
            erros.add(ehMandanteValidacao);
        }

        GenericExceptionEnum razaoSocialValidacao = validarRazaoSocial(empresaDemandanteDadosDTO.razaoSocial());
        if (razaoSocialValidacao != VALIDACAO_OK){
            erros.add(razaoSocialValidacao);
        }
        return erros;
    }

    public static EmpresaDemandante exibirEmpresaDemandanteParaUpdate(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK){
            return null;
        }

        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();
        return dao.readById(Long.parseLong(id));
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(EmpresaDemandanteDadosDTO empresaDemandanteDadosDTO){
        List<GenericExceptionEnum> mensagens = validarEmpresaDemandanteInsert(empresaDemandanteDadosDTO);
        if (mensagens.isEmpty()) {
            int resultado = persistirEmpresaDemandante(empresaDemandanteDadosDTO);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static List<GenericExceptionEnum> validarEmpresaDemandanteInsert(EmpresaDemandanteDadosDTO empresaDemandanteDadosDTO){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(empresaDemandanteDadosDTO.idUsuario());
        if (validacaoIdUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoIdUsuario);
        }

        GenericExceptionEnum ehMandanteValidacao = validarEhMandante(empresaDemandanteDadosDTO.ehMandante());
        if(ehMandanteValidacao != VALIDACAO_OK){
            listaDeErros.add(ehMandanteValidacao);
        }

        GenericExceptionEnum validacaoCnpj = validarCnpj(empresaDemandanteDadosDTO.cnpj(), true);
        if(validacaoCnpj != VALIDACAO_OK && validacaoCnpj != ATRIBUTO_NULL){
            listaDeErros.add(validacaoCnpj);
        }

        GenericExceptionEnum validacaoRazaoSocial = validarRazaoSocial(empresaDemandanteDadosDTO.razaoSocial());
        if(validacaoRazaoSocial != VALIDACAO_OK){
            listaDeErros.add(validacaoRazaoSocial);
        }
        return listaDeErros;
    }

    private static int persistirEmpresaDemandante(EmpresaDemandanteDadosDTO empresaDemandanteDadosDTO){
        EmpresaDemandanteDAO dao = new EmpresaDemandanteDAO();
        return dao.insert(empresaDemandanteDadosDTO.construirEmpresaDemandante());
    }


}
