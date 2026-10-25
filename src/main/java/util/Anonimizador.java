package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Admin;
import model.FuncoesAdmin;

public final class Anonimizador {

  //Métodos auxiliares para anonimizar os dados quando o usuário não tem autorização de vê-los na integra
  private static String anonimizarEmail(String email) {
    char[] emailEmChar = email.toCharArray();
    int posicaoArroba = email.lastIndexOf('@');

    for (int i = 1; i < posicaoArroba; i++) {
      emailEmChar[i] = emailEmChar[i] == '.' ? '.' : '*';
    }
    return String.copyValueOf(emailEmChar);
  }

  private static String anonimizarNomeGenerico(String string) {
    String[] partes = string.trim().split("\\s+");

    for (int i = 0; i < partes.length; i++) {
      String palavra = partes[i];

      if (palavra.length() > 1) {
        String asteriscos = "*".repeat(palavra.length() - 2);
        partes[i] = palavra.charAt(0) + asteriscos + palavra.charAt(palavra.length() - 1);
      }
    }
    return String.join(" ", partes);
  }

  public static String anonimizarCnpjECpf(String dado, boolean isCpnj) {
    char[] dadoEmChar = dado.toCharArray();

    int inicio = 3;
    if (isCpnj) {
      inicio--;
    }

    for (int i = inicio; i < inicio + 6; i++) {
      dadoEmChar[i] = '*';
    }

    return String.copyValueOf(dadoEmChar);
  }

  private static String anonimizarTelefone(String dado) {
    char[] dadoEmChar = dado.toCharArray();

    for (int i = 2; i < 6; i++) {
      dadoEmChar[i] = '*';
    }

    return String.copyValueOf(dadoEmChar);
  }

  private static String anonimizarElementoEndereco(String dado) {

    String[] dadosPartes = dado.split("\\s+");

    String asteriscos = (dadosPartes[dadosPartes.length - 1].length() - 2) >= 0 ? "*".repeat(
        dadosPartes[dadosPartes.length - 1].length() - 2) : "";
    dadosPartes[dadosPartes.length - 1] =
        dadosPartes[dadosPartes.length - 1].charAt(0) + dadosPartes[dadosPartes.length - 1].charAt(
            1) + asteriscos;

    return String.join(" ", dadosPartes);
  }

  private static String anonimizarCep(String dado) {
    return dado.substring(0, 5) + "*".repeat(3);
  }

  private static String anonimizarComplemento(String dado) {
    char[] dadoEmChar = dado.toCharArray();

    for (int i = 0; i < dadoEmChar.length; i++) {
      if (dadoEmChar[i] != ' ') {
        dadoEmChar[i] = '*';
      }
    }
    return String.copyValueOf(dadoEmChar);
  }

  private static String anonimizarIp(String dado) {
    if(dado.strip().equalsIgnoreCase("::1")){
      return dado.strip();
    }

    String[] dadosParte = dado.split("\\.");

    for (int i = dadosParte.length - 2; i < dadosParte.length; i++) {
      dadosParte[i] = "*".repeat(dadosParte[i].length());
    }

    return String.join(".", dadosParte);
  }

  //Método para anonimizar os dados utilizando os utilitarios e o enum
  public void anonimizarDadosLidos(List<Map<String, Object>> dados,
      Admin admin) {
    List<CampoParaAnonimizar> campos = new ArrayList<>();

    if (!dados.isEmpty()) {
      Map<String, Object> dadoInicial = dados.getFirst();
      for (String chave : dadoInicial.keySet()) {
        CampoParaAnonimizar campoAtual = CampoParaAnonimizar.descobrirNomeCampo(chave);
        if (campoAtual != null) {
          campos.add(campoAtual);
        }
      }
    }

    for (Map<String, Object> dado : dados) {
      for (CampoParaAnonimizar campoParaAnonimizar : campos) {
        campoParaAnonimizar.anonimizar(dado, admin);
      }
    }
  }

  /* Uso de um enum privado e estatico com o intuito de facilitar o
     fluxo de anonimizar os dados para ir para o front
   */
  private static enum CampoParaAnonimizar {

