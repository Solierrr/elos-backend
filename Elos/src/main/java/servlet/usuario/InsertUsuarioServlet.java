package servlet.usuario;

import exception.GenericExceptionEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.UsuarioService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudUsuario-insert")
public class InsertUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()+"/crudUsuario");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            String tipoUsuario = request.getParameter("tipoUsuarioInsert");
            String email = request.getParameter("emailInsert");
            String senha = request.getParameter("senhaInsert");
            String nome = request.getParameter("nomeInsert");
            String raioProcuraKm = request.getParameter("raioProcuraKmInsert");

            ArrayList<GenericExceptionEnum> mensagens = UsuarioService.realizarInsert(email, senha, nome, tipoUsuario, raioProcuraKm);

            if(!mensagens.isEmpty()){
                session.setAttribute("mensagensInsert", mensagens);
                session.setAttribute("tipoUsuarioInsert", tipoUsuario);
                session.setAttribute("emailInsert", email);
                session.setAttribute("nomeInsert", nome);
                session.setAttribute("raioProcuraKmInsert", raioProcuraKm);

                //Atributo usado no javascript para abrir o pop-up
                session.setAttribute("abrirInsert", true);

                response.sendRedirect(request.getContextPath()+"/crudUsuario");
            } else{
                session.setAttribute("mensagemInsert", "O cadastro foi efetuado com sucesso");
                response.sendRedirect(request.getContextPath() + "/crudUsuario");
            }
        } catch (Exception exception){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            ArrayList<GenericExceptionEnum> mensagens = new ArrayList<>();
            mensagens.add(ERRO_GENERICO);

            session.setAttribute("mensagensInsert", mensagens);
            response.sendRedirect(request.getContextPath() + "/crudUsuario");
        }
    }
}
