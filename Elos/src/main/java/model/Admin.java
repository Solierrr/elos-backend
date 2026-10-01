package model;

public class Admin {

    //Atributos
    private final long id;
    private String email;
    private String nome;
    private String senha;
    private FuncoesAdmin funcao;
    private boolean em_atividade;

    //Construtor
    public Admin(long id, String email, String senha, String nome, FuncoesAdmin funcao, boolean em_atividade) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.funcao = funcao;
        this.em_atividade = em_atividade;
    }

    //Getters e Setters
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public FuncoesAdmin getFuncao() {
        return funcao;
    }

    public void setFuncao(FuncoesAdmin funcao) {
        this.funcao = funcao;
    }

    public boolean isEm_atividade() {
        return em_atividade;
    }

    public void setEm_atividade(boolean em_atividade) {
        this.em_atividade = em_atividade;
    }

    //Método toString
    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "Email: "+ this.email + "\n" +
                "Nome: "+ this.nome + "\n" +
                "Senha: "+ this.senha + "\n" +
                "Função: "+ this.funcao + "\n" +
                "Está em atividade: "+ this.em_atividade + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}

