<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tela de cadastro</title>

  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" type="text/css" href="style/cadastro.css">
  <link rel="icon" type="image/svg" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg">

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
    <div class="tela-login">
      <div class="initial-text">
        <p class="sub_title">Insira os dados!</p>
        <div class="home">
          <a href="${pageContext.request.contextPath}">
            <img id="iconHome" src="${pageContext.request.contextPath}/assets/icone/home.svg"
                 width="24" height="24">
          </a>
        </div>
      </div>
      <p class="title">Queremos te ver mais vezes!</p>
      <form id="formulario-cadastro" action="${pageContext.request.contextPath}/cadastro"
            method="post">
        <div class="form-group">
          <div>
            <input class="text-input" type="email" id="email" name="email"
                   placeholder="Coloque o seu e-mail aqui" required>
            <span id="erro-email" class="span-erro"></span>
          </div>
          <div>
            <div id="conteiner-senha">
              <input class="text-input" id="senha" type="password" name="senha"
                     placeholder="Coloque sua senha" required
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
              <input class="text-input" id="senha-confirmacao" type="password"
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
          <div>
            <input class="text-input" type="text" placeholder="Coloque seu raio de procura (KM)"
                   required name="raio-procura-km">
            <span id="erro-raio-procura-km" class="span-erro"></span>
          </div>
        </div>
        <div class="button-text-group">
          <button class="sub_title" type="submit">Continuar</button>
        </div>
      </form>
    </div>
  </div>
</div>

<meta name="context-path" content="${pageContext.request.contextPath}/erro">
<script src="${pageContext.request.contextPath}/js/telaCadastro.js"></script>

</body>
</html>