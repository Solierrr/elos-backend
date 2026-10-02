package servlet.empresaDemandante;

import exception.GenericExceptionEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.EmpresaDemandanteService;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Enumeration;
import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudEmpresaDemandante-insert")
public class InsertEmpresaDemandanteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()+"/crudEmpresaDemandante");
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

            String idUsuario = request.getParameter("idUsuarioInsert");
            String tipoUsuario = request.getParameter("tipoUsuarioInsert");
            String cnpj = request.getParameter("cnpjInsert");
            String razaoSocial = request.getParameter("razaoSocialInsert");
            String ehMandante = request.getParameter("ehMandanteInsert");

            ArrayList<GenericExceptionEnum> mensagens = EmpresaDemandanteService.realizarInsert(idUsuario, tipoUsuario, cnpj, tipoUsuario, razaoSocial, ehMandante);

            if(!mensagens.isEmpty()){
                session.setAttribute("mensagensInsert", mensagens);
                session.setAttribute("tipoUsuarioInsert", tipoUsuario);
                session.setAttribute("idUsuarioInsert", idUsuario);
                session.setAttribute("cnpjInsert", cnpj);
                session.setAttribute("razaoSocial", razaoSocial);
                session.setAttribute("ehMandanteInsert", ehMandante);

                //Atributo usado no javascript para abrir o pop-up
                session.setAttribute("abrirInsert", true);

                response.sendRedirect(request.getContextPath()+"/crudEmpresaDemandante");
            } else{
                session.setAttribute("mensagemInsert", "O cadastro foi efetuado com sucesso");
                response.sendRedirect(request.getContextPath() + "/crudEmpresaDemandante");
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
            response.sendRedirect(request.getContextPath() + "/crudEmpresaDemandante");
        }
    }
}

