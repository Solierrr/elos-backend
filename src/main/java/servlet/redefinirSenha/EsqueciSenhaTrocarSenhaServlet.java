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

@WebServlet("/redefinirSenha-senha")
public class EsqueciSenhaTrocarSenhaServlet extends HttpServlet {


  // Constantes utilizadas pelo código
  private final Gson gson =
          new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final RedefinicaoSenhaService redefinicaoSenhaService = new RedefinicaoSenhaService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session =  request.getSession(true);

    Object tokenCerto = session.getAttribute("tokenCerto");

    if(tokenCerto == null){
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    RequestDispatcher dispatcher =
            request.getRequestDispatcher("/WEB-INF/view/redefinirSenha/trocarSenha.jsp");
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    Map<String, String> resposta = new HashMap<>();

    HttpSession session =  request.getSession(false);

    String email = (String) session.getAttribute("email");

    if(jsonObject.isEmpty()){
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      resposta.put("senha", "Insira uma senha!");
      response.getWriter().write(gson.toJson(resposta));
      return;
    }

    String senha = jsonObject.get("senha").getAsString();
    String senhaConfirmacao = jsonObject.get("senha-confirmacao").getAsString();

    GenericExceptionEnum resultado = redefinicaoSenhaService.alterarSenha(email, senha, senhaConfirmacao);

    if(resultado == SUCESSO){
      response.getWriter().write(request.getContextPath() + "/login");

      return;
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    resposta.put(resultado.nomeCampoErro(), resultado.exibirMensagem());
    response.getWriter().write(gson.toJson(resposta));

  }

}

