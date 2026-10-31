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
import service.usuario.UsuarioCadastroDTO;
import service.usuario.UsuarioService;

@WebServlet("/cadastro")
public final class CadastroInicioServlet extends HttpServlet {

  // Constantes utilizadas pelo código
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

  private final UsuarioService usuarioService = new UsuarioService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    RequestDispatcher dispatcher =
        request.getRequestDispatcher("/WEB-INF/view/cadastro/telaCadastro.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    request.setCharacterEncoding("UTF-8");
    JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);

    UsuarioCadastroDTO usuarioCadastroDTO = gson.fromJson(jsonObject, UsuarioCadastroDTO.class);

    List<GenericExceptionEnum> listaErros = usuarioService.validarCadastro(usuarioCadastroDTO);

    if (listaErros.isEmpty()) {
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(request.getContextPath() + "/cadastro-tipo");

      HttpSession session = request.getSession(true);
      session.setAttribute("dadosParte1", jsonObject);

      return;
    }

    Map<String, String> mapaJson = new HashMap<>();
    for (GenericExceptionEnum genericExceptionEnum : listaErros) {
      mapaJson.put(genericExceptionEnum.nomeCampoErro(), genericExceptionEnum.exibirMensagem());
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(gson.toJson(mapaJson));
  }
}
