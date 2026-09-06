package servlet.endereco;

import exception.GenericExceptionEnum;

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

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudEndereco-update")
public class UpdateEnderecoServlet extends HttpServlet{

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
            Endereco endereco = EnderecoService.exibirFornecedorParaUpdate(id);

            if (endereco == null){
                session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
                response.sendRedirect(request.getContextPath()+"/crudEndereco");
            } else {
                session.setAttribute("enderecoUpdate", endereco);
                session.setAttribute("abrirUpdate", true);
                response.sendRedirect(request.getContextPath()+"/crudEndereco");
            }
        } catch (Exception e){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
            response.sendRedirect(request.getContextPath()+"/crudEndereco");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();

            String idUpdate = request.getParameter("idUpdate");
            String idUsuarioUpdate = request.getParameter("idUsuarioUpdate");
            String estadoUpdate = request.getParameter("estadoUpdate");
            String cidadeUpdate = request.getParameter("cidadeUpdate");
            String bairroUpdate = request.getParameter("bairroUpdate");
            String cepUpdate = request.getParameter("cepUpdate");
            String logradouroUpdate = request.getParameter("logradouroUpdate");
            String numeroUpdate = request.getParameter("numeroUpdate");
            String complementoUpdate = request.getParameter("complementoUpdate");

            ArrayList<GenericExceptionEnum> erros = EnderecoService.realizarUpdate(idUpdate, idUsuarioUpdate, estadoUpdate, cidadeUpdate, bairroUpdate, cepUpdate, logradouroUpdate, numeroUpdate, complementoUpdate);

            if(!erros.isEmpty()){
                session.setAttribute("errosUpdate", erros);
                response.sendRedirect(request.getContextPath()+"/crudEndereco");
            } else{
                //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
                Enumeration<String> attributes = session.getAttributeNames();
                while(attributes.hasMoreElements()){
                    session.removeAttribute(attributes.nextElement());
                }

                session.setAttribute("mensagemUpdate", "Os dados foram atualizados com sucesso");
                response.sendRedirect(request.getContextPath()+"/crudEndereco");
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();

            ArrayList<GenericExceptionEnum> erros = new ArrayList<>();
            erros.add(ERRO_GENERICO);

            session.setAttribute("errosUpdate", erros);
            response.sendRedirect(request.getContextPath()+"/crudEndereco");
        }
    }
}
