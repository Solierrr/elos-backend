package servlet.fornecedor;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.REGISTRO_NAO_ENCONTRADO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import exception.GenericExceptionEnum;
import model.Fornecedor;
import service.fornecedor.FornecedorParaInsertDTO;
import service.fornecedor.FornecedorService;

@WebServlet("/crudFornecedor-update")
public final class UpdateFornecedorServlet extends HttpServlet{

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
            Fornecedor fornecedor = FornecedorService.exibirFornecedorParaUpdate(id);

            if (fornecedor == null || fornecedor.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
                session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
                response.sendRedirect(request.getContextPath()+"/crudFornecedor");
            } else {
                session.setAttribute("forecedorUpdate", fornecedor);
                session.setAttribute("abrirUpdate", true);
                response.sendRedirect(request.getContextPath()+"/crudFornecedor");
            }
        } catch (Exception e){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
            response.sendRedirect(request.getContextPath()+"/crudFornecedor");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();

            String idUpdate = request.getParameter("idUpdate");
            String idUsuarioUpdate = request.getParameter("idUsuarioUpdate");
            String tipoFornecedorUpdate = request.getParameter("tipoFornecedorUpdate");
            String cnpjUpdate = request.getParameter("cnpjUpdate");
            String razaoSocialUpdate = request.getParameter("razaoSocialUpdate");

            FornecedorParaInsertDTO fornecedorParaInsertDto = new FornecedorParaInsertDTO(idUpdate, idUsuarioUpdate, tipoFornecedorUpdate, cnpjUpdate, razaoSocialUpdate);
            List<GenericExceptionEnum> erros = FornecedorService.realizarUpdate(fornecedorParaInsertDto);

            if(!erros.isEmpty()){
                session.setAttribute("errosUpdate", erros);
                response.sendRedirect(request.getContextPath()+"/crudFornecedor");
            } else{
                //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
                Enumeration<String> attributes = session.getAttributeNames();
                while(attributes.hasMoreElements()){
                    session.removeAttribute(attributes.nextElement());
                }

                session.setAttribute("mensagemUpdate", "Os dados foram atualizados com sucesso");
                response.sendRedirect(request.getContextPath()+"/crudFornecedor");
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();

            List<GenericExceptionEnum> erros = new ArrayList<>();
            erros.add(ERRO_GENERICO);

            session.setAttribute("errosUpdate", erros);
            response.sendRedirect(request.getContextPath()+"/crudFornecedor");
        }
    }
}