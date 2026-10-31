<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Redefinir a senha</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg"
        type="image/svg+xml">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/redefinirSenha.css">

</head>
<body>
<div class="containerP">
  <header>
    <img src="${pageContext.request.contextPath}/assets/icone/Logo_com_nome.svg" alt="logo-elos"
         width="146" height="60">
  </header>
  <div class="container">
    <div class="tela-login">
      <div class="home">
        <img src="${pageContext.request.contextPath}/assets/icone/home.svg" width="24" height="24">
      </div>
      <p class="sub_title">Insira os dados!</p>
      <p class="title">Redefina sua senha</p>
      <form action="${pageContext.request.contextPath}/redefinirSenha-senha" method="POST"
            id="formulario-senha">
        <div class="form-group">
          <div>
            <div id="conteiner-senha">
              <input class="text-input" id="input-senha" type="password" name="senha"
                     placeholder="Coloque sua nova senha" required
                     pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}">
              <img id="olho-senha"
                   src="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                   data-url-olho="${pageContext.request.contextPath}/assets/icone/olho.svg"
                   data-url-oculto="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                   alt="Mostrar senha">
            </div>
            <span id="erro-senha" class="span-erro"></span>
          </div>
          <div>
            <div id="conteiner-senha-confirmacao">
              <input class="text-input" id="input-senha-confirmacao" type="password"
                     name="senha-confirmacao"
                     placeholder="Confirme sua senha" required
                     pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}">
              <img id="olho-senha-confirmacao"
                   src="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                   data-url-olho="${pageContext.request.contextPath}/assets/icone/olho.svg"
                   data-url-oculto="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                   alt="Mostrar senha">
            </div>
            <span id="erro-senha-confirmacao" class="span-erro"></span>
          </div>
        </div>
        <div class="button-text-group">
          <button class="sub_title" type="submit">Confirmar</button>
          <p class="little-title">
            <span class="color">Clicou errado?</span>
            <a href="${pageContext.request.contextPath}/login" class="link-cadastro">Voltar</a>
          </p>
        </div>
      </form>
    </div>
  </div>
</div>
</div>

<script src="${pageContext.request.contextPath}/js/novaSenha.js" defer></script>
<meta name="context-path" content="${pageContext.request.contextPath}/erro">

</body>
</html>