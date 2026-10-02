package servlet.usuario;

import static exception.ErrosGerais.ERRO_GENERICO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Usuario;
import service.usuario.UsuarioDadosDePesquisaDto;
import service.usuario.UsuarioService;
import exception.GenericExceptionEnum;

@WebServlet("/crudUsuario")
public class ReadUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            HttpSession session = request.getSession();

            String sairPressionado = request.getParameter("sairPressionado");

            if(sairPressionado != null && sairPressionado.equalsIgnoreCase("true")){
                Enumeration<String> attributes = session.getAttributeNames();
                while(attributes.hasMoreElements()){
                    session.removeAttribute(attributes.nextElement());
                }
            }

            String clausulaWhereNome = request.getParameter("clausulaWhereNome");
            String clausulaWhereValor = request.getParameter("clausulaWhereValor");
            String clausulaWhereValor2 = request.getParameter("clausulaWhereValor2");
            String orderBy = request.getParameter("orderBy");
            String ordenacao = request.getParameter("sentidoOrderBy");

            ArrayList<GenericExceptionEnum> errosRead = new ArrayList<>();
            List<Usuario> usuariosRead = new ArrayList<>();
            UsuarioDadosDePesquisaDto usuarioDadosDePesquisaDto = new UsuarioDadosDePesquisaDto(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, orderBy, ordenacao);
            usuariosRead.addAll(UsuarioService.realizarSelect(usuarioDadosDePesquisaDto, errosRead));

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("usuariosRead", usuariosRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/usuario/crudUsuario.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e){
            ArrayList<GenericExceptionEnum> errosRead = new ArrayList<>();
            errosRead.add(ERRO_GENERICO);

            List<Usuario> usuariosRead = UsuarioService.realizarSelect(null, errosRead);

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("usuariosRead", usuariosRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/usuario/crudUsuario.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
