package exception;

public enum ErrosDoSQL {

    //Variações do enum
    STRING_DATA_RIGHT_TRUNCATION("22001"),
    INTEGRITY_CONSTRAINT_VIOLATION("23000"),
    RESTRICT_VIOLATION("23001"),
    NOT_NULL_VIOLATION("23502"),
    FOREIGN_KEY_VIOLATION("23503"),
    UNIQUE_VIOLATION("23505"),
    CHECK_VIOLATION("23514"),
    EXCLUSION_VIOLATION("23P01");

    //Constantes das variações
    private final String sqlState;

    //Construtor
    ErrosDoSQL(String sqlState) {
        this.sqlState = sqlState;
    }

    //Getter
    public String getSqlState() {
        return sqlState;
    }

    //Método auxiliar
    public static boolean foiCausadoPorConstraint(String sqlState){
        if(sqlState == null){
            return false;
        }
        for (ErrosDoSQL errosDoSQL : ErrosDoSQL.values()){
            if(errosDoSQL.getSqlState().equalsIgnoreCase(sqlState)){
                return true;
            }
        }
        return false;
    }
}
