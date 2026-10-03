package model;

public class TelefoneAdmin {
    //Atributos
    private final Long id;
    private final Long idAdmin;
    private String telefone;
    private TiposTelefone tipo;
    private Boolean principal;

    //Construtor
    public TelefoneAdmin(Long id, Long idAdmin, String telefone, TiposTelefone tipo, Boolean principal) {
        this.id = id;
        this.idAdmin = idAdmin;
        this.telefone = telefone;
        this.tipo = tipo;
        this.principal = principal;
    }

    //Getters e Setters
    public Long getId() {
        return id;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TiposTelefone getTipo() {
        return tipo;
    }

    public void setTipo(TiposTelefone tipo) {
        this.tipo = tipo;
    }

    public Boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    //Método toString
    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "Telefone: "+ this.telefone + "\n" +
                "Tipo do telefone: "+ this.tipo + "\n" +
                "ID do admin: "+ this.idAdmin + "\n" +
                "Principal: "+ this.principal + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
