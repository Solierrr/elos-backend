package servlet.fornecedor;

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

import model.Fornecedor;
import service.fornecedor.FornecedorDadosDePesquisaDTO;
import service.fornecedor.FornecedorService;
import exception.GenericExceptionEnum;

@WebServlet("/crudFornecedor")
public final class ReadFornecedorServlet extends HttpServlet {

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
            String orderBy = request.getParameter("orderBy");
            String sentidoOrderBy = request.getParameter("sentidoOrderBy");

            List<GenericExceptionEnum> errosRead = new ArrayList<>();
            List<Fornecedor> fornecedoresRead = new ArrayList<>();
            FornecedorDadosDePesquisaDTO fornecedorDadosDePesquisaDto =  new FornecedorDadosDePesquisaDTO(clausulaWhereNome, clausulaWhereValor, orderBy, sentidoOrderBy);

            fornecedoresRead.addAll(FornecedorService.realizarSelect(fornecedorDadosDePesquisaDto, errosRead));

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("fornecedoresRead", fornecedoresRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/fornecedor/crudFornecedor.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e){
            List<GenericExceptionEnum> errosRead = new ArrayList<>();
            errosRead.add(ERRO_GENERICO);

            List<Fornecedor> fornecedorRead = FornecedorService.realizarSelect(null, errosRead);

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("fornecedorRead", fornecedorRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/fornecedor/crudFornecedor.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