    EMAIL("email") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                anonimizarEmail((String) registro.get(this.getNomeCampo())));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o e-mail");
          }
        }
      }
    }, SENHA("senha") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        registro.remove(this.getNomeCampo());
      }
    }, NOME("nome") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                anonimizarNomeGenerico((String) registro.get(this.getNomeCampo())));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o nome");
          }
        }
      }
    }, CPF_AES("cpf_aes") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.put("cpf", PorMascara.porMascaraCpf(anonimizarCnpjECpf(
                new Criptografia().descriptografarAes((byte[]) registro.get(this.getNomeCampo())),
                false)));
            registro.remove("cpf_aes");
            registro.remove("cpf_hmac");
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o cpf");
          } catch (RuntimeException runtimeException) {
            System.out.println(
                runtimeException.getMessage() + " | ID do registro: " + registro.get("id"));
            registro.replace(this.getNomeCampo(), "Erro ao ler o cpf");
          }
          return;
        }
        try {
          registro.put("cpf", PorMascara.porMascaraCpf(
              new Criptografia().descriptografarAes((byte[]) registro.get(this.getNomeCampo()))));
          registro.remove("cpf_aes");
          registro.remove("cpf_hmac");
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler o cpf");
        } catch (RuntimeException runtimeException) {
          System.out.println(
              runtimeException.getMessage() + " | ID do registro: " + registro.get("id"));
          registro.replace(this.getNomeCampo(), "Erro ao ler o cpf");
        }
      }
    }, ID_FORNECEDOR("id_fornecedor") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (registro.get(this.getNomeCampo()) == null) {
          registro.replace(this.getNomeCampo(), "Autônomo");
        }
      }
    }, DOCUMENTO_CONTRATO("documento_contrato") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          registro.remove(this.getNomeCampo());
        }else{
          if(registro.get(this.getNomeCampo()) == null)
            registro.replace(this.getNomeCampo(), "Autônomo");
        }
      }
    }, CNPJ("cnpj") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(), PorMascara.porMascaraCnpj(
                anonimizarCnpjECpf((String) registro.get(this.getNomeCampo()), true)));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o cnpj");
          }
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),
              PorMascara.porMascaraCnpj((String) registro.get(this.getNomeCampo())));
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler o cnpj");
        }
      }
    }, RAZAO_SOCIAL("razao_social") {
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                anonimizarNomeGenerico((String) registro.get(this.getNomeCampo())));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler a razão social");
          }
        }
      }
    }, EH_MANDANTE("eh_mandante") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (registro.get(this.getNomeCampo()).equals(true)) {
          registro.replace(this.getNomeCampo(), "Sim");
        } else {
          registro.replace(this.getNomeCampo(), "Não");
        }
      }
    }, PRINCIPAL("principal") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (String.valueOf(registro.get(this.getNomeCampo())).equalsIgnoreCase("true")) {
          registro.replace(this.getNomeCampo(), "Sim");
        } else {
          registro.replace(this.getNomeCampo(), "Não");
        }
      }
    }, TELEFONE("telefone") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(), PorMascara.porMascaraTelefone(
                anonimizarTelefone((String) registro.get(this.getNomeCampo()))));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o número do telefone");
          }
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),
              PorMascara.porMascaraTelefone((String) registro.get(this.getNomeCampo())));
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler o número do telefone");
        }
      }
    }, BAIRRO("bairro") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                anonimizarElementoEndereco((String) registro.get(this.getNomeCampo())));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o bairro");
          }
        }
      }
    }, LOGRADOURO("logradouro") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                anonimizarElementoEndereco((String) registro.get(this.getNomeCampo())));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o logradouro");
          }
        }
      }
    }, CEP("cep") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(), PorMascara.porMascaraCep(
                anonimizarCep((String) registro.get(this.getNomeCampo()))));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o CEP");
          }
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),
              PorMascara.porMascaraCep((String) registro.get(this.getNomeCampo())));
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler o CEP");
        }
      }
    }, COMPLEMENTO("complemento") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                registro.get(this.getNomeCampo())==null? "Sem complemento" : anonimizarComplemento((String) registro.get(this.getNomeCampo())));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o complemento");
          }
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),
                  registro.get(this.getNomeCampo())==null? "Sem complemento" : registro.get(this.getNomeCampo()));
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler o complemento");
        }
      }
    }, DADOS_ANTIGOS("dados_antigos") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          registro.remove(this.getNomeCampo());
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),((org.postgresql.util.PGobject) registro.get(this.getNomeCampo())).getValue());
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler os dados antigos");
        }
      }
    }, DADOS_ATUALIZADOS("dados_atualizados") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          registro.remove(this.getNomeCampo());
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),((org.postgresql.util.PGobject) registro.get(this.getNomeCampo())).getValue());
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler os dados atualizados");
        }
      }
    }, ENDERECO_IP("endereco_ip") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (admin.getFuncao() != FuncoesAdmin.SUPERADMIN) {
          try {
            registro.replace(this.getNomeCampo(),
                anonimizarIp(((org.postgresql.util.PGobject) registro.get(this.getNomeCampo())).getValue()));
          } catch (ClassCastException classCastException) {
            registro.replace(this.getNomeCampo(), "Erro ao ler o endereço de ip");
          }
          return;
        }
        try {
          registro.replace(this.getNomeCampo(),((org.postgresql.util.PGobject) registro.get(this.getNomeCampo())).getValue());
        } catch (ClassCastException classCastException) {
          registro.replace(this.getNomeCampo(), "Erro ao ler o endereço de ip");
        }
      }
    }, EM_ATIVIDADE("em_atividade") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (registro.get(this.getNomeCampo()).equals(true)) {
          registro.replace(this.getNomeCampo(), "Sim");
        } else {
          registro.replace(this.getNomeCampo(), "Não");
        }
      }
    }, SUCESSO("sucesso") {
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if (registro.get(this.getNomeCampo()).equals(true)) {
          registro.replace(this.getNomeCampo(), "Sim");
        } else {
          registro.replace(this.getNomeCampo(), "Não");
        }
      }
    }, MOTIVO_FALHA("motivo_falha"){
      @Override
      public void anonimizar(Map<String, Object> registro, Admin admin) {
        if(registro.get(this.getNomeCampo()) == null){
          registro.replace(this.getNomeCampo(), "Login efetuado");
        }

      }
    };


    private final String nomeCampo;

    CampoParaAnonimizar(String nomeCampo) {
      this.nomeCampo = nomeCampo;
    }

    public String getNomeCampo() {
      return nomeCampo;
    }

    public static CampoParaAnonimizar descobrirNomeCampo(String nomeCampoEntrada) {
      if (nomeCampoEntrada == null || nomeCampoEntrada.isBlank()) {
        return null;
      }

      String nomeCampoEntradaTratado = nomeCampoEntrada.strip().toLowerCase();
      for (CampoParaAnonimizar campoLido : CampoParaAnonimizar.values()) {
        if (campoLido.getNomeCampo().equalsIgnoreCase(nomeCampoEntradaTratado)) {
          return campoLido;
        }
      }
      return null;
    }

    public abstract void anonimizar(Map<String, Object> registro, Admin admin);

  }


}
