package servlet.usuario;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.GenericExceptionEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.FuncoesAdmin;
import service.usuario.UsuarioService;

@WebServlet("/crudUsuario-insert")
public final class InsertUsuarioServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()+"/crudUsuario");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        HttpSession session = request.getSession();

        FuncoesAdmin funcaoAdm = (FuncoesAdmin) session.getAttribute("funcaoAdm");

        request.setCharacterEncoding("UTF-8");
        List<GenericExceptionEnum> mensagensInsert = UsuarioService.realizarInsert(gson.fromJson(request.getReader(), JsonObject.class));

        if(!mensagensInsert.isEmpty()){
            Map<String, String> mapaJson = new HashMap<>();
            for (GenericExceptionEnum genericExceptionEnum : mensagensInsert) {
                mapaJson.put(genericExceptionEnum.nomeCampoErro(), genericExceptionEnum.exibirMensagem());
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(mapaJson));
        } else{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{}");
        }
    }
}
