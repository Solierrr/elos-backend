package servlet.empresaDemandante;

import exception.GenericExceptionEnum;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.EmpresaDemandante;
import service.empresaDemandante.EmpresaDemandanteDadosDePesquisaDTO;
import service.empresaDemandante.EmpresaDemandanteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudEmpresaDemandante")
public final class ReadEmpresaDemandanteServlet extends HttpServlet {

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
            String ordenacao = request.getParameter("ordenacao");

            List<GenericExceptionEnum> errosRead = new ArrayList<>();
            List<EmpresaDemandante> empresaDemandanteRead = new ArrayList<>();

            EmpresaDemandanteDadosDePesquisaDTO empresaDemandanteDadosDePesquisaDTO = new EmpresaDemandanteDadosDePesquisaDTO(clausulaWhereNome, clausulaWhereValor, orderBy, ordenacao);
            empresaDemandanteRead.addAll(EmpresaDemandanteService.realizarSelect(empresaDemandanteDadosDePesquisaDTO, errosRead));

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("usuariosRead", empresaDemandanteRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/empresaDemandante/crudEmpresaDemandante.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e){
            List<GenericExceptionEnum> errosRead = new ArrayList<>();
            errosRead.add(ERRO_GENERICO);

            EmpresaDemandanteDadosDePesquisaDTO dadosDePesquisaDTO = new EmpresaDemandanteDadosDePesquisaDTO(null, null, null, null);
            List<EmpresaDemandante> empresaDemandanteRead = EmpresaDemandanteService.realizarSelect(dadosDePesquisaDTO, errosRead);

            request.setAttribute("errosRead", errosRead);
            request.setAttribute("empresaDemandanteRead", empresaDemandanteRead);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/empresaDemandante/crudEmpresaDemandante.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
