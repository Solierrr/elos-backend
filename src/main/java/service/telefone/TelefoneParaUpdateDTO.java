package service.telefone;

import model.Telefone;
import model.TiposTelefone;

public record TelefoneParaUpdateDTO(String idUpdate, String telefoneUpdate, String tipoUpdate, String principalUpdate) {

    public Telefone construirTelefone(){
        String telefoneTratado = telefoneUpdate.strip().replace("(", "").replace(")", "").replaceAll("\\s", "");

        TiposTelefone tiposTelefone = TiposTelefone.descobrirTipoTelefone(tipoUpdate);

        Boolean principalTratado = Boolean.parseBoolean(principalUpdate);

        return new Telefone(Long.parseLong(idUpdate.strip()), null, telefoneTratado,  tiposTelefone, principalTratado);
    }

}
