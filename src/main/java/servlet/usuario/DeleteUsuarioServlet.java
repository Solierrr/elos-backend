package servlet.usuario;

import static exception.ErrosGerais.SUCESSO;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import exception.GenericExceptionEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Admin;
import service.usuario.UsuarioService;

@WebServlet("/crudUsuario-delete")
public final class DeleteUsuarioServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
        .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/crudUsuario");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("chegou no servlet");

        HttpSession session = request.getSession();

        Admin admin = (Admin) session.getAttribute("adm");

        if (admin == null)
            response.sendRedirect(request.getContextPath() + "/login");

        request.setCharacterEncoding("UTF-8");

        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        System.out.println(jsonObject.toString());

        GenericExceptionEnum resultado = new UsuarioService().realizarDelete(jsonObject.get("id").getAsString(), admin.getId());

        if (resultado == SUCESSO) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("");
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{}");
        }

    }
}