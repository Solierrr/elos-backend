package model;

public class EmpresaDemandante {

  //Atributos
  private final Long id;
  private final Long idUsuario;
  private final TiposUsuario tipoUsuario;
  private final String cnpj;
  private String razaoSocial;
  private Boolean ehMandante;

  //Construtor
  public EmpresaDemandante(
      Long id,
      Long idUsuario,
      TiposUsuario tipoUsuario,
      String cnpj,
      String razaoSocial,
      Boolean ehMandante) {
    this.id = id;
    this.idUsuario = idUsuario;
    this.tipoUsuario = tipoUsuario;
    this.cnpj = cnpj;
    this.razaoSocial = razaoSocial;
    this.ehMandante = ehMandante;
  }

  //Getters e Setters
  public Long getId() {
    return id;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public TiposUsuario getTipoUsuario() {
    return tipoUsuario;
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

  public Boolean isEhMandante() {
    return ehMandante;
  }

  public void setEhMandante(Boolean ehMandante) {
    this.ehMandante = ehMandante;
  }

  // Método toString
  @Override
  public String toString() {
    return  "ID: "
        + this.id
        + "\n"
        + "ID do usuário: "
        + this.idUsuario
        + "\n"
        + "Tipo do usuário: "
        + this.tipoUsuario.getTipoDoUsuario()
        + "\n"
        + "CNPJ: "
        + this.cnpj
        + "\n"
        + "Razão social: "
        + this.razaoSocial
        + "\n"
        + "É mandante: "
        + this.ehMandante;
  }
}
