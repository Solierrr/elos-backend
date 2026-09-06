package servlet.endereco;

import exception.GenericExceptionEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.EnderecoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import static exception.ErrosGerais.ERRO_GENERICO;

@WebServlet("/crudEndereco-insert")
public class InsertEnderecoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()+"/crudEndereco");
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
            String estado = request.getParameter("estadoInsert");
            String cidade = request.getParameter("cidadeInsert");
            String bairro = request.getParameter("bairroInsert");
            String cep = request.getParameter("cepInsert");
            String logradouro = request.getParameter("logradouroInsert");
            String numero = request.getParameter("numeroInsert");
            String complemento = request.getParameter("complementoInsert");

            ArrayList<GenericExceptionEnum> mensagens = EmpresaDemandanteService.realizarInsert(idUsuario, estado, cidade, bairro, cep, logradouro, numero, complemento);

            if(!mensagens.isEmpty()){
                session.setAttribute("mensagensInsert", mensagens);
                session.setAttribute("estadoInsert", estado);
                session.setAttribute("idUsuarioInsert", idUsuario);
                session.setAttribute("cidadeInsert", cidade);
                session.setAttribute("bairroInsert", bairro);
                session.setAttribute("cepInsert", cep);
                session.setAttribute("logradouroInsert", logradouro);
                session.setAttribute("numeroInsert", numero);
                session.setAttribute("complementoInsert", complemento);

                //Atributo usado no javascript para abrir o pop-up
                session.setAttribute("abrirInsert", true);

                response.sendRedirect(request.getContextPath()+"/crudEndereco");
            } else{
                session.setAttribute("mensagemInsert", "O cadastro foi efetuado com sucesso");
                response.sendRedirect(request.getContextPath() + "/crudEndereco");
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
            response.sendRedirect(request.getContextPath() + "/crudEndereco");
        }
    }
}