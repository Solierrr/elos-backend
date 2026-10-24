package service.usuario;

import dao.AcoesInstrucao;
import service.GenericEnumCampos;

import java.sql.Types;

import static dao.AcoesInstrucao.*;

public enum CamposUsuario implements GenericEnumCampos {

    ID("id"),
    TIPO_USUARIO("tipo_usuario"),
    SENHA("senhaUpdate"),
    EMAIL("emailUpdate"),
    NOME("nomeUpdate"),
    RAIO_PROCURA_KM("raio_procura_km"),
    INVALIDO("campo_do_usuario_invalido");

    private final String campoUsuario;

    CamposUsuario(String campoUsuario) {
        this.campoUsuario = campoUsuario;
    }

    public String getCampoUsuario() {
        return campoUsuario;
    }

    public static CamposUsuario descobrirCampoUsuario(String campoUsuarioEntrada){
        if(campoUsuarioEntrada == null || campoUsuarioEntrada.isBlank()){
            return INVALIDO;
        }

        String campoUsuarioEntradaTratado = campoUsuarioEntrada.strip().toLowerCase();
        for(CamposUsuario campoUsuario : CamposUsuario.values()){
            if(campoUsuario.getCampoUsuario().equalsIgnoreCase(campoUsuarioEntradaTratado)){
                return campoUsuario;
            }
        }
        return INVALIDO;
    }

    @Override
    public boolean isValido(){
        return this != INVALIDO;
    }

}
