package servlet.cadastro;

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
import java.util.List;
import java.util.Map;
import service.usuario.UsuarioService;

@WebServlet("/cadastro-fim")
public final class CadastroDadosFinaisServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final UsuarioService usuarioService = new UsuarioService();

  private final Long idAdminQueRegistra = 5L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    HttpSession session = request.getSession(true);
    Object usuarioParaInser = session.getAttribute("dadosParte1");
    Object tipoUsuario = session.getAttribute("tipoUsuario");

    if (usuarioParaInser == null) {
      response.sendRedirect(request.getContextPath() + "/cadastro");
      return;
    }

    if (tipoUsuario == null) {
      response.sendRedirect(request.getContextPath() + "/cadastro-tipo");
      return;
    }

    String tipoUsuarioConvertido = (String) tipoUsuario;

    if (tipoUsuarioConvertido.equalsIgnoreCase("FORNECEDOR")) {
      RequestDispatcher dispatcher =
          request.getRequestDispatcher("/WEB-INF/view/cadastro/telaCadastroFornecedor.jsp");
      dispatcher.forward(request, response);
    } else if (tipoUsuarioConvertido.equalsIgnoreCase("EMPRESA_DEMANDANTE")) {
      RequestDispatcher dispatcher =
          request.getRequestDispatcher("/WEB-INF/view/cadastro/telaCadastroDemandante.jsp");
      dispatcher.forward(request, response);
    } else {
      RequestDispatcher dispatcher =
          request.getRequestDispatcher("/WEB-INF/view/cadastro/telaCadastroProfissional.jsp");
      dispatcher.forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    HttpSession session = request.getSession(true);
    Object usuarioParaInser = session.getAttribute("dadosParte1");
    Object tipoUsuario = session.getAttribute("tipoUsuario");

    JsonObject jsonObjectUsuario = (JsonObject) usuarioParaInser;
    String tipoUsuarioConvertido = (String) tipoUsuario;

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
    jsonObject.entrySet().forEach(entry -> jsonObjectUsuario.add(entry.getKey(), entry.getValue()));
    jsonObjectUsuario.addProperty("tipo-usuario", tipoUsuarioConvertido);

    if(!jsonObject.has("nome")){
      jsonObjectUsuario.add("nome", jsonObject.get("razao-social"));
    }

    List<GenericExceptionEnum> mensagensInsert =
        usuarioService.realizarInsert(jsonObjectUsuario, idAdminQueRegistra);

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
      response.getWriter().write(request.getContextPath() + "/login");
    }
  }
}
