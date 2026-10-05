package model;

public class Admin {

    //Atributos
    private final Long id;
    private String email;
    private String nome;
    private String senha;
    private FuncoesAdmin funcao;
    private Boolean emAtividade;

    //Construtor
    public Admin(Long id, String email, String nome, String senha, FuncoesAdmin funcao, Boolean emAtividade) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.funcao = funcao;
        this.emAtividade = emAtividade;
    }

    //Getters e Setters
    public Long getId() {
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

    public Boolean isEmAtividade() {
        return emAtividade;
    }

    public void setEmAtividade(Boolean emAtividade) {
        this.emAtividade = emAtividade;
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
                "Está em atividade: "+ this.emAtividade + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}

