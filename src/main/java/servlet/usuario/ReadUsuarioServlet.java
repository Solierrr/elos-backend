package servlet.usuario;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import exception.GenericExceptionEnum;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Admin;
import service.usuario.FiltrosUsuarioDTO;
import service.usuario.UsuarioService;

@WebServlet("/crudUsuario")
public final class ReadUsuarioServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Faz com que o navegador não salve a página no cache garantindo proteção dos dados privados do crud
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        HttpSession httpSession = request.getSession();
        Admin admin = (Admin) httpSession.getAttribute("adm");

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/usuario/crudUsuario.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(true);
        Admin admin = (Admin) session.getAttribute("adm");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setCharacterEncoding("UTF-8");

        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        Map<String, Object> json = new HashMap<>();
        if (jsonObject.isEmpty()) {
            json.put("valores", usuarioService.realizarSelectInicial(admin));

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(json));
            return;
        }

        List<GenericExceptionEnum> erros = new ArrayList<>();

        List<Map<String, Object>> usuario = usuarioService.realizarSelect(gson.fromJson(jsonObject, FiltrosUsuarioDTO.class), erros, admin);
        if (erros.isEmpty()) {
            if (!usuario.isEmpty())
                json.put("valores", usuario);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(gson.toJson(json));
            return;
        }

        for (GenericExceptionEnum genericExceptionEnum : erros) {
            json.put(genericExceptionEnum.nomeCampoErro(), genericExceptionEnum.exibirMensagem());
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(json));
    }
}
