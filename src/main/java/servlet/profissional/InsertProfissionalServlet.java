package servlet.profissional;

import exception.GenericExceptionEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ProfissionalService;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Enumeration;
import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudProfissional-insert")
public class InsertProfissionalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()+"/crudProfissional");
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
            String profissao = request.getParameter("profissaoInsert");
            String cpf = request.getParameter("cpfInsert");
            String idFornecedor = request.getParameter("idFornecedorInsert");

            ArrayList<GenericExceptionEnum> mensagens = ProfissionalService.realizarInsert(idUsuario, profissao, cpf, tipoUsuario, idFornecedor);

            if(!mensagens.isEmpty()){
                session.setAttribute("mensagensInsert", mensagens);
                session.setAttribute("idUsuarioInsert", idUsuario);
                session.setAttribute("tipoUsuarioInsert", tipoUsuario);
                session.setAttribute("profissaoInsert", profissao);
                session.setAttribute("cpfInsert", cpf);
                session.setAttribute("idFornecedorInsert", idFornecedor);

                //Atributo usado no javascript para abrir o pop-up
                session.setAttribute("abrirInsert", true);

                response.sendRedirect(request.getContextPath()+"/crudProfissional");
            } else{
                session.setAttribute("mensagemInsert", "O cadastro foi efetuado com sucesso");
                response.sendRedirect(request.getContextPath() + "/crudProfissional");
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
            response.sendRedirect(request.getContextPath() + "/crudProfissional");
        }
    }
}