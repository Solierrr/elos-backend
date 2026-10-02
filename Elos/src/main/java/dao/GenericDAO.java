package dao;

import java.util.ArrayList;

public interface GenericDAO<T> {

    public int insert(T objeto);

    public T readById(long id);

    public ArrayList<T> readAll();

    public int updateById(T objeto);

    public int deleteById(long id);

}
