<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Redefinir a Senha</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg"
        type="image/svg+xml">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/codigoEmail.css">

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
      <div class="home">
        <a href="${pageContext.request.contextPath}">
          <img id="iconHome" src="${pageContext.request.contextPath}/assets/icone/home.svg"
               width="24" height="24">
        </a>
      </div>
      <p class="sub_title">Insira os dados!</p>
      <p class="title">Digite o código que foi enviado!</p>
      <form id="formulario-codigo" action="${pageContext.request.contextPath}/redefinirSenha-token" method="POST">
        <div class="form-group">
          <input class="text-input" type="text" inputmode="numeric" required id="n1" name="n1" autocomplete="off">
          <input class="text-input" type="text" inputmode="numeric" required id="n2" name="n2" autocomplete="off">
          <input class="text-input" type="text" inputmode="numeric" required id="n3" name="n3" autocomplete="off">
          <input class="text-input" type="text" inputmode="numeric" required id="n4" name="n4" autocomplete="off">
          <input class="text-input" type="text" inputmode="numeric" required id="n5" name="n5" autocomplete="off">
        </div>
        <span id="erro-codigo" class="span-erro"></span>
        <div class="button-text-group">
          <button id="botao" class="sub_title" type="submit">Proximo</button>
          <p class="little_title">
            <div class="sPPL">
              <span class="color">Não recebeu email?</span>
              <span id="reenviar-email" class="link-cadastro">Reenviar email</span>
              <input type="hidden" id="email">
            </div>
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
<script type="module" src="${pageContext.request.contextPath}/js/codigoEsqueciSenha.js"></script>
<meta name="context-path" content="${pageContext.request.contextPath}/erro">
</body>
</html>