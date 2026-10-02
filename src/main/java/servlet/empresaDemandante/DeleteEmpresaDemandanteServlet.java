package servlet.empresaDemandante;

import exception.GenericExceptionEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.EmpresaDemandante;
import service.EmpresaDemandanteService;

import java.io.IOException;
import java.util.Enumeration;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudEmpresaDemandante-delete")
public class DeleteEmpresaDemandanteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/crudEmpresaDemandante");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            String id = request.getParameter("idDelete");

            GenericExceptionEnum erro = EmpresaDemandanteService.realizarDelete(id);

            if(erro != null){
                session.setAttribute("mensagemDelete", erro.exibirMensagem());
                response.sendRedirect(request.getContextPath() + "/crudEmpresaDemandante");
            } else {
                session.setAttribute("mensagemDelete", "O usuário foi deletado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/crudEmpresaDemandante");
            }
        } catch (Exception e){

            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("mensagemDelete", ERRO_GENERICO.exibirMensagem());
            response.sendRedirect(request.getContextPath() + "/crudEmpresaDemandante");
        }
    }
}