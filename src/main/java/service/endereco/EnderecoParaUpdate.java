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

        String cidadeTratada = cidadeUpdate == null || cidadeUpdate.isBlank() ? null : cidadeUpdate.strip();
        String bairroTratado = bairroUpdate == null || bairroUpdate.isBlank() ? null : bairroUpdate.strip();
        String cepTratado = cepUpdate == null || cepUpdate.isBlank() ? null : cepUpdate.strip().replace("-", "");
        String logradouroTratado = logradouroUpdate == null || logradouroUpdate.isBlank() ? null : logradouroUpdate.strip();
        String numeroTratado = numeroUpdate == null || numeroUpdate.isBlank() ? null : numeroUpdate.strip();
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