package servlet.usuario;

import static exception.ErrosGerais.ERRO_GENERICO;

import java.util.Enumeration;
import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.usuario.UsuarioDadosDto;
import exception.GenericExceptionEnum;
import service.usuario.UsuarioService;

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

            String tipoUsuarioInsert = request.getParameter("tipoUsuarioInsert");
            String emailInsert = request.getParameter("emailInsert");
            String senhaInsert = request.getParameter("senhaInsert");
            String nomeInsert = request.getParameter("nomeInsert");
            String raioProcuraKmInsert = request.getParameter("raioProcuraKmInsert");

            UsuarioDadosDto usuarioDadosDto = new UsuarioDadosDto(null, tipoUsuarioInsert, emailInsert,
                                                                    senhaInsert, nomeInsert, raioProcuraKmInsert);

            ArrayList<GenericExceptionEnum> mensagensInsert = UsuarioService.realizarInsert(usuarioDadosDto);

            if(!mensagensInsert.isEmpty()){
                session.setAttribute("mensagensInsert", mensagensInsert);
                session.setAttribute("tipoUsuarioInsert", tipoUsuarioInsert);
                session.setAttribute("emailInsert", emailInsert);
                session.setAttribute("nomeInsert", nomeInsert);
                session.setAttribute("raioProcuraKmInsert", raioProcuraKmInsert);

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

            ArrayList<GenericExceptionEnum> mensagensInsert = new ArrayList<>();
            mensagensInsert.add(ERRO_GENERICO);

            session.setAttribute("mensagensInsert", mensagensInsert);
            response.sendRedirect(request.getContextPath() + "/crudUsuario");
        }
    }
}
