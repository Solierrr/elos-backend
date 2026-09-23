package model;

import java.time.LocalDate;

public class HistoricoAlteracoesAdmin {

    //Atributos
    private final long id;
    private final long idAdmin;
    private long idRegistro;
    private String tabelaModificada;
    private String acao;
    private String dadosAntigos; //Vira JSON
    private String dadosAtualizados; //Vira JSON
    private LocalDate dataAlteracao;

    //Construtor

    public HistoricoAlteracoesAdmin(long id, long idAdmin, long idRegistro, String tabelaModificada, String acao, String dadosAntigos, String dadosAtualizados, LocalDate dataAlteracao) {
        this.id = id;
        this.idAdmin = idAdmin;
        this.idRegistro = idRegistro;
        this.tabelaModificada = tabelaModificada;
        this.acao = acao;
        this.dadosAntigos = dadosAntigos;
        this.dadosAtualizados = dadosAtualizados;
        this.dataAlteracao = dataAlteracao;
    }

    //Getters e Setters
    public long getId() {
        return id;
    }

    public long getIdAdmin() {
        return idAdmin;
    }

    public long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getTabelaModificada() {
        return tabelaModificada;
    }

    public void setTabelaModificada(String tabelaModificada) {
        this.tabelaModificada = tabelaModificada;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getDadosAntigos() {
        return dadosAntigos;
    }

    public void setDadosAntigos(String dadosAntigos) {
        this.dadosAntigos = dadosAntigos;
    }

    public String getDadosAtualizados() {
        return dadosAtualizados;
    }

    public void setDadosAtualizados(String dadosAtualizados) {
        this.dadosAtualizados = dadosAtualizados;
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDate dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    //Método toString
    @Override
    public String toString(){
        return  "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n" +
                "ID: "+ this.id + "\n" +
                "ID do admin: "+ this.idAdmin + "\n" +
                "ID do registro: "+ this.idRegistro + "\n" +
                "Tabela modificada: "+ this.tabelaModificada + "\n" +
                "Ação: "+ this.acao + "\n" +
                "Dados antigos: "+ this.dadosAntigos + "\n" +
                "Dados atualizados: "+ this.dadosAtualizados + "\n" +
                "Data de alteração: "+ this.dataAlteracao + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }
}
