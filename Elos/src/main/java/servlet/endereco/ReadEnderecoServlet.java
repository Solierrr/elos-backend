package servlet.endereco;

import exception.GenericExceptionEnum;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Endereco;
import service.EnderecoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudEndereco")
public class ReadEnderecoServlet extends HttpServlet {

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
            List<Endereco> enderecoRead = new ArrayList<>();

            enderecoRead.addAll(EnderecoService.realizarSelect(clausulaWhereNome, clausulaWhereValor, clausulaWhereValor2, orderBy, ordenacao, errosRead));

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("enderecoRead", enderecoRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/endereco/crudEndereco.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e){
            ArrayList<GenericExceptionEnum> errosRead = new ArrayList<>();
            errosRead.add(ERRO_GENERICO);

            List<Endereco> enderecoRead = EnderecoService.realizarSelect(null, null, null, null, null, errosRead);

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("enderecoRead", enderecoRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/endereco/crudEndereco.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}