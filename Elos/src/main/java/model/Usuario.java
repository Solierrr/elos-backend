package model;

public class Usuario {

    //Atributos
    private final Long id;
    private final TiposUsuario tipoUsuario;
    private String email;
    private String senha;
    private String nome;
    private Double raioProcuraKm;

    //Construtor
    public Usuario(Long id, String email, String senha, String nome, TiposUsuario tipoUsuario, Double raioProcuraKm) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.raioProcuraKm = raioProcuraKm;
    }

    //Getters e Setters
    public Long getId() {
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

    public Double getRaioProcuraKm() {
        return raioProcuraKm;
    }

    public void setRaioProcuraKm(Double raioProcura) {
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
