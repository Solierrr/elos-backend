package service.telefone;

import dao.TelefoneDAO;
import dao.UsuarioDAO;
import exception.ErrosGerais;
import exception.GenericExceptionEnum;

import model.Telefone;
import model.TiposTelefone;
import model.Usuario;
import service.ValidacoesComunsService;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static service.telefone.CamposTelefone.*;
import static exception.ErrosDadosTelefone.*;
import static exception.ErrosGerais.*;
import static exception.ErrosGerais.REGISTROS_NAO_ENCONTRADOS;
import static exception.ErrosGerais.SUCESSO;
import static exception.ErrosGeraisDados.*;

public class TelefoneService {

    //Constantes para evitar valores mágicos ou instancia desnecessária de objetos
    private static final Pattern PATTERN_TELEFONE = Pattern.compile("^[0-9]{2}(9)?[0-9]{8}$");
    private static final int TAMANHO_TELEFONE = 11;

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
        return VALIDACAO_OK;
    }

    //Validações do telefone
    private static GenericExceptionEnum validarTelefone(String telefone, boolean insert){
        GenericExceptionEnum validacaoBase = validarTelefoneBase(telefone);
        if(validacaoBase != VALIDACAO_OK){
            return validacaoBase;
        }

        if(!insert){
            return VALIDACAO_OK;
        }
        return validarTelefoneUnico(telefone.strip());
    }

    private static GenericExceptionEnum validarTelefoneBase(String telefone){
        if(telefone == null || telefone.isBlank()){
            return TELEFONE_VAZIO;
        }

        String telefoneTratado = telefone.strip();
        if(telefoneTratado.length() != TAMANHO_TELEFONE){
            return TELEFONE_INVALIDO;
        }
        return PATTERN_TELEFONE.matcher(telefoneTratado).matches() ?  VALIDACAO_OK : TELEFONE_INVALIDO;
    }

    private static GenericExceptionEnum validarTelefoneUnico(String numeroTelefone){
        TelefoneDAO dao = new TelefoneDAO();

        Telefone telefone = dao.readByTelefone(numeroTelefone);
        if(telefone != null && telefone.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
            return TELEFONE_INVALIDO;
        }
        return VALIDACAO_OK;
    }

    //Validação do tipo
    private static GenericExceptionEnum validarTipo(String tipo){
        if(tipo == null || tipo.isBlank()){
            return TIPO_VAZIO;
        }
        return TiposTelefone.descobrirTipoTelefone(tipo) == null ? TIPO_INVALIDO : VALIDACAO_OK;
    }

    //Validação do principal
    private static GenericExceptionEnum validarPrincipal(String pricipal){
        if(pricipal == null || pricipal.isBlank()){
            return PRINCIPAL_INVALIDO;
        }

        if("true".equalsIgnoreCase(pricipal.toLowerCase().trim()) || "false".equalsIgnoreCase(pricipal.toLowerCase().trim())){
            return VALIDACAO_OK;
        }
        return PRINCIPAL_INVALIDO;
    }

    //Métodos relacionados ao delete
    public static GenericExceptionEnum realizarDelete(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK) {
            return ERRO_GENERICO;
        }

        int qtdLinhasDeletadas = deletarTelefone(id);
        if(qtdLinhasDeletadas > 0){
            return SUCESSO;
        }
        return descobrirErroGeral(qtdLinhasDeletadas);
    }

    private static int deletarTelefone(String id){
        TelefoneDAO dao = new TelefoneDAO();
        return dao.delete(Long.parseLong(id.strip()));
    }

    //Métodos relacionados ao select
    public static List<Telefone> realizarSelect(TelefoneDadosDePesquisaDTO telefoneDadosDePesquisaDTO, List<GenericExceptionEnum> errosEncontrados){
        List<Telefone> telefones;

        if(telefoneDadosDePesquisaDTO == null){
            return lerTelefonees(null, true);
        }

        errosEncontrados.addAll(validarTelefoneSelect(telefoneDadosDePesquisaDTO));
        if (!errosEncontrados.isEmpty()){
            return lerTelefonees(telefoneDadosDePesquisaDTO,true);
        }

        telefones = lerTelefonees(telefoneDadosDePesquisaDTO,false);
        if (telefones.isEmpty()){
            errosEncontrados.add(REGISTROS_NAO_ENCONTRADOS);
        }
        return telefones;
    }

    private static List<Telefone> lerTelefonees(TelefoneDadosDePesquisaDTO telefoneDadosDePesquisaDTO, boolean erroEncontrado){
        if (erroEncontrado){
            TelefoneDAO dao = new TelefoneDAO();
            return dao.readAll();
        }

        CamposTelefone clausulaWhere = CamposTelefone.descobrirCampoTelefone(telefoneDadosDePesquisaDTO.clausulaWhereNome().strip().toLowerCase());

        if(!clausulaWhere.isMultiplosRetornos()){
            Telefone telefone = lerTelefoneUnicoRetorno(clausulaWhere, telefoneDadosDePesquisaDTO.clausulaWhereValor());

            List<Telefone> telefonees = new ArrayList<>();

            if (telefone.getId() != REGISTRO_NAO_ENCONTRADO.getCodigo()){
                telefonees.add(telefone);
            }
            return telefonees;
        }
        return lerTelefoneMultiplosRetornos(clausulaWhere, telefoneDadosDePesquisaDTO);
    }

    private static Telefone lerTelefoneUnicoRetorno(CamposTelefone clausulaWhere, String clausulaWhereValor){
        TelefoneDAO dao = new TelefoneDAO();

        if (clausulaWhere == ID){
            long id = Long.parseLong(clausulaWhereValor.strip());
            return dao.readById(id);
        }

//        if (clausulaWhere == ID_USUARIO){
//            long idUsuario = Long.parseLong(clausulaWhereValor.strip());
//            return dao.readByIdUsuario(idUsuario);
//        }
//
//        if (clausulaWhere == CNPJ){
//            String cnpjTratado = clausulaWhereValor.strip();
//            return dao.readByCnpj(cnpjTratado);
//        }
        return new Telefone((long)REGISTRO_NAO_ENCONTRADO.getCodigo(),(long) REGISTRO_NAO_ENCONTRADO.getCodigo(), null, null, false);
    }

    private static List<Telefone> lerTelefoneMultiplosRetornos(CamposTelefone clausulaWhere, TelefoneDadosDePesquisaDTO telefoneDadosDePesquisaDTO){
        CamposTelefone orderBy = CamposTelefone.descobrirCampoTelefone(telefoneDadosDePesquisaDTO.orderBy().strip().toLowerCase());

        TelefoneDAO dao = new TelefoneDAO();
//        if (clausulaWhere == GENERICO){
//            return orderBy == GENERICO ? dao.readAll() : dao.readAllOrderBy(orderBy.getCampoTelefone(), telefoneDadosDePesquisaDTO.sentidoOrderBy());
//        }
//
//        if (clausulaWhere == TIPO_FORNECEDOR){
//            String tipoTelefoneTratado = telefoneDadosDePesquisaDTO.clausulaWhereValor().strip();
//            return orderBy == GENERICO ? dao.readAllByTipoTelefone(tipoTelefoneTratado) :
//                    dao.readAllByTipoTelefoneOrderBy(tipoTelefoneTratado, orderBy.getCampoTelefone(), telefoneDadosDePesquisaDTO.sentidoOrderBy());
//        }
//
//        if(clausulaWhere == RAZAO_SOCIAL){
//            String razaoSocialTratada = telefoneDadosDePesquisaDTO.clausulaWhereValor().toUpperCase();
//            return orderBy == GENERICO ?
//                    dao.readAllByRazaoSocial(razaoSocialTratada) :
//                    dao.readAllByRazaoSocialOrderBy(razaoSocialTratada, orderBy.getCampoTelefone(), telefoneDadosDePesquisaDTO.sentidoOrderBy());
//        }
        return new ArrayList<>();
    }

    private static List<GenericExceptionEnum> validarTelefoneSelect(TelefoneDadosDePesquisaDTO telefoneDadosDePesquisaDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        CamposTelefone clausulaWhere = CamposTelefone.descobrirCampoTelefone(telefoneDadosDePesquisaDTO.clausulaWhereNome());
        GenericExceptionEnum clausulaWhereNomeValidacao = ValidacoesComunsService.validarWhere(clausulaWhere);
        if (clausulaWhereNomeValidacao != VALIDACAO_OK){
            erros.add(clausulaWhereNomeValidacao);
            return erros;
        }

        erros.addAll(validarClausulaWhereValor(telefoneDadosDePesquisaDTO.clausulaWhereNome(), telefoneDadosDePesquisaDTO.clausulaWhereValor()));

        CamposTelefone orderBy = CamposTelefone.descobrirCampoTelefone(telefoneDadosDePesquisaDTO.orderBy());
        GenericExceptionEnum orderByValidacao = ValidacoesComunsService.validarOrderBy(orderBy);
        if (orderByValidacao != VALIDACAO_OK){
            erros.add(orderByValidacao);
        }

        GenericExceptionEnum ordenacaoValidacao = ValidacoesComunsService.validarSentidoOrderBy(telefoneDadosDePesquisaDTO.sentidoOrderBy());
        if (ordenacaoValidacao != VALIDACAO_OK){
            erros.add(ordenacaoValidacao);
        }
        return erros;
    }

    private static List<GenericExceptionEnum> validarClausulaWhereValor(String clausulaWhereNome, String clausulaWhereValor){
        List<GenericExceptionEnum> errosNasClausulasWhere = new ArrayList<>();

        GenericExceptionEnum clausulaWhereValorValidacao = null;

        CamposTelefone clausulaWhere = CamposTelefone.descobrirCampoTelefone(clausulaWhereNome.strip().toLowerCase());
        if (clausulaWhere == GENERICO){
            return errosNasClausulasWhere;
        }

        if (clausulaWhere == ID || clausulaWhere == ID_USUARIO){
            clausulaWhereValorValidacao = ValidacoesComunsService.validarId(clausulaWhereValor);
        }

        if (clausulaWhere == ID_USUARIO){
            clausulaWhereValorValidacao = validarIdUsuario(clausulaWhereValor);
        }

        if (clausulaWhere == TELEFONE){
            clausulaWhereValorValidacao = validarTelefone(clausulaWhereValor, false);
        }

        if (clausulaWhere == TIPO){
            clausulaWhereValorValidacao = validarTipo(clausulaWhereValor);
        }

        if (clausulaWhere == PRINCIPAL){
            clausulaWhereValorValidacao = validarPrincipal(clausulaWhereValor);
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
    public static List<GenericExceptionEnum> realizarUpdate(TelefoneDadosDTO telefoneDadosDTO){
        List<GenericExceptionEnum> erros = validarUpdate(telefoneDadosDTO);
        if(!erros.isEmpty()){
            return erros;
        }

        int qtdLinhasAlteradas = atualizarTelefone(telefoneDadosDTO);
        if(qtdLinhasAlteradas < 1){
            erros.add(ErrosGerais.descobrirErroGeral(qtdLinhasAlteradas));
        }
        return erros;
    }

    private static int atualizarTelefone(TelefoneDadosDTO telefoneDadosDTO){
        TelefoneDAO dao = new TelefoneDAO();
        return dao.update(telefoneDadosDTO.construirTelefone());
    }

    private static List<GenericExceptionEnum> validarUpdate(TelefoneDadosDTO telefoneDadosDTO){
        List<GenericExceptionEnum> erros = new ArrayList<>();

        GenericExceptionEnum principalValidacao = validarPrincipal(telefoneDadosDTO.principal());
        if (principalValidacao != VALIDACAO_OK){
            erros.add(principalValidacao);
        }
        return erros;
    }

    public static Telefone exibirTelefoneParaUpdate(String id){
        if(ValidacoesComunsService.validarId(id) != VALIDACAO_OK){
            return null;
        }

        TelefoneDAO dao = new TelefoneDAO();
        return dao.readById(Long.parseLong(id));
    }

    //Métodos relacionados ao insert
    public static List<GenericExceptionEnum> realizarInsert(TelefoneDadosDTO telefoneDadosDTO){
        List<GenericExceptionEnum> mensagens = validarTelefoneInsert(telefoneDadosDTO);
        if (mensagens.isEmpty()) {
            int resultado = persistirTelefone(telefoneDadosDTO);
            if (resultado < 1) {
                mensagens.add(ErrosGerais.descobrirErroGeral(resultado));
            }
        }
        return mensagens;
    }

    private static List<GenericExceptionEnum> validarTelefoneInsert(TelefoneDadosDTO telefoneDadosDTO){
        List<GenericExceptionEnum> listaDeErros = new ArrayList<>();

        GenericExceptionEnum validacaoIdUsuario = validarIdUsuario(telefoneDadosDTO.idUsuario());
        if (validacaoIdUsuario != VALIDACAO_OK){
            listaDeErros.add(validacaoIdUsuario);
        }

        GenericExceptionEnum validacaoTipo = validarTipo(telefoneDadosDTO.tipo());
        if(validacaoTipo != VALIDACAO_OK){
            listaDeErros.add(validacaoTipo);
        }

        GenericExceptionEnum validacaoTelefone = validarTelefone(telefoneDadosDTO.telefone(), true);
        if(validacaoTelefone != VALIDACAO_OK && validacaoTelefone != ATRIBUTO_NULL){
            listaDeErros.add(validacaoTelefone);
        }

        GenericExceptionEnum validacaoPrincipal = validarTipo(telefoneDadosDTO.principal());
        if(validacaoPrincipal != VALIDACAO_OK){
            listaDeErros.add(validacaoPrincipal);
        }
        return listaDeErros;
    }

    private static int persistirTelefone(TelefoneDadosDTO telefoneDadosDTO){
        TelefoneDAO dao = new TelefoneDAO();
        return dao.insert(telefoneDadosDTO.construirTelefone());
    }
}
