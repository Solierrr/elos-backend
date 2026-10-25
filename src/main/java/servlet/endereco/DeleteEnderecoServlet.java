package servlet.endereco;

import static exception.ErrosGerais.SUCESSO;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import exception.GenericExceptionEnum;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Admin;
import service.endereco.EnderecoService;

@WebServlet("/crudEndereco-delete")
public class DeleteEnderecoServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final EnderecoService enderecoService = new EnderecoService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect(request.getContextPath() + "/crudEndereco");
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

    GenericExceptionEnum resultado =
        enderecoService.realizarDelete(jsonObject.get("id").getAsString(), admin.getId());
    if (resultado == SUCESSO) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{}");
    } else {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"erro\": true}");
    }
  }
}
