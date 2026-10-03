package service.profissional;

import static exception.ErrosDadosProfissional.*;
import static exception.ErrosGerais.*;
import static exception.ErrosGeraisDados.*;
import static service.profissional.CamposProfissional.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.Fornecedor;
import model.Profissional;
import model.TiposUsuario;
import model.Usuario;
import dao.FornecedorDAO;
import dao.ProfissionalDAO;
import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;
import service.ValidacoesComunsService;

public final class ProfissionalService {

    //Constantes para evitar valores mágicos ou instancia desnecessária de objeto
    private static final Pattern PATTERN_PROFISSAO = Pattern.compile("^[\\p{Script=Latin}0-9,.\\s\\-/]+$");
    private static final int TAMANHO_MAXIMO_PROFISSAO = 100;

    private static final int TAMANHO_CPF = 11;
    private static final Pattern PATTERN_CPF = Pattern.compile("^[0-9]{11}$");

    //Validações

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

        if(usuario != null && usuario.getTipoUsuario() != TiposUsuario.PROFISSIONAL){
            return TIPO_USUARIO_INCOMPATIVEL;
        }
        return VALIDACAO_OK;
    }

    //Validação do id fornecedor
    private static GenericExceptionEnum validarIdFornecedor(String idFornecedor){
        if(ValidacoesComunsService.validarId(idFornecedor) != VALIDACAO_OK){
            return ID_FORNECEDOR_INVALIDO;
        }

        long idFornecedorConvertido = Long.parseLong(idFornecedor);

        FornecedorDAO dao = new FornecedorDAO();
        Fornecedor fornecedor = dao.readById(idFornecedorConvertido);
        if(fornecedor != null && fornecedor.getId() == (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return ID_FORNECEDOR_NAO_REGISTRADO;
        }
        return VALIDACAO_OK;
    }

    //Validações da profissão
    private static GenericExceptionEnum validarProfissao(String profissao){
        if(profissao == null || profissao.isBlank()){
            return PROFISSAO_VAZIA;
        }

        String profissaoTratada = profissao.strip();
        if(profissaoTratada.length() > TAMANHO_MAXIMO_PROFISSAO){
            return PROFISSAO_TAMANHO_INVALIDO;
        }

        if(profissaoTratada.replace(profissaoTratada.charAt(0), ' ').isBlank()){
            return PROFISSAO_INVALIDA;
        }

        return validarFormatoProfissao(profissaoTratada);
    }

    private static GenericExceptionEnum validarFormatoProfissao(String profissao){
        return PATTERN_PROFISSAO.matcher(profissao).matches() ? VALIDACAO_OK : PROFISSAO_INVALIDA;
    }

    //Validações do cpf
    private static GenericExceptionEnum validarCpf(String cpf, boolean insert){
        if(cpf == null || cpf.isBlank()){
            return CPF_VAZIO;
        }

        String cpfTratado = cpf.strip();
        if(cpfTratado.length() != TAMANHO_CPF){
            return CPF_TAMANHO_INVALIDO;
        }


        if(cpfTratado.replace(cpfTratado.charAt(0), ' ').isBlank()){
            return CPF_INVALIDO;
        }

        GenericExceptionEnum validacaoFormatoCpf = validarFormatoCpf(cpfTratado);
        if(validacaoFormatoCpf != VALIDACAO_OK){
            return validacaoFormatoCpf;
        }

        GenericExceptionEnum validacaoCondicaoExistenciaCpf = validarCondicaoDeExistenciaCpf(cpfTratado);
        if(validacaoCondicaoExistenciaCpf != VALIDACAO_OK){
            return validacaoCondicaoExistenciaCpf;
        }

        return insert ? validarCpfUnico(cpfTratado) : VALIDACAO_OK;
    }

    private static GenericExceptionEnum validarCpfUnico(String cpf){
        ProfissionalDAO dao = new ProfissionalDAO();
        Profissional profissional = dao.readByCpf(cpf);

        return profissional == null || profissional.getId() == (long)REGISTRO_NAO_ENCONTRADO.getCodigo() ? VALIDACAO_OK : CPF_INVALIDO;
    }

    private static GenericExceptionEnum validarFormatoCpf(String cpf){
        return PATTERN_CPF.matcher(cpf).matches() ? VALIDACAO_OK : CPF_NAO_NUMERICO;
    }

    private static GenericExceptionEnum validarCondicaoDeExistenciaCpf(String cpf){

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

    //Métodos relacionados ao delete
    public static GenericExceptionEnum realizarDelete(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK) {
            return ERRO_GENERICO;
        }

        int qtdLinhasDeletadas = deletarProfissional(id);
        if(qtdLinhasDeletadas > 0){
            return SUCESSO;
        }
        return descobrirErroGeral(qtdLinhasDeletadas);
    }

    private static int deletarProfissional(String id){
        ProfissionalDAO dao = new ProfissionalDAO();
        return dao.delete(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static List<Profissional> realizarSelect(ProfissionalDadosDePesquisaDTO profissionalDadosDePesquisaDTO, List<GenericExceptionEnum> errosEncontrados){
        List<Profissional> usuarios;

        if(profissionalDadosDePesquisaDTO == null){
            return lerProfissional(null, true);
        }

        errosEncontrados.addAll(validarProfissionalSelect(profissionalDadosDePesquisaDTO));
        if (!errosEncontrados.isEmpty()){
            return lerProfissional(profissionalDadosDePesquisaDTO,true);
        }

        usuarios = lerProfissional(profissionalDadosDePesquisaDTO,false);
        if (usuarios.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return usuarios;
    }

    private static List<Profissional> lerProfissional(ProfissionalDadosDePesquisaDTO profissionalDadosDePesquisaDTO, boolean erroEncontrado){
        ProfissionalDAO dao = new ProfissionalDAO();
        if (erroEncontrado){
            return dao.readAll();
        }

        CamposProfissional clausulaWhere = CamposProfissional.descobrirCampoProfissional(profissionalDadosDePesquisaDTO.clausulaWhereNome().strip().toLowerCase());

        if(!clausulaWhere.isMultiplosRetornos()){
            Profissional profissional= lerProfissionalUnicoRetorno(clausulaWhere, profissionalDadosDePesquisaDTO.clausulaWhereValor());

            List<Profissional> profissionais = new ArrayList<>();

            if (profissional.getId() != (long)REGISTRO_NAO_ENCONTRADO.getCodigo()){
                profissionais.add(profissional);
            }
            return profissionais;
        }
        return lerProfissionalMultiplosRetorno(clausulaWhere, profissionalDadosDePesquisaDTO);
    }

    private static Profissional lerProfissionalUnicoRetorno(CamposProfissional clausulaWhere, String clausulaWhereValor){
        ProfissionalDAO dao = new ProfissionalDAO();

        if (clausulaWhere == ID){
            long id = Long.parseLong(clausulaWhereValor.strip());
            return dao.readById(id);
        }

        if (clausulaWhere == ID_USUARIO){
            long idUsuario = Long.parseLong(clausulaWhereValor.strip());
            return dao.readByIdUsuario(idUsuario);
        }

        if (clausulaWhere == CPF){
            String cpf = clausulaWhereValor.strip();
            return dao.readByCpf(cpf);
        }

        return new Profissional((long)REGISTRO_NAO_ENCONTRADO.getCodigo(), (long)REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, (long)REGISTRO_NAO_ENCONTRADO.getCodigo());
    }

    private static List<Profissional> lerProfissionalMultiplosRetorno(CamposProfissional clausulaWhere, ProfissionalDadosDePesquisaDTO profissionalDadosDePesquisaDTO){
        CamposProfissional orderBy = CamposProfissional.descobrirCampoProfissional(profissionalDadosDePesquisaDTO.orderBy().strip().toLowerCase());

        ProfissionalDAO dao = new ProfissionalDAO();
        if (clausulaWhere == GENERICO){
            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoProfissional(), profissionalDadosDePesquisaDTO.sentidoOrderBy());
        }

        if (clausulaWhere == PROFISSAO){
            String profissaoTratada = profissionalDadosDePesquisaDTO.clausulaWhereValor().strip();
            return orderBy == GENERICO ?
                                dao.readAllByIdProfissao(profissaoTratada) :
                                dao.readAllByIdProfissaoOrderBy(profissaoTratada, orderBy.getCampoProfissional(), profissionalDadosDePesquisaDTO.sentidoOrderBy());
        }

        if(clausulaWhere == ID_FORNECEDOR){
            long idFornecedor = Long.parseLong(profissionalDadosDePesquisaDTO.clausulaWhereValor().strip());
            return orderBy == GENERICO ?
                            dao.readAllByIdFornecedor(idFornecedor) :
                            dao.readAllByIdFornecedorOrderBy(idFornecedor, orderBy.getCampoProfissional(), profissionalDadosDePesquisaDTO.sentidoOrderBy());
        }
        return new ArrayList<>();
    }

    private static List<GenericExceptionEnum> validarProfissionalSelect(ProfissionalDadosDePesquisaDTO profissionalDadosDePesquisaDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        CamposProfissional clausulaWhere = CamposProfissional.descobrirCampoProfissional(profissionalDadosDePesquisaDTO.clausulaWhereNome());
        GenericExceptionEnum clausulaWhereNomeValidacao = ValidacoesComunsService.validarWhere(clausulaWhere);
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(profissionalDadosDePesquisaDTO.clausulaWhereNome(), profissionalDadosDePesquisaDTO.clausulaWhereValor()));

        CamposProfissional orderBy = CamposProfissional.descobrirCampoProfissional(profissionalDadosDePesquisaDTO.orderBy());
        GenericExceptionEnum orderByValidacao = ValidacoesComunsService.validarOrderBy(orderBy);
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = ValidacoesComunsService.validarSentidoOrderBy(profissionalDadosDePesquisaDTO.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static List<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor){
        List<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposProfissional clausulaWhere = CamposProfissional.descobrirCampoProfissional(clausulaWhereNome.strip().toLowerCase());
        if (clausulaWhere == GENERICO){
            return errosNasClausulasWhere;
        }

        if (clausulaWhere == ID || clausulaWhere == ID_USUARIO || clausulaWhere == ID_FORNECEDOR){
            clausulaWhereValorValidacao = ValidacoesComunsService.validarId(clausulaWhereValor);
        }

        if (clausulaWhere == PROFISSAO){
            clausulaWhereValorValidacao = validarProfissao(clausulaWhereValor);
        }

        if (clausulaWhere == CPF){
            clausulaWhereValorValidacao = validarCpf(clausulaWhereValor, false);
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
    public static List<GenericExceptionEnum> realizarUpdate(ProfissionalParaInsertDTO profissionalParaInsertDTO){
        List<GenericExceptionEnum> erros = validarUpdate(profissionalParaInsertDTO);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarProfissional(profissionalParaInsertDTO);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarProfissional(ProfissionalParaInsertDTO profissionalParaInsertDTO){
        ProfissionalDAO dao = new ProfissionalDAO();
        return dao.update(profissionalParaInsertDTO.construirProfissional());
    }

    private static List<GenericExceptionEnum> validarUpdate(ProfissionalParaInsertDTO profissionalParaInsertDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum profissaoValidacao = validarProfissao(profissionalParaInsertDTO.profissao());
        if(profissaoValidacao != VALIDACAO_OK){
            erros.add(profissaoValidacao);
        }

        GenericExceptionEnum idFornecedorValidacao = validarIdFornecedor(profissionalParaInsertDTO.idFornecedor());
        if (idFornecedorValidacao != VALIDACAO_OK){
            erros.add(idFornecedorValidacao);
        }
        return erros;
    }

    public static Profissional exibirProfissionalParaUpdate(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK){
            return null;
        }

        ProfissionalDAO dao = new ProfissionalDAO();
        return dao.readById(Long.parseLong(id));
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(ProfissionalParaInsertDTO profissionalParaInsertDTO){
        List<GenericExceptionEnum> mensagens = validarProfissionalInsert(profissionalParaInsertDTO);
        if (mensagens.isEmpty()) {
            int resultado = persistirProfissional(profissionalParaInsertDTO);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static List<GenericExceptionEnum> validarProfissionalInsert(ProfissionalParaInsertDTO profissionalParaInsertDTO){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(profissionalParaInsertDTO.idUsuario());
        if (validacaoIdUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoIdUsuario);
        }

        GenericExceptionEnum validacaoProfissao = validarProfissao(profissionalParaInsertDTO.profissao());
        if(validacaoProfissao != VALIDACAO_OK){
            listaDeErros.add(validacaoProfissao);
        }

        GenericExceptionEnum validacaoCpf = validarCpf(profissionalParaInsertDTO.cpf(), true);
        if(validacaoCpf != VALIDACAO_OK && validacaoCpf != ATRIBUTO_NULL){
            listaDeErros.add(validacaoCpf);
        }

        GenericExceptionEnum validacaoIdFornecedor = validarIdFornecedor(profissionalParaInsertDTO.idFornecedor());
        if (validacaoIdFornecedor != VALIDACAO_OK){
            listaDeErros.add(validacaoIdFornecedor);
        }
        return listaDeErros;
    }

    private static int persistirProfissional(ProfissionalParaInsertDTO profissionalParaInsertDTO){
        ProfissionalDAO dao = new ProfissionalDAO();
        return dao.insert(profissionalParaInsertDTO.construirProfissional());
    }

}
