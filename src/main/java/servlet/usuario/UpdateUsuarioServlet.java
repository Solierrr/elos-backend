package servlet.usuario;

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
import model.Usuario;
import service.usuario.UsuarioParaInsertDTO;
import service.usuario.UsuarioService;

@WebServlet("/crudUsuario-update")
public final class UpdateUsuarioServlet extends HttpServlet{

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

            String idUpdate = request.getParameter("idUpdate");
            Usuario usuario = UsuarioService.exibirUsuarioParaUpdate(idUpdate);

            if (usuario == null || usuario.getId() == REGISTRO_NAO_ENCONTRADO.getCodigo()){
                session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
                response.sendRedirect(request.getContextPath()+"/crudUsuario");
            } else {
                session.setAttribute("usuarioUpdate", usuario);
                session.setAttribute("abrirUpdate", true);
                response.sendRedirect(request.getContextPath()+"/crudUsuario");
            }
        } catch (Exception e){
            HttpSession session = request.getSession();

            //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
            Enumeration<String> attributes = session.getAttributeNames();
            while(attributes.hasMoreElements()){
                session.removeAttribute(attributes.nextElement());
            }

            session.setAttribute("erroUpdate", "Um erro inesperado aconteceu, tente novamente");
            response.sendRedirect(request.getContextPath()+"/crudUsuario");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();

            String idUpdate = request.getParameter("idUpdate");
            String tipoUsuarioUpdate = request.getParameter("tipoUsuarioUpdate");
            String emailUpdate = request.getParameter("emailUpdate");
            String senhaUpdate = request.getParameter("senhaUpdate");
            String nomeUpdate = request.getParameter("nomeUpdate");
            String raioProcuraKmUpdate = request.getParameter("raioProcuraKmUpdate");
            UsuarioParaInsertDTO usuarioParaInsertDto = new UsuarioParaInsertDTO(tipoUsuarioUpdate, emailUpdate, senhaUpdate, nomeUpdate, raioProcuraKmUpdate);
            List<GenericExceptionEnum> erros = UsuarioService.realizarUpdate(usuarioParaInsertDto);

            if(!erros.isEmpty()){
                session.setAttribute("errosUpdate", erros);
                response.sendRedirect(request.getContextPath()+"/crudUsuario");
            } else{
                //Limpeza dos atributos da seção, para evitar casos dos pop-ups abrirem quando não deveriam
                Enumeration<String> attributes = session.getAttributeNames();
                while(attributes.hasMoreElements()){
                    session.removeAttribute(attributes.nextElement());
                }

                session.setAttribute("mensagemUpdate", "Os dados foram atualizados com sucesso");
                response.sendRedirect(request.getContextPath()+"/crudUsuario");
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();

            ArrayList<GenericExceptionEnum> erros = new ArrayList<>();
            erros.add(ERRO_GENERICO);

            session.setAttribute("errosUpdate", erros);
            response.sendRedirect(request.getContextPath()+"/crudUsuario");
        }
    }
}
