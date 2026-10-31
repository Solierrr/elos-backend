package service;

import java.util.Arrays;

public enum JoinsPossiveis {

  // Variações do enum

  // Tabelas relacionadas ao usuário
  USUARIO(
      "usuario",
      "id_usuario",
      new String[] {"telefone", "endereco", "empresa_demandante", "profissional", "fornecedor"}),
  TELEFONE("telefone", "id_telefone", new String[] {}),
  ENDERECO("endereco", "id_endereco", new String[] {}),
  EMPRESA_DEMANDANTE("empresa_demandante", "id_empresa_demandante", new String[] {}),
  PROFISSIONAL("profissional", "id_profissional", new String[] {}),
  FORNECEDOR("fornecedor", "id_fornecedor", new String[] {"profissional"}),

  // Tabelas relacionadas aos admins
  ADMIN(
      "admin",
      "id_admin",
      new String[] {"historico_alteracoes", "log_acessos_admin", "telefone_admin"}),
  HISTORICO_ALTERACOES("historico_alteracoes", "id_historico_alteracoes", new String[] {}),
  LOG_ACESSOS_ADMIN("log_acessos_admin", "id_log_acessos_admin", new String[] {}),
  TELEFONE_ADMIN("telefone_admin", "id_telefone_admin", new String[] {});

  // Atributos
  private final String nomeTabela;
  private final String nomePkComoFk;
  private final String[] tabelasConectaveisViaPk;

  // Construtor
  JoinsPossiveis(String nomeTabela, String nomePkComoFk, String[] tabelasConectaveisViaPk) {
    this.nomeTabela = nomeTabela;
    this.nomePkComoFk = nomePkComoFk;
    this.tabelasConectaveisViaPk = tabelasConectaveisViaPk;
  }

  // Getters
  public String getNomeTabela() {
    return nomeTabela;
  }

  public String getNomePkComoFk() {
    return nomePkComoFk;
  }

  public String[] getTabelasConectaveisViaPk() {
    return tabelasConectaveisViaPk;
  }

  // Métodos utilitarios
  public static JoinsPossiveis encontrarTabelaReferente(String tabela) {
    if (tabela == null || tabela.isBlank()) {
      return null;
    }

    String tabelaTratada = tabela.strip().toLowerCase();
    for (JoinsPossiveis joinsPossiveis : JoinsPossiveis.values()) {
      if (joinsPossiveis.getNomeTabela().equalsIgnoreCase(tabelaTratada)) {
        return joinsPossiveis;
      }
    }
    return null;
  }

  public static String joinValido(String tabelaOrigem, String tabelaDestino) {

    JoinsPossiveis tabelaOrigemConvertida = encontrarTabelaReferente(tabelaOrigem);
    JoinsPossiveis tabelaDestinoConvertida = encontrarTabelaReferente(tabelaDestino);

    if (tabelaOrigemConvertida == null || tabelaDestinoConvertida == null) return null;

    if (Arrays.asList(tabelaOrigemConvertida.getTabelasConectaveisViaPk())
        .contains(tabelaDestino.toLowerCase())) return tabelaOrigemConvertida.getNomePkComoFk();

    if (Arrays.asList(tabelaDestinoConvertida.getTabelasConectaveisViaPk())
        .contains(tabelaOrigem.toLowerCase())) return tabelaDestinoConvertida.getNomePkComoFk();

    return null;
  }
}
