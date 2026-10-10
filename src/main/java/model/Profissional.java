package model;

public class Profissional {

    private final Long id;
    private final Long idUsuario;
    private final TiposUsuario tipoUsuario;
    private String profissao;
    private final String cpfHmac;
    private final byte[] cpfAes;
    private Long idFornecedor;
    private String documentoContrato;

    public Profissional(Long id, Long idUsuario, TiposUsuario tipoUsuario, String profissao,
                        String cpfHmac, byte[] cpfAes, Long idFornecedor, String documentoContrato) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
        this.profissao = profissao;
        this.cpfHmac = cpfHmac;
        this.cpfAes = cpfAes;
        this.idFornecedor = idFornecedor;
        this.documentoContrato = documentoContrato;
    }

    public Long getId() {
        return id;
    }

    public Long getIdUsuario() {
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

    public String getCpfHmac() {
        return cpfHmac;
    }

    public byte[] getCpfAes() {
        return cpfAes;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getDocumentoContrato() {
        return documentoContrato;
    }

    public void setDocumentoContrato(String documentoContrato) {
        this.documentoContrato = documentoContrato;
    }

    @Override
    public String toString() {
        return "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: " + this.id + "\n" +
                "ID do usuário: " + this.idUsuario + "\n" +
                "Tipo do usuário: " + this.tipoUsuario.getTipoDoUsuario() + "\n" +
                "Profissão: " + this.profissao + "\n" +
                "CPF hmac: " + this.cpfHmac + "\n" +
                "CPF aes: " + this.cpfAes + "\n" +
                "ID do fornecedor: " + this.idFornecedor + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
