package servlet.usuario;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Admin;
import service.usuario.UsuarioService;

@WebServlet("/crudUsuario-update")
public final class UpdateUsuarioServlet extends HttpServlet {

  private final Gson gson = new GsonBuilder().setFieldNamingPolicy(
      FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final UsuarioService usuarioService = new UsuarioService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    HttpSession session = request.getSession(true);
    Admin admin = (Admin) session.getAttribute("adm");
    if (admin == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String id = request.getParameter("id");

    List<Map<String, Object>> usuario = usuarioService.exibirUsuarioParaUpdate(id, admin);
    if (usuario.isEmpty()) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      response.getWriter().write("");
      return;
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().write(gson.toJson(usuario));
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    Admin admin = (Admin) session.getAttribute("adm");
    if (admin == null) {
      response.sendRedirect(request.getContextPath() + "/login");
    }

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    List<GenericExceptionEnum> mensagensInsert = usuarioService.realizarUpdate(jsonObject,
        admin.getId());
    if (!mensagensInsert.isEmpty()) {
      Map<String, String> mapaJson = new HashMap<>();
      for (GenericExceptionEnum genericExceptionEnum : mensagensInsert) {
        mapaJson.put(genericExceptionEnum.nomeCampoErro(), genericExceptionEnum.exibirMensagem());
      }

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(gson.toJson(mapaJson));
    } else {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{}");
    }
  }
}
