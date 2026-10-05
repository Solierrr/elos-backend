package servlet.login;

import jakarta.servlet.RequestDispatcher;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/login/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ip = request.getHeader("CF-Connecting-IP");
        if (ip == null) ip = request.getRemoteAddr();

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String userAgent = request.getParameter("navegador-e-sop");

        LoginService loginService = new LoginService();

        Admin admin = loginService.realizarLogin(email, senha, userAgent, ip);

        System.out.println(userAgent);

        if(admin == null){
            System.out.println("deu erro");
            request.setAttribute("erro", "Senha incorreta");
            request.setAttribute("erroCaixa", "border-color: #D9021E");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/view/login/login.jsp").forward(request, response);
        }else {

            System.out.println(admin);

            HttpSession httpSession = request.getSession(false);

            httpSession.setAttribute("idAdm", admin.getId());
            httpSession.setAttribute("funcao", admin.getFuncao());
        }
    }
}
