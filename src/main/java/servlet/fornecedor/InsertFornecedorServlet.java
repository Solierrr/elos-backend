package servlet.fornecedor;

import static exception.ErrosGerais.ERRO_GENERICO;

import java.util.Enumeration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.fornecedor.FornecedorParaInsertDTO;
import service.fornecedor.FornecedorService;
import exception.GenericExceptionEnum;

@WebServlet("/crudFornecedor-insert")
public final class InsertFornecedorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()+"/crudFornecedor");
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
            String tipoFornecedor = request.getParameter("tipoFornecedorInsert");
            String cnpj = request.getParameter("cnpjInsert");
            String razaoSocial = request.getParameter("razaoSocialInsert");

            FornecedorParaInsertDTO fornecedorParaInsertDto = new FornecedorParaInsertDTO(null, idUsuario, tipoFornecedor, cnpj, razaoSocial);
            List<GenericExceptionEnum> mensagens = FornecedorService.realizarInsert(fornecedorParaInsertDto);

            if(!mensagens.isEmpty()){
                session.setAttribute("mensagensInsert", mensagens);
                session.setAttribute("tipoUsuarioInsert", tipoUsuario);
                session.setAttribute("tipoFornecedorInsert", tipoFornecedor);
                session.setAttribute("cnpjInsert", cnpj);
                session.setAttribute("razaoSocialInsert", razaoSocial);

                //Atributo usado no javascript para abrir o pop-up
                session.setAttribute("abrirInsert", true);

                response.sendRedirect(request.getContextPath()+"/crudFornecedor");
            } else{
                session.setAttribute("mensagemInsert", "O cadastro foi efetuado com sucesso");
                response.sendRedirect(request.getContextPath() + "/crudFornecedor");
            }
        } catch (Exception exception){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            List<GenericExceptionEnum> mensagens = new ArrayList<>();
            mensagens.add(ERRO_GENERICO);

            session.setAttribute("mensagensInsert", mensagens);
            response.sendRedirect(request.getContextPath() + "/crudFornecedor");
        }
    }
}