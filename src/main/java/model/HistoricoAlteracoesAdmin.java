package model;

import com.google.gson.Gson;
import java.time.LocalDate;

public class HistoricoAlteracoesAdmin {

    //Atributos
    private final Long id;
    private final Long idAdmin;
    private Long idRegistro;
    private String tabelaModificada;
    private String acao;
    private final Gson dadosAntigos; //Vira JSON
    private final Gson dadosAtualizados; //Vira JSON
    private LocalDate dataAlteracao;

    //Construtor

    public HistoricoAlteracoesAdmin(Long id, Long idAdmin, Long idRegistro, String tabelaModificada, String acao, Gson dadosAntigos, Gson dadosAtualizados, LocalDate dataAlteracao) {
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
    public Long getId() {
        return id;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
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

    public Gson getDadosAntigos() {
        return dadosAntigos;
    }

    public Gson getDadosAtualizados() {
        return dadosAtualizados;
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
