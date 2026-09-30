package dao;

import exception.GenericExceptionEnum;

import java.util.List;

public interface GenericDAO<T> {

    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica);

    public List<T> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica);

    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica);

    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica);

}
