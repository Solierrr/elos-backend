package model;

import java.time.LocalDate;

public class LogAcessosAdmin {

    //Atributos
    private final long id;
    private final long idAdmin;
    private LocalDate dataAcesso;
    private boolean sucesso;
    private String enderecoIp;
    private String motivoFalha;
    private NavegadoresAdmin navegador;
    private SistemasOperacionaisAdmin sistemaOperacional;

    //Construtor
    public LogAcessosAdmin(long id, long idAdmin, LocalDate dataAcesso, boolean sucesso, String enderecoIp, String motivoFalha, NavegadoresAdmin navegador, SistemasOperacionaisAdmin sistemaOperacional) {
        this.id = id;
        this.idAdmin = idAdmin;
        this.dataAcesso = dataAcesso;
        this.sucesso = sucesso;
        this.enderecoIp = enderecoIp;
        this.motivoFalha = motivoFalha;
        this.navegador = navegador;
        this.sistemaOperacional = sistemaOperacional;
    }

    //Getters e Setters
    public long getId() {
        return id;
    }

    public long getIdAdmin() {
        return idAdmin;
    }

    public LocalDate getDataAcesso() {
        return dataAcesso;
    }

    public void setDataAcesso(LocalDate dataAcesso) {
        this.dataAcesso = dataAcesso;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getEnderecoIp() {
        return enderecoIp;
    }

    public void setEnderecoIp(String enderecoIp) {
        this.enderecoIp = enderecoIp;
    }

    public String getMotivoFalha() {
        return motivoFalha;
    }

    public void setMotivoFalha(String motivoFalha) {
        this.motivoFalha = motivoFalha;
    }

    public NavegadoresAdmin getNavegador() {
        return navegador;
    }

    public void setNavegador(NavegadoresAdmin navegador) {
        this.navegador = navegador;
    }

    public SistemasOperacionaisAdmin getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(SistemasOperacionaisAdmin sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    //Metodo toString
    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "ID do admin: "+ this.idAdmin + "\n" +
                "Data de acesso: "+ this.dataAcesso + "\n" +
                "Sucesso: "+ this.sucesso + "\n" +
                "Endereço IP: "+ this.enderecoIp + "\n" +
                "Navegador: "+ this.navegador + "\n" +
                "Sistema operacional: "+ this.sistemaOperacional + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }

}
