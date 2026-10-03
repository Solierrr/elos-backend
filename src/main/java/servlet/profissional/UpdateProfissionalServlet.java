package servlet.profissional;

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

import model.Profissional;
import service.profissional.ProfissionalParaInsertDTO;
import service.profissional.ProfissionalService;
import exception.GenericExceptionEnum;

@WebServlet("/crudProfissional-update")
public final class UpdateProfissionalServlet extends HttpServlet{

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
            Profissional profissional = ProfissionalService.exibirProfissionalParaUpdate(id);

            if (profissional == null || profissional.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
                session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
                response.sendRedirect(request.getContextPath()+"/crudProfissional");
            } else {
                session.setAttribute("profissionalUpdate", profissional);
                session.setAttribute("abrirUpdate", true);
                response.sendRedirect(request.getContextPath()+"/crudProfissional");
            }
        } catch (Exception e){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
            response.sendRedirect(request.getContextPath()+"/crudProfissional");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();

            String idUpdate = request.getParameter("idUpdate");
            String idUsuarioUpdate = request.getParameter("idUsuarioUpdate");
            String profissaoUpdate = request.getParameter("profissaoUpdate");
            String cpfUpdate = request.getParameter("cpfUpdate");
            String idFornecedorUpdate = request.getParameter("idFornecedorUpdate");

            ProfissionalParaInsertDTO profissionalParaInsertDto = new ProfissionalParaInsertDTO(idUpdate, idUsuarioUpdate, profissaoUpdate, cpfUpdate, idFornecedorUpdate);
            List<GenericExceptionEnum> erros = ProfissionalService.realizarUpdate(profissionalParaInsertDto);

            if(!erros.isEmpty()){
                session.setAttribute("errosUpdate", erros);
                response.sendRedirect(request.getContextPath()+"/crudProfissional");
            } else{
                //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
                Enumeration<String> attributes = session.getAttributeNames();
                while(attributes.hasMoreElements()){
                    session.removeAttribute(attributes.nextElement());
                }

                session.setAttribute("mensagemUpdate", "Os dados foram atualizados com sucesso");
                response.sendRedirect(request.getContextPath()+"/crudProfissional");
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();

            List<GenericExceptionEnum> erros = new ArrayList<>();
            erros.add(ERRO_GENERICO);

            session.setAttribute("errosUpdate", erros);
            response.sendRedirect(request.getContextPath()+"/crudProfissional");
        }
    }
}
