package service;

import java.util.Arrays;
import service.fornecedor.CamposFornecedor;

public enum JoinsPossiveis {

    //Tabelas relacionadas ao usuário
    USUARIO("usuario", "id_usuario", new String[]{}, new String[]{"telefone", "endereco", "empresa_demandante", "profissional", "fornecedor"}),
    TELEFONE("telefone", "id_telefone", new String[]{"usuario"}, new String[]{}),
    ENDERECO("endereco", "id_encereco", new String[]{"usuario"}, new String[]{}),
    EMPRESA_DEMANDANTE("empresa_demandante", "id_empresa_demandante", new String[]{"usuario"}, new String[]{}),
    PROFISSIONAL("profissional", "id_profissional", new String[]{"usuario", "fornecedor"}, new String[]{}),
    FORNECEDOR("fornecedor", "id_fornecedor", new String[]{"usuario"}, new String[]{"profissional"}),

    //Tabelas relacionadas aos admins
    ADMIN("admin", "id_admin", new String[]{}, new String[]{"historico_alteracoes", "log_acessos_admin", "telefone_admin"}),
    HISTORICO_ALTERACOES("historico_alteracoes", "id_historico_alteracoes", new String[]{"id_admin"}, new String[]{}),
    LOG_ACESSOS_ADMIN("log_acessos_admin", "id_log_acessos_admin", new String[]{"id_admin"}, new String[]{}),
    TELEFONE_ADMIN("telefone_admin", "id_telefone_admin", new String[]{"id_admin"}, new String[]{});

    private final String nomeTabela;
    private final String nomePkComoFk;
    private final String[] tabelasConectaveisViaFk;
    private final String[] tabelasConectaveisViaPk;

    JoinsPossiveis(String nomeTabela, String nomePkComoFk, String[] tabelasConectaveisViaFk, String[] tabelasConectaveisViaPk) {
        this.nomeTabela = nomeTabela;
        this.nomePkComoFk = nomePkComoFk;
        this.tabelasConectaveisViaFk = tabelasConectaveisViaFk;
        this.tabelasConectaveisViaPk = tabelasConectaveisViaPk;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public String getNomePkComoFk() {
        return nomePkComoFk;
    }

    public String[] getTabelasConectaveisViaFk() {
        return tabelasConectaveisViaFk;
    }

    public String[] getTabelasConectaveisViaPk() {
        return tabelasConectaveisViaPk;
    }

    public static JoinsPossiveis encontrarTabelaReferente(String tabela){
        if(tabela == null || tabela.isBlank()){
            return null;
        }

        String tabelaTratada = tabela.strip().toLowerCase();
        for(JoinsPossiveis joinsPossiveis : JoinsPossiveis.values()){
            if(joinsPossiveis.getNomeTabela().equalsIgnoreCase(tabelaTratada)){
                return joinsPossiveis;
            }
        }
        return null;
    }

    public static String joinValido(String tabelaOrigem, String tabelaDestino){

        JoinsPossiveis tabelaOrigemConvertida = encontrarTabelaReferente(tabelaOrigem);
        JoinsPossiveis tabelaDestinoConvertida = encontrarTabelaReferente(tabelaDestino);

        if(tabelaOrigemConvertida == null || tabelaDestinoConvertida == null) return null;

        if(Arrays.asList(tabelaOrigemConvertida.getTabelasConectaveisViaPk()).contains(tabelaDestino.toLowerCase()))
            return tabelaOrigemConvertida.getNomePkComoFk();

        if(Arrays.asList(tabelaDestinoConvertida.getTabelasConectaveisViaPk()).contains(tabelaDestino.toLowerCase()))
            return tabelaDestinoConvertida.getNomePkComoFk();

        return null;
    }

}
