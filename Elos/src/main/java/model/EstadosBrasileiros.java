package model;

public enum EstadosBrasileiros {

    AC("AC", "Acre"),
    AL("AL", "Alagoas"),
    AP("AP","Amapá"),
    AM("AM", "Amazonas"),
    BA("BA", "Bahia"),
    CE("CE", "Ceará"),
    DF("DF", "Distrito Federal"),
    ES("ES", "Espirito Santo"),
    GO("GO", "Goiás"),
    MA("MA", "Maranhão"),
    MT("MT", "Mato Grosso"),
    MS("MS", "Mato Grosso do Sul"),
    MG("MG", "Minas Gerais"),
    PA("PA", "Pará"),
    PB("PB", "Paraíba"),
    PR("PR", "Paraná"),
    PE("PE", "Pernambuco"),
    PI("PI", "Piauí"),
    RJ("RJ", "Rio de Janeiro"),
    RN("RN", "Rio Grande do Norte"),
    RS("RS", "Rio Grande do Sul"),
    RO("RO", "Rondônia"),
    RR("RR", "Roraima"),
    SC("SC", "Santa Catarina"),
    SP("SP", "São Paulo"),
    SE("SE", "Sergipe"),
    TO("TO", "Tocantins");

    private final String siglaEstado;
    private final String nomeEstado;

    EstadosBrasileiros(String siglaEstado, String nomeEstado) {
        this.siglaEstado = siglaEstado;
        this.nomeEstado = nomeEstado;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public String getNomeEstado() {
        return nomeEstado;
    }

    public static EstadosBrasileiros descobrirEstadoBrasileiroPorSigla(String estadoBrasileiroSiglaEntrada){
        if(estadoBrasileiroSiglaEntrada == null){
            return null;
        }

        String estadoBrasileiroSiglaEntradaTratado = estadoBrasileiroSiglaEntrada.strip().toUpperCase();
        for(EstadosBrasileiros estadosBrasileiros : EstadosBrasileiros.values()){
            if(estadosBrasileiros.getSiglaEstado().equalsIgnoreCase(estadoBrasileiroSiglaEntradaTratado)){
                return estadosBrasileiros;
            }
        }
        return null;
    }
}
