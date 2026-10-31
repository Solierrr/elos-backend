package servlet.redefinirSenha;

import static exception.ErrosGerais.SUCESSO;

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
import java.util.HashMap;
import java.util.Map;
import service.redefinirSenha.RedefinicaoSenhaService;

@WebServlet("/redefinirSenha")
public final class EsqueciSenhaEmailServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final RedefinicaoSenhaService redefinicaoSenhaService = new RedefinicaoSenhaService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    RequestDispatcher dispatcher =
        request.getRequestDispatcher("/WEB-INF/view/redefinirSenha/telaEmail.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    Map<String, String> resposta = new HashMap<>();

    if (jsonObject.isEmpty()) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      resposta.put("email", "Nenhum e-mail foi inserido!");
      response.getWriter().write(gson.toJson(resposta));
      return;
    }

    String email = jsonObject.get("email").getAsString();

    GenericExceptionEnum resultado = redefinicaoSenhaService.enviarToken(email);

    if (resultado == SUCESSO) {
      HttpSession session = request.getSession(true);
      session.setAttribute("email", email.toLowerCase().strip());

      response.getWriter().write(request.getContextPath() + "/redefinirSenha-token");
      return;
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    resposta.put("email", resultado.exibirMensagem());

    response.getWriter().write(gson.toJson(resposta));
  }
}
