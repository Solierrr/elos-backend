package service.login;

import static exception.ErrosGerais.ADMIN_INATIVO;
import static exception.ErrosGerais.SENHA_INVALIDA;

import conexao.Conexao;
import dao.AcoesInstrucao;
import dao.AdminDAO;
import dao.CriarInstrucaoDinamica;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import model.Admin;
import service.admin.AdminService;
import service.logAcessosAdmin.LogAcessosAdminService;
import util.Criptografia;

public final class LoginService {

    public Admin realizarLogin(String email, String senha, String userAgent, String ip){
        if(!AdminService.validarDadosLogin(email, senha)){
            return null;
        }
        return logar(email, senha, userAgent, ip);
    }

    private Admin logar(String email, String senha, String userAgent, String ip){
        LogAcessosAdminService logAcessosAdminService = new LogAcessosAdminService();

        List<GenericExceptionEnum> erros = logAcessosAdminService.validarDados(userAgent);

        try(Connection connection = Conexao.getConnection()){
            Admin admin = buscarAdminPorEmail(email, connection);
            Long idAdmin;
            if(admin != null){
                if(!admin.isEmAtividade())
                    erros.add(ADMIN_INATIVO);
                else if(!senhaCerta(admin.getSenha(), senha))
                    erros.add(SENHA_INVALIDA);
                idAdmin = admin.getId();
                admin = new Admin(admin.getId(), admin.getEmail(), admin.getNome(), "SENHA_SECRETA", admin.getFuncao(), admin.isEmAtividade());
            }else{
                idAdmin = null;
            }

            logAcessosAdminService.salvarAcessoDoAdmin(erros, userAgent, ip, idAdmin, connection);

            return erros.isEmpty() ? admin : null;
            //Essa escolha foi feita para que o usuário só saiba que não deu certo, garantindo que ele não saiba se foi o email ou senha
        }catch (SQLException | IllegalArgumentException exception){
            exception.printStackTrace();
            return null;
        }
    }

    private Admin buscarAdminPorEmail(String email, Connection connection) throws SQLException{
        AdminDAO dao = new AdminDAO();
        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

        criarInstrucaoDinamica.setWhere("email", AcoesInstrucao.IGUAL, AcoesInstrucao.VAZIO, email.strip(), Types.VARCHAR);
        List<Admin> admins = dao.readAll(criarInstrucaoDinamica, connection);

        return !admins.isEmpty() ? admins.getFirst() : null;
    }

    private boolean senhaCerta(String senhaEmHash, String senhaEntrada){
        Criptografia criptografia = new Criptografia();

        return criptografia.verificarSeSenhaBate(senhaEntrada.strip(), senhaEmHash);
    }

}
