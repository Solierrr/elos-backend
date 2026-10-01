package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public enum DataTypesUsados {

    INTEGER(Types.INTEGER){
        @Override
        public void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException{
            if (object == null) {
                preparedStatement.setNull(index, Types.INTEGER);
            } else if (object instanceof Integer) {
                preparedStatement.setInt(index, (Integer) object);
            } else {
                throw new IllegalArgumentException(
                        "DataType fornecido incompatível com o valor inserido");
            }
        }
    },
    DOUBLE(Types.DOUBLE){
        @Override
        public void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException{
            if (object == null) {
                preparedStatement.setNull(index, Types.DOUBLE);
            } else if (object instanceof Double) {
                preparedStatement.setDouble(index, (Double) object);
            } else {
                throw new IllegalArgumentException(
                        "DataType fornecido incompatível com o valor inserido");
            }
        }
    },
    LONG(Types.BIGINT){
        @Override
        public void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException{
            if (object == null) {
                preparedStatement.setNull(index, Types.BIGINT);
            } else if (object instanceof Long) {
                preparedStatement.setLong(index, (Long) object);
            } else {
                throw new IllegalArgumentException(
                        "DataType fornecido incompatível com o valor inserido");
            }
        }
    },
    STRING(Types.VARCHAR){
        @Override
        public void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException{
            if (object == null) {
                preparedStatement.setNull(index, Types.VARCHAR);
            } else if (object instanceof String) {
                preparedStatement.setString(index, (String) object);
            } else {
                throw new IllegalArgumentException(
                        "DataType fornecido incompatível com o valor inserido");
            }
        }
    },
    BOOLEAN(Types.BOOLEAN){
        @Override
        public void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException{
            if (object == null) {
                preparedStatement.setNull(index, Types.BOOLEAN);
            } else if (object instanceof Boolean) {
                preparedStatement.setBoolean(index, (Boolean) object);
            } else {
                throw new IllegalArgumentException(
                        "DataType fornecido incompatível com o valor inserido");
            }
        }
    },
    BYTEA(Types.BINARY){
        @Override
        public void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException{
            if (object == null) {
                preparedStatement.setNull(index, Types.BINARY);
            } else if (object instanceof byte[]) {
                preparedStatement.setBytes(index, (byte[]) object);
            } else {
                throw new IllegalArgumentException(
                        "DataType fornecido incompatível com o valor inserido");
            }
        }
    };

    private final int dataType;

    DataTypesUsados(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public abstract void realizarSet(PreparedStatement preparedStatement, int index, Object object) throws SQLException;

    public static DataTypesUsados descobrirDataType(int dataType){
        for(DataTypesUsados dataTypesUsados : DataTypesUsados.values()){
            if(dataTypesUsados.getDataType() == dataType){
                return dataTypesUsados;
            }
        }
        throw new IllegalArgumentException("DataType não cadastrado inserido");
    }

}
