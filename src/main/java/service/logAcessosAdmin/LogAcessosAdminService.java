package service.logAcessosAdmin;

import static exception.ErrosGerais.ADMIN_INATIVO;
import static exception.ErrosGerais.SENHA_INVALIDA;
import static exception.ErrosLogAcessosAdmin.NAVEGADOR_INVALIDO;
import static exception.ErrosLogAcessosAdmin.SISTEMA_OPERACIONAL_INVALIDO;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.ENDERECO_IP;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.ID;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.ID_ADMIN;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.MOTIVO_FALHA;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.NAVEGADOR;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.SISTEMA_OPERACIONAL;
import static service.logAcessosAdmin.CamposLogAcessosAdmin.SUCESSO;

import dao.CriarInstrucaoDinamica;
import dao.LogAcessosAdminDAO;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.NavegadoresAdmin;
import model.SistemasOperacionaisAdmin;

public final class LogAcessosAdminService {

    private static final int INET = -1000;

    public List<GenericExceptionEnum> validarDados(String userAgent){
        List<GenericExceptionEnum> erros = new ArrayList<>();
        if(NavegadoresAdmin.descobrirNavegadorAdmin(userAgent) == NavegadoresAdmin.NAVEGADOR_INVALIDO)
            erros.add(NAVEGADOR_INVALIDO);

        if(SistemasOperacionaisAdmin.descobrirSistemaOperacionalAdmin(userAgent) == SistemasOperacionaisAdmin.SISTEMA_OPERACIONAL_INVALIDO)
            erros.add(SISTEMA_OPERACIONAL_INVALIDO);

        return erros;
    }

    public void salvarAcessoDoAdmin(List<GenericExceptionEnum> erros, String userAgent, String ip, Long idAdmin, Connection connection) throws SQLException {
        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

        StringBuilder mensagemErro = new StringBuilder();

        if(idAdmin == null){
            return;
        }

        criarInstrucaoDinamica.setCampo(ID_ADMIN.getCampoLogAcessosAdmin(),idAdmin, Types.BIGINT);


        if(erros.contains(NAVEGADOR_INVALIDO)) {
            criarInstrucaoDinamica.setCampo(NAVEGADOR.getCampoLogAcessosAdmin(), NavegadoresAdmin.NAVEGADOR_INVALIDO.getNavegador(), Types.VARCHAR);
            mensagemErro.append("Navegador inválido");
        }else{
            criarInstrucaoDinamica.setCampo(NAVEGADOR.getCampoLogAcessosAdmin(), NavegadoresAdmin.descobrirNavegadorAdmin(userAgent).getNavegador(), Types.VARCHAR);
        }

        if(erros.contains(SISTEMA_OPERACIONAL_INVALIDO)) {
            criarInstrucaoDinamica.setCampo(SISTEMA_OPERACIONAL.getCampoLogAcessosAdmin(),
                    SistemasOperacionaisAdmin.SISTEMA_OPERACIONAL_INVALIDO.getSistemaOperacional(),
                    Types.VARCHAR);
            if(mensagemErro.isEmpty()){
                mensagemErro.append("Sistema operacional inválido");
            }else{
                mensagemErro.append("e sistema operacional inválido");
            }
        }else{
            criarInstrucaoDinamica.setCampo(SISTEMA_OPERACIONAL.getCampoLogAcessosAdmin(),
                    SistemasOperacionaisAdmin.descobrirSistemaOperacionalAdmin(userAgent).getSistemaOperacional(),
                    Types.VARCHAR);
        }

        if(erros.contains(ADMIN_INATIVO)){
            if(mensagemErro.isEmpty()){
                mensagemErro.append("A conta do administrator está desativada");
            }else{
                mensagemErro.append("e a conta do administrator está desativada");
            }
        }

        if(erros.contains(SENHA_INVALIDA)){
            if(mensagemErro.isEmpty()){
                mensagemErro.append("Senha inválida");
            }else{
                mensagemErro.append("e sistema operacional inválido");
            }
        }



        criarInstrucaoDinamica.setCampo(ENDERECO_IP.getCampoLogAcessosAdmin(), ip, INET);

        if(erros.isEmpty()){
            criarInstrucaoDinamica.setCampo(SUCESSO.getCampoLogAcessosAdmin(), true, Types.BOOLEAN);
            criarInstrucaoDinamica.setCampo(MOTIVO_FALHA.getCampoLogAcessosAdmin(), null, Types.VARCHAR);
            System.out.println("deu certo");
        }else{
            criarInstrucaoDinamica.setCampo(SUCESSO.getCampoLogAcessosAdmin(), false, Types.BOOLEAN);
            criarInstrucaoDinamica.setCampo(MOTIVO_FALHA.getCampoLogAcessosAdmin(), mensagemErro.toString(), Types.VARCHAR);
            System.out.println("deu errado");
        }

        LogAcessosAdminDAO logAcessosAdminDAO = new LogAcessosAdminDAO();

        logAcessosAdminDAO.insert(criarInstrucaoDinamica, connection);
    }

}
