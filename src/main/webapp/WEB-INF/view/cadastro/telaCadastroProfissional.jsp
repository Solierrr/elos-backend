<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cadastro profissional</title>

  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/style/cadastroProfissional.css">
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
      <p class="title">Olá, pronto para começar?</p>
      <form id="formulario-cadastro" method="POST"
            action="${pageContext.request.contextPath}/cadastro-fim">
        <div class="form-group">
          <div>
            <input class="text-input" type="text" placeholder="Coloque seu nome" id="nome"
                   name="nome" required>
            <span id="erro-nome" class="span-erro"></span>
          </div>
          <div>
            <input class="text-input" type="text" placeholder="Coloque o seu CPF" id="cpf"
                   name="cpf" required>
            <span id="erro-cpf" class="span-erro"></span>
          </div>
          <div>
            <input class="text-input" type="text" placeholder="Coloque a sua profissão"
                   id="profissao" name="profissao" required>
            <span id="erro-profissao" class="span-erro"></span>
          </div>
          <div class="checkbox-group">
            <input class="checkbox" type="checkbox" required>
            <p class="checkboxT">Concordo com os termos de privacidade</p>
          </div>
        </div>
        <div class="button-text-group">
          <button class="sub_title" type="submit">Finalizar</button>
          <div class="sPPL">
            <span class="color">Clicou errado?</span>
            <a href="${pageContext.request.contextPath}/login" class="link-cadastro">Voltar</a>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<meta name="context-path" content="${pageContext.request.contextPath}/erro">
<script src="${pageContext.request.contextPath}/js/telaCadastroFim.js"></script>

</body>
</html>