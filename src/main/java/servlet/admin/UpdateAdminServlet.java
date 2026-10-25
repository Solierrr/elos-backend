package servlet.admin;

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
import service.admin.AdminService;

@WebServlet("/crudAdmin-update")
public final class UpdateAdminServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final AdminService adminService = new AdminService();

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

    List<Map<String, Object>> adminLido = adminService.exibirAdminParaUpdate(id, admin);
    if (adminLido.isEmpty()) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      response.getWriter().write("");
      return;
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().write(gson.toJson(adminLido));
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    Admin admin = (Admin) session.getAttribute("adm");
    if (admin == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    List<GenericExceptionEnum> mensagensInsert =
        adminService.realizarUpdate(jsonObject, admin.getId());
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
