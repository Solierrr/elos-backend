package service.endereco;

import model.Endereco;
import model.EstadosBrasileiros;

public record EnderecoParaUpdate(
        String idUpdate,
        String estadoUpdate,
        String cidadeUpdate,
        String bairroUpdate,
        String cepUpdate,
        String logradouroUpdate,
        String numeroUpdate,
        String complementoUpdate) {

    public Endereco construirEndereco() {
        EstadosBrasileiros estadosBrasileiros = EstadosBrasileiros.descobrirEstadoBrasileiroPorSigla(estadoUpdate);

        String cidadeTratada = cidadeUpdate.strip();
        String bairroTratado = bairroUpdate.strip();
        String cepTratado = cepUpdate.strip().replace("-", "");
        String logradouroTratado = logradouroUpdate.strip();
        String numeroTratado = numeroUpdate.strip();
        String complementoTratado =
                complementoUpdate == null || complementoUpdate.isBlank() ? null : complementoUpdate.strip();

        return new Endereco(
                Long.parseLong(idUpdate.strip()),
                null,
                estadosBrasileiros,
                cidadeTratada,
                bairroTratado,
                cepTratado,
                logradouroTratado,
                numeroTratado,
                complementoTratado);
    }
}