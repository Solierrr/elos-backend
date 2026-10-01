package servlet.usuario;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import exception.GenericExceptionEnum;
import service.fornecedor.FornecedorService;
import service.usuario.UsuarioService;

@WebServlet("/crudUsuario-delete")
public final class DeleteUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/crudUsuario");
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
            GenericExceptionEnum erro = UsuarioService.realizarDelete(id);
            if(erro != null && erro != SUCESSO ){
                session.setAttribute("mensagemErroDelete", erro);
                response.sendRedirect(request.getContextPath() + "/crudUsuario");
            } else {
                session.setAttribute("mensagemDelete", "O usuário foi deletado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/crudUsuario");
            }
        } catch (Exception e){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("mensagemDelete", ERRO_GENERICO.exibirMensagem());
            response.sendRedirect(request.getContextPath() + "/crudUsuario");
        }
    }
}