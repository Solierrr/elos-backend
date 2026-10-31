package util;

public final class PorMascara {

  // Métodos utilitarios para mascarar dados facilitando a compreensão do usuário
  public static String porMascaraCpf(String cpf) {
    String[] partesCpf = {
      cpf.substring(0, 3), cpf.substring(3, 6), cpf.substring(6, 9), cpf.substring(9, 11)
    };

    StringBuilder sb = new StringBuilder();
    sb.append(partesCpf[0])
        .append(".")
        .append(partesCpf[1])
        .append(".")
        .append(partesCpf[2])
        .append("-")
        .append(partesCpf[3]);

    return sb.toString();
  }

  public static String porMascaraCnpj(String cnpj) {
    String[] partesCnpj = {
      cnpj.substring(0, 2),
      cnpj.substring(2, 5),
      cnpj.substring(5, 8),
      cnpj.substring(8, 12),
      cnpj.substring(12, 14)
    };

    StringBuilder sb = new StringBuilder();
    sb.append(partesCnpj[0])
        .append(".")
        .append(partesCnpj[1])
        .append(".")
        .append(partesCnpj[2])
        .append("/")
        .append(partesCnpj[3])
        .append("-")
        .append(partesCnpj[4]);

    return sb.toString();
  }

  public static String porMascaraTelefone(String telefone) {
    String[] partesTelefone = new String[3];

    if (telefone.length() == 10) {
      partesTelefone[0] = telefone.substring(0, 2);
      partesTelefone[1] = telefone.substring(2, 6);
      partesTelefone[2] = telefone.substring(6, 10);
    } else {
      partesTelefone[0] = telefone.substring(0, 2);
      partesTelefone[1] = telefone.substring(2, 7);
      partesTelefone[2] = telefone.substring(7, 11);
    }

    StringBuilder sb = new StringBuilder();

    sb.append("(")
        .append(partesTelefone[0])
        .append(") ")
        .append(partesTelefone[1])
        .append("-")
        .append(partesTelefone[2]);

    return sb.toString();
  }

  public static String porMascaraCep(String cep) {
    return cep.substring(0, 5) + "-" + cep.substring(5, 8);
  }
}
