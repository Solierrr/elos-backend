package servlet.telefone;

import exception.GenericExceptionEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.TelefoneService;
import model.Telefone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudTelefone-update")
public class UpdateTelefoneServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            String id = request.getParameter("idUpdate");
            Telefone telefone = TelefoneService.exibirFornecedorParaUpdate(id);

            if (telefone == null){
                session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
                response.sendRedirect(request.getContextPath()+"/crudTelefone");
            } else {
                session.setAttribute("telefoneUpdate", telefone);
                session.setAttribute("abrirUpdate", true);
                response.sendRedirect(request.getContextPath()+"/crudTelefone");
            }
        } catch (Exception e){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
            response.sendRedirect(request.getContextPath()+"/crudTelefone");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();

            String idUpdate = request.getParameter("idUpdate");
            String idUsuarioUpdate = request.getParameter("idUsuarioUpdate");
            String telefoneUpdate = request.getParameter("telefoneUpdate");
            String tipoUpdate = request.getParameter("tipoUpdate");
            String principalUpdate = request.getParameter("principalUpdate");

            ArrayList<GenericExceptionEnum> erros = TelefoneService.realizarUpdate(idUpdate, telefoneUpdate, idUsuarioUpdate, tipoUpdate, principalUpdate);

            if(!erros.isEmpty()){
                session.setAttribute("errosUpdate", erros);
                response.sendRedirect(request.getContextPath()+"/crudTelefone");
            } else{
                //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
                Enumeration<String> attributes = session.getAttributeNames();
                while(attributes.hasMoreElements()){
                    session.removeAttribute(attributes.nextElement());
                }

                session.setAttribute("mensagemUpdate", "Os dados foram atualizados com sucesso");
                response.sendRedirect(request.getContextPath()+"/crudTelefone");
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();

            ArrayList<GenericExceptionEnum> erros = new ArrayList<>();
            erros.add(ERRO_GENERICO);

            session.setAttribute("errosUpdate", erros);
            response.sendRedirect(request.getContextPath()+"/crudTelefone");
        }
    }
}