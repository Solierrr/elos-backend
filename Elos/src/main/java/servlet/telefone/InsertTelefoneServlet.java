package servlet.telefone;

import exception.GenericExceptionEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.TelefoneService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudTelefone-insert")
public class InsertTelefoneServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/crudTelefone");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                session.removeAttribute(attributes.nextElement());
            }

            String idUsuario = request.getParameter("idUsuarioInsert");
            String telefone = request.getParameter("telefoneInsert");
            String tipo = request.getParameter("tipoInsert");
            String principal = request.getParameter("principalInsert");

            ArrayList<GenericExceptionEnum> mensagens = TelefoneService.realizarInsert(idUsuario, telefone, tipo, principal);

            if (!mensagens.isEmpty()) {
                session.setAttribute("mensagensInsert", mensagens);
                session.setAttribute("idUsuarioInsert", idUsuario);
                session.setAttribute("telefoneInsert", telefone);
                session.setAttribute("tipoInsert", tipo);
                session.setAttribute("principalInsert", principal);

                //Atributo usado no javascript para abrir o pop-up
                session.setAttribute("abrirInsert", true);

                response.sendRedirect(request.getContextPath() + "/crudTelefone");
            } else {
                session.setAttribute("mensagemInsert", "O cadastro foi efetuado com sucesso");
                response.sendRedirect(request.getContextPath() + "/crudTelefone");
            }
        } catch (Exception exception) {
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                session.removeAttribute(attributes.nextElement());
            }

            ArrayList<GenericExceptionEnum> mensagens = new ArrayList<>();
            mensagens.add(ERRO_GENERICO);

            session.setAttribute("mensagensInsert", mensagens);
            response.sendRedirect(request.getContextPath() + "/crudTelefone");
        }
    }
}
