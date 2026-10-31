package servlet.cadastro;

import static exception.ErrosGeraisDados.VALIDACAO_OK;

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
import service.usuario.UsuarioService;

@WebServlet("/cadastro-tipo")
public final class CadastroTipoUsuarioServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final UsuarioService usuarioService = new UsuarioService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    HttpSession session = request.getSession(true);
    Object usuarioParaInsert = session.getAttribute("dadosParte1");

    if (usuarioParaInsert == null) {
      response.sendRedirect(request.getContextPath() + "/cadastro");
      return;
    }

    RequestDispatcher dispatcher =
        request.getRequestDispatcher("/WEB-INF/view/cadastro/telaSelecaoUsuario.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    HttpSession session = request.getSession(true);
    Object usuarioParaInsert = session.getAttribute("dadosParte1");

    if (usuarioParaInsert == null) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(request.getContextPath() + "/cadastro");
      return;
    }

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    String tipoUsuarioString = jsonObject.get("tipo-usuario").getAsString();

    GenericExceptionEnum tipoUsuarioValidacao =
        UsuarioService.validarTipoUsuario(tipoUsuarioString);
    if (tipoUsuarioValidacao != VALIDACAO_OK) {
      Map<String, Object> map = new HashMap<>();
      map.put("erro", tipoUsuarioValidacao);

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(gson.toJson(map));
      return;
    }

    session.setAttribute("tipoUsuario", tipoUsuarioString.toUpperCase().strip());

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(request.getContextPath() + "/cadastro-fim");
  }
}
