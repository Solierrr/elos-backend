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

import model.Admin;
import model.Usuario;
import service.usuario.UsuarioDadosDePesquisaDTO;
import service.usuario.UsuarioService;
import exception.GenericExceptionEnum;

@WebServlet("/crudUsuario")
public final class ReadUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {

            //Fazemos isso para garantir que o usuario depois de sair da tela do crud não consiga voltar pelas setinhas do navegador
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");

            HttpSession httpSession = request.getSession();
            Admin admin = (Admin) httpSession.getAttribute("adm");


            if(admin == null){
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            System.out.println(admin);

//            String clausulaWhereNome = request.getParameter("clausulaWhereNome");
//            String clausulaWhereValor = request.getParameter("clausulaWhereValor");
//            String clausulaWhereValor2 = request.getParameter("clausulaWhereValor2");
//            String orderBy = request.getParameter("orderBy");
//            String ordenacao = request.getParameter("sentidoOrderBy");
//
//            ArrayList<GenericExceptionEnum> errosRead = new ArrayList<>();
//            List<Usuario> usuariosRead = new ArrayList<>();
//            UsuarioDadosDePesquisaDTO usuarioDadosDePesquisaDto = new UsuarioDadosDePesquisaDTO(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, orderBy, ordenacao);
//
//            usuariosRead.addAll(UsuarioService.realizarSelect(usuarioDadosDePesquisaDto, errosRead));
//
//            request.setAttribute("errosRead", errosRead);
//            request.setAttribute("usuariosRead", usuariosRead);

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
