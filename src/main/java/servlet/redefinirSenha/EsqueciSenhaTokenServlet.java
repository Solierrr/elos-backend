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

@WebServlet("/redefinirSenha-token")
public final class EsqueciSenhaTokenServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final RedefinicaoSenhaService redefinicaoSenhaService = new RedefinicaoSenhaService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    RequestDispatcher dispatcher =
        request.getRequestDispatcher("/WEB-INF/view/redefinirSenha/telaCodigo.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    Map<String, String> resposta = new HashMap<>();

    HttpSession session = request.getSession(false);

    String email = (String) session.getAttribute("email");

    if (jsonObject.isEmpty()) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      resposta.put("token", "Insira o código que você recebeu via e-mail!");
      response.getWriter().write(gson.toJson(resposta));
      return;
    }

    if (jsonObject.has("reenviar")) {

      GenericExceptionEnum resultado = redefinicaoSenhaService.enviarToken(email);

      if (resultado == SUCESSO) {
        resposta.put("codigo", "O token foi enviado novamente com sucesso!");
      } else {
        resposta.put("codigo", resultado.exibirMensagem());
      }
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      response.getWriter().write(gson.toJson(resposta));
      return;
    }

    String token = jsonObject.get("token").getAsString();

    GenericExceptionEnum resultado = redefinicaoSenhaService.verificarToken(token, email);

    if (resultado == SUCESSO) {
      session.setAttribute("tokenCerto", true);

      response.getWriter().write(request.getContextPath() + "/redefinirSenha-senha");

      return;
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    resposta.put("token", resultado.exibirMensagem());
    response.getWriter().write(gson.toJson(resposta));
  }
}
