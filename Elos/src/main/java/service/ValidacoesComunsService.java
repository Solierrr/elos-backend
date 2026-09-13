package service;

import exception.GenericExceptionEnum;

import static dao.AcoesInstrucao.*;
import static exception.ErrosGeraisDados.*;

public class ValidacoesComunsService {

    public static GenericExceptionEnum validarSentidoOrderBy(String sentidoOrderBy){
        if (sentidoOrderBy == null || sentidoOrderBy.isBlank()){return SENTIDO_ORDER_BY_INVALIDO;}

        String ordenacaoTratada = sentidoOrderBy.strip().toLowerCase();
        if(ORDEM_CRESCENTE.getAcao().equalsIgnoreCase(ordenacaoTratada) ||
           ORDEM_DECRESCENTE.getAcao().equalsIgnoreCase(ordenacaoTratada)){
            return VALIDACAO_OK;
        }
        return SENTIDO_ORDER_BY_INVALIDO;
    }

    public static GenericExceptionEnum validarId(String id){
        try {
            String idTratado = id.strip();
            Long.parseLong(idTratado);
            return VALIDACAO_OK;
        } catch (NumberFormatException numberFormatException){
            return ID_INVALIDO;
        }
    }

    public static GenericExceptionEnum validarWhere(GenericEnumCampos where){
        return where.isValido() ? VALIDACAO_OK : WHERE_INVALIDO;
    }

    public static GenericExceptionEnum validarOrderBy(GenericEnumCampos ordenacao){
        return ordenacao.isValido() ? VALIDACAO_OK : ORDER_BY_INVALIDO;
    }

    public static GenericExceptionEnum validarEntradaStringUniversal(ValidadorEntradaStringUniversalDto validadorEntradaStringDto){
        String stringParaValidar = validadorEntradaStringDto.stringParaValidar();
        if (stringParaValidar == null || stringParaValidar.isBlank()) {
            return validadorEntradaStringDto.retornoParaVazio();
        }

        String stringTratada = stringParaValidar.strip();
        if(stringTratada.length() > validadorEntradaStringDto.tamanhoMaximoString()){
            return validadorEntradaStringDto.retornoParaTamanhoInvalido();
        }
        return VALIDACAO_OK;
    }





}
