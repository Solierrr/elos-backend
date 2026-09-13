package service.usuario;

import dao.AcoesInstrucao;
import service.GenericEnumCampos;

import java.sql.Types;

import static dao.AcoesInstrucao.*;

public enum CamposUsuario implements GenericEnumCampos {

    ID("id", true, IGUAL, Types.BIGINT),
    TIPO_USUARIO("tipo_usuario", true, IGUAL, Types.VARCHAR),
    SENHA("senha", false, VAZIO, Types.VARCHAR),
    EMAIL("email", true, IGUAL, Types.VARCHAR),
    NOME("nome", true, ILIKE, Types.VARCHAR),
    RAIO_PROCURA_KM("raio_procura_km", true, BETWEEN, Types.DOUBLE),
    GENERICO("campo_usado_quando_nao_ocorre_filtragem_ou_ordenacao", true, VAZIO, 0),
    INVALIDO("campo_do_usuario_invalido", true, VAZIO, 0);

    private final String campoUsuario;
    private final boolean acessivel;
    private final AcoesInstrucao acao;
    private final int dataType;

    CamposUsuario(String campoUsuario, boolean acessivel, AcoesInstrucao acao, int dataType) {
        this.campoUsuario = campoUsuario;
        this.acessivel = acessivel;
        this.acao = acao;
        this.dataType = dataType;
    }

    public String getCampoUsuario() {
        return campoUsuario;
    }

    public boolean isAcessivel() {
        return acessivel;
    }

    public AcoesInstrucao getAcao() {
        return acao;
    }

    public int getDataType() {
        return dataType;
    }

    public static CamposUsuario descobrirCampoUsuario(String campoUsuarioEntrada){
        if(campoUsuarioEntrada == null || campoUsuarioEntrada.isBlank()){
            return INVALIDO;
        }

        String campoUsuarioEntradaTratado = campoUsuarioEntrada.strip().toLowerCase();
        for(CamposUsuario campoUsuario : CamposUsuario.values()){
            if(campoUsuario.getCampoUsuario().equalsIgnoreCase(campoUsuarioEntradaTratado) && campoUsuario.isAcessivel()){
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
