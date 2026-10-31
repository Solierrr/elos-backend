package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

public enum DataTypesUsados {

  //Variações do enum
  INTEGER(Types.INTEGER) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.INTEGER);
      } else if (object instanceof Integer) {
        preparedStatement.setInt(index, (Integer) object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  DOUBLE(Types.DOUBLE) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.DOUBLE);
      } else if (object instanceof Double) {
        preparedStatement.setDouble(index, (Double) object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  LONG(Types.BIGINT) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.BIGINT);
      } else if (object instanceof Long) {
        preparedStatement.setLong(index, (Long) object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  STRING(Types.VARCHAR) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.VARCHAR);
      } else if (object instanceof String) {
        preparedStatement.setString(index, (String) object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  BOOLEAN(Types.BOOLEAN) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.BOOLEAN);
      } else if (object instanceof Boolean) {
        preparedStatement.setBoolean(index, (Boolean) object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  BYTEA(Types.BINARY) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.BINARY);
      } else if (object instanceof byte[]) {
        preparedStatement.setBytes(index, (byte[]) object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  JSON(Types.OTHER) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.OTHER);
      } else if (object instanceof com.google.gson.JsonElement) {
        preparedStatement.setObject(index, object.toString(), Types.OTHER);
      } else if (object instanceof String) {
        preparedStatement.setObject(index, (String) object, Types.OTHER);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  INET(-1000) {
    @Override
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.OTHER);
      } else if (object instanceof java.net.InetAddress) {
        preparedStatement.setObject(
            index, ((java.net.InetAddress) object).getHostAddress(), Types.OTHER);
      } else if (object instanceof String) {
        preparedStatement.setObject(index, (String) object, Types.OTHER);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  },
  DATE(Types.DATE){
    public void realizarSet(PreparedStatement preparedStatement, int index, Object object)
        throws SQLException {
      if (object == null) {
        preparedStatement.setNull(index, Types.DATE);
      } else if (object instanceof LocalDate) {
        preparedStatement.setObject(index, object);
      } else {
        throw new IllegalArgumentException("DataType fornecido incompatível com o valor inserido");
      }
    }
  };

  //Atributo
  private final int dataType;

  //Construtor
  DataTypesUsados(int dataType) {
    this.dataType = dataType;
  }

  //Getter
  public int getDataType() {
    return dataType;
  }

  //Método utilizado para realizar o setValor no PreparedStatement de maneira própria em cada versão do enum
  public abstract void realizarSet(PreparedStatement preparedStatement, int index, Object object)
      throws SQLException;

  //Método auxiliar
  public static DataTypesUsados descobrirDataType(int dataType) {
    for (DataTypesUsados dataTypesUsados : DataTypesUsados.values()) {
      if (dataTypesUsados.getDataType() == dataType) {
        return dataTypesUsados;
      }
    }
    throw new IllegalArgumentException("DataType não cadastrado inserido");
  }
}
