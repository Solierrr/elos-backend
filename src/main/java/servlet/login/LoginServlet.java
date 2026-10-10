package servlet.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Admin;
import service.login.LoginService;

@WebServlet("/login")
public final class LoginServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Fazemos isso para garantir que o usuario depois de sair da tela do crud não consiga voltar pelas setinhas do navegador
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        HttpSession sessionAtual = request.getSession(false);
        sessionAtual.invalidate();
        request.getRequestDispatcher("/WEB-INF/view/login/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = request.getHeader("CF-Connecting-IP");
        if (ip == null) ip = request.getRemoteAddr();

        request.setCharacterEncoding("UTF-8");

        JsonObject dadosFormulario;
        try {
            dadosFormulario = gson.fromJson(request.getReader(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (dadosFormulario == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        LoginService loginService = new LoginService();

        Admin admin = loginService.realizarLogin(
                dadosFormulario.has("email") && !dadosFormulario.get("email").isJsonNull() ? dadosFormulario.get("email").getAsString() : null,
                dadosFormulario.has("senha") && !dadosFormulario.get("senha").isJsonNull() ? dadosFormulario.get("senha").getAsString() : null,
                dadosFormulario.has("userAgent") && !dadosFormulario.get("userAgent").isJsonNull() ? dadosFormulario.get("userAgent").getAsString() : null,
                ip);

        System.out.println("Ip conectado: "+ip);

        if (admin == null) {
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("adm", null);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("adm", admin);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(request.getContextPath() + "/crudUsuario");
    }
}
