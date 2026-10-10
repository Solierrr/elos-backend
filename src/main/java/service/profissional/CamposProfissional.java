package service.profissional;


import service.GenericEnumCampos;

public enum CamposProfissional implements GenericEnumCampos {

    ID("id", false),
    ID_USUARIO("id_usuario", false),
    PROFISSAO("profissao", true),
    CPF("cpf", false),
    ID_FORNECEDOR("id_fornecedor", true),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true),
    INVALIDO("campo_do_profissional_invalido", false);

    private final String campoProfissional;
    private final boolean multiplosRetornos;

    CamposProfissional(String campoProfissional, boolean multiplosRetornos) {
        this.campoProfissional = campoProfissional;
        this.multiplosRetornos = multiplosRetornos;
    }

    public String getCampoProfissional() {
        return campoProfissional;
    }

    public boolean isMultiplosRetornos() {
        return multiplosRetornos;
    }

    public static CamposProfissional descobrirCampoProfissional(String campoProfissionalEntrada){
        if(campoProfissionalEntrada == null || campoProfissionalEntrada.isBlank()){
            return INVALIDO;
        }

        String campoProfissionalEntradaTratada = campoProfissionalEntrada.strip().toLowerCase();
        for(CamposProfissional campoProfissional : CamposProfissional.values()){
            if(campoProfissional.getCampoProfissional().equalsIgnoreCase(campoProfissionalEntradaTratada)){
                return campoProfissional;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
