package model;

public class Profissional {

    private final long id;
    private final long idUsuario;
    private final TiposUsuario tipoUsuario = TiposUsuario.PROFISSIONAL;
    private String profissao;
    private final String cpf;
    private long idFornecedor;

    public Profissional(long id, long idUsuario, String profissao, String cpf, long idFornecedor) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.profissao = profissao;
        this.cpf = cpf;
        this.idFornecedor = idFornecedor;
    }

    public long getId() {
        return id;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public TiposUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCpf() {
        return cpf;
    }

    public long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "ID do usuário: "+ this.idUsuario + "\n" +
                "Tipo do usuário: "+ this.tipoUsuario.getTipoDoUsuario() + "\n" +
                "Profissão: "+ this.profissao + "\n" +
                "CPF: "+ this.cpf + "\n" +
                "ID do fornecedor: "+ this.idFornecedor + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
