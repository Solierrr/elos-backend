package service;

import exception.GenericExceptionEnum;

public record ValidadorEntradaStringUniversalDto(GenericExceptionEnum retornoParaVazio, GenericExceptionEnum retornoParaTamanhoInvalido, int tamanhoMaximoString, String stringParaValidar) {}
