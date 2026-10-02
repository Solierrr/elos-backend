package model;

public class Endereco {

    private final long id;
    private final long idUsuario;
    private EstadosBrasileiros estado;
    private String cidade;
    private String bairro;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;

    public Endereco(long id, long idUsuario, EstadosBrasileiros estado, String cidade, String bairro, String cep, String logradouro, String numero, String complemento) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
    }

    public long getId() {
        return id;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public EstadosBrasileiros getEstado() {
        return estado;
    }

    public void setEstado(EstadosBrasileiros estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "ID do usuário: "+ this.idUsuario + "\n" +
                "Estado: "+ this.estado.getSiglaEstado() + "\n" +
                "Cidade: "+ this.cidade + "\n" +
                "Bairro: "+ this.bairro + "\n" +
                "CEP: "+ this.cep + "\n" +
                "Logradouro: "+ this.logradouro + "\n" +
                "Número: "+ this.numero + "\n" +
                "Complemento: "+ this.complemento + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
