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
import java.util.Map;
import model.Admin;
import model.FuncoesAdmin;
import service.admin.AdminService;
import service.admin.CamposAdmin;
import service.logAcessosAdmin.LogAcessosAdminService;
import util.Criptografia;

public final class LoginService {

    public Admin realizarLogin(String email, String senha, String userAgent, String ip) {
        if (!AdminService.validarDadosLogin(email, senha)) {
            return null;
        }
        return logar(email, senha, userAgent, ip);
    }

    private Admin logar(String email, String senha, String userAgent, String ip) {
        LogAcessosAdminService logAcessosAdminService = new LogAcessosAdminService();

        List<GenericExceptionEnum> erros = logAcessosAdminService.validarDados(userAgent);

        try (Connection connection = Conexao.getConnection()) {
            connection.setAutoCommit(false);

            try {
                Admin admin = buscarAdminPorEmail(email, connection);
                Long idAdmin;
                if (admin != null) {
                    if (!admin.isEmAtividade()) erros.add(ADMIN_INATIVO);
                    else if (!senhaCerta(admin.getSenha(), senha)) erros.add(SENHA_INVALIDA);
                    idAdmin = admin.getId();
                    admin = new Admin(admin.getId(), admin.getEmail(), admin.getNome(), "SENHA_SECRETA", admin.getFuncao(), admin.isEmAtividade());
                } else {
                    idAdmin = null;
                }


                logAcessosAdminService.salvarAcessoDoAdmin(erros, userAgent, ip, idAdmin, connection);

                connection.commit();
                return erros.isEmpty() ? admin : null;
                //Essa escolha foi feita para que o usuário só saiba que não deu certo, garantindo que ele não saiba se foi o email ou senha
            } catch (SQLException | IllegalArgumentException exception) {
                connection.rollback();
                throw exception;
            }
        } catch (SQLException | IllegalArgumentException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private Admin buscarAdminPorEmail(String email, Connection connection) throws SQLException {
        AdminDAO dao = new AdminDAO();
        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

        criarInstrucaoDinamica.setWhere("email", AcoesInstrucao.IGUAL, AcoesInstrucao.VAZIO, email.strip(), Types.VARCHAR);
        List<Map<String, Object>> admins = dao.readAll(criarInstrucaoDinamica, connection);

        if (admins.isEmpty()) return null;

        Map<String, Object> adminMap = admins.getFirst();
        try {
            Object idObj = adminMap.get(CamposAdmin.ID.getCampoAdmin());

            if(!(idObj instanceof Number))
                return null;

            return new Admin(((Number) idObj).longValue(),
                            (String) adminMap.get(CamposAdmin.EMAIL.getCampoAdmin()),
                            (String) adminMap.get(CamposAdmin.NOME.getCampoAdmin()),
                            (String) adminMap.get(CamposAdmin.SENHA.getCampoAdmin()),
                            FuncoesAdmin.descobrirFuncaoAdmin((String) adminMap.get(CamposAdmin.EMAIL.getCampoAdmin())),
                            (Boolean) adminMap.get(CamposAdmin.EM_ATIVIDADE.getCampoAdmin()));
        }catch (ClassCastException classCastException){
            return null;
        }
    }

    private boolean senhaCerta(String senhaEmHash, String senhaEntrada) {
        Criptografia criptografia = new Criptografia();

        return criptografia.verificarSeSenhaBate(senhaEntrada.strip(), senhaEmHash);
    }

}
