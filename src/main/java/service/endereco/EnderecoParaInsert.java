package service.endereco;

import model.Endereco;
import model.EstadosBrasileiros;

public record EnderecoParaInsert(
    String idUsuario,
    String estado,
    String cidade,
    String bairro,
    String cep,
    String logradouro,
    String numero,
    String complemento) {

  public Endereco construirEndereco() {
    Long idUsuarioTratado = Long.parseLong(idUsuario.strip());

    EstadosBrasileiros estadosBrasileiros =
        EstadosBrasileiros.descobrirEstadoBrasileiroPorSigla(estado);

    String cidadeTratada = cidade.strip();
    String bairroTratado = bairro.strip();
    String cepTratado = cep.strip().replace("-", "");
    String logradouroTratado = logradouro.strip();
    String numeroTratado = numero.strip();
    String complementoTratado =
        complemento == null || complemento.isBlank() ? null : complemento.strip();

    return new Endereco(
        null,
        idUsuarioTratado,
        estadosBrasileiros,
        cidadeTratada,
        bairroTratado,
        cepTratado,
        logradouroTratado,
        numeroTratado,
        complementoTratado);
  }
}
