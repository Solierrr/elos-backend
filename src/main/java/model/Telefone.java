package model;

public class Telefone {

    //Atributos
    private final long id;
    private final long idUsuario;
    private String telefone;
    private TiposTelefone tipo;
    private boolean principal;

    //Construtor
    public Telefone(long id,  long idUsuario, String telefone, TiposTelefone tipo,boolean principal) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.telefone = telefone;
        this.tipo = tipo;
        this.principal = principal;
    }

    //Getters e Setters
    public long getId() {
        return id;
    }

    public long getIdUsuario() {
        return idUsuario;
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

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    //Método toString
    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "Telefone: "+ this.telefone + "\n" +
                "Tipo do telefone: "+ this.tipo.getTipoTelefone() + "\n" +
                "ID do usuário: "+ this.idUsuario + "\n" +
                "Principal: "+ this.principal + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
