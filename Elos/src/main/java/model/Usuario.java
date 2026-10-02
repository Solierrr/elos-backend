package model;

public class Usuario {

    //Atributos
    private final long id;
    private final TiposUsuario tipoUsuario;
    private String email;
    private String senha;
    private String nome;
    private double raioProcuraKm;

    //Construtor
    public Usuario(long id, String email, String senha, String nome, TiposUsuario tipoUsuario, double raioProcuraKm) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.raioProcuraKm = raioProcuraKm;
    }

    //Getters e Setters
    public long getId() {
        return id;
    }

    public TiposUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getRaioProcuraKm() {
        return raioProcuraKm;
    }

    public void setRaioProcuraKm(double raioProcura) {
        this.raioProcuraKm = raioProcura;
    }

    //Método toString
    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "Tipo do usuário: "+ this.tipoUsuario + "\n" +
                "Email: "+ this.email + "\n" +
                "Senha: "+ this.senha + "\n" +
                "Nome: "+ this.nome + "\n" +
                "Raio de procura em km: "+ this.raioProcuraKm + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
