package servlet.profissional;

import exception.GenericExceptionEnum;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Profissional;
import service.ProfissionalService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudProfissionl")
public class ReadProfissionalServlet extends HttpServlet {

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
            String ordenacao = request.getParameter("ordenacao");

            ArrayList<GenericExceptionEnum> errosRead = new ArrayList<>();
            List<Profissional> profissionalRead = new ArrayList<>();

            profissionalRead.addAll(ProfissionalService.realizarSelect(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, orderBy, ordenacao, errosRead));

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("profissionalRead", profissionalRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/profissional/crudProfissional.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e){
            ArrayList<GenericExceptionEnum> errosRead = new ArrayList<>();
            errosRead.add(ERRO_GENERICO);

            List<Profissional> profissionalRead = ProfissionalService.realizarSelect(null, null, null, null, null, errosRead);

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("profissionalRead", profissionalRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/profissional/crudProfissional.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}