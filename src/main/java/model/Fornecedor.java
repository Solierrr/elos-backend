package model;

public class Fornecedor {

    private final long id;
    private final long idUsuario;
    private final TiposUsuario tipoUsuario = TiposUsuario.FORNECEDOR;
    private TiposFornecedor tipoFornecedor;
    private final String cnpj;
    private String razaoSocial;

    public Fornecedor(long id, long idUsuario, TiposFornecedor tipoFornecedor, String cnpj, String razaoSocial) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipoFornecedor = tipoFornecedor;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
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

    public TiposFornecedor getTipoFornecedor() {
        return tipoFornecedor;
    }

    public void setTipoFornecedor(TiposFornecedor tipoFornecedor) {
        this.tipoFornecedor = tipoFornecedor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "ID do usuário: "+ this.idUsuario + "\n" +
                "Tipo do usuário: "+ this.tipoUsuario.getTipoDoUsuario() + "\n" +
                "Tipo do fornecedor: "+ this.tipoFornecedor.getTipoFornecedor() + "\n" +
                "CNPJ: "+ this.cnpj + "\n" +
                "Razão social: " + this.razaoSocial + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }

}