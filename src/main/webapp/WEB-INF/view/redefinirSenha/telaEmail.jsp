<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Reedefinir a senha</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg"
        type="image/svg+xml">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/colocarEmail.css">

</head>
<body>
<div class="containerP">
  <header>
    <a href="${pageContext.request.contextPath}">
      <img src="${pageContext.request.contextPath}/assets/icone/Logo_com_nome.svg" alt="logo-elos"
           width="146" height="60">
    </a>
  </header>
  <div class="container">
    <div class="tela-redefinirSenha">
      <div>
        <div class="home">
          <a href="${pageContext.request.contextPath}">
            <img id="iconHome" src="${pageContext.request.contextPath}/assets/icone/home.svg" width="24" height="24">
          </a>
        </div>
        <p class="sub_title">Insira os dados!</p>
        <p class="title">Esqueceu sua senha?</p>
        <form id="formulario-email" action="${pageContext.request.contextPath}/redefinirSenha" method="POST">
          <div class="form-group">
            <input class="text-input" name="email" id="email" type="text"
                   placeholder="Coloque o seu e-mail aqui" required>
            <span id="erro-email" class="span-erro"></span>
          </div>
          <div class="button-text-group">
            <button id="botao-enviar" class="sub_title" type="submit">Proximo</button>

            <p class="little_title">
            <div class="sPPL">
              <span class="color">Clicou errado?</span>
              <a href="${pageContext.request.contextPath}/login" class="link-cadastro">Voltar</a>
            </div>
            </p>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<script src="${pageContext.request.contextPath}/js/redefinirSenhaEmail.js" defer></script>
<meta name="context-path" content="${pageContext.request.contextPath}/erro">

</body>
</html>
