<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Seleção Usuário</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/style/selecaoUsuario.css">
  <link rel="icon" type="image/svg" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg">

  <style>

    .imagem {
      background-image: url("${pageContext.request.contextPath}/assets/img/imagem-contribuicao.png");
    }

    .imagem1 {
      background-image: url("${pageContext.request.contextPath}/assets/img/imagem-contribuicao2.png");
    }

    .imagem2 {
      background-image: url("${pageContext.request.contextPath}/assets/img/imagem-contribuicao3.png");
    }

  </style>

</head>
<body>
<div class="containerP">
  <header>
    <a href="${pageContext.request.contextPath}/">
      <img src="${pageContext.request.contextPath}/assets/icone/Logo_com_nome_laranja.svg"
           alt="logo-elos" width="146" height="60">
    </a>
    <a href="${pageContext.request.contextPath}/">
      <img id="iconHome" src="${pageContext.request.contextPath}/assets/icone/home.svg" width="24"
           height="24">
    </a>
  </header>
  <div class="bb">
    <p class="big_text">O que você é</p>
    <p class="title">Qual será sua contruibuição para o ecossistema?</p>
    <div class="sPPL">
      <span class="color">Clicou errado?</span>
      <a href="${pageContext.request.contextPath}/login" class="link-cadastro">Voltar</a>
    </div>
  </div>
  <div class="cards" id="conteiner-cards">
    <div class="container" id="EMPRESA_DEMANDANTE">
      <div class="imagem"></div>
      <div class="card-content">
        <p class="sub_title">Demandante</p>
        <p class="little_text">Tenha acesso a todo o acervo dos fornecedores cadastrados no site do
          Elos!</p>
        <p class="little_text">Destinado somente para empresas.</p>
      </div>
    </div>

    <div class="container" id="FORNECEDOR">
      <div class="imagem1"></div>
      <div class="card-content">
        <p class="sub_title">Fornecedor</p>
        <p class="little_text">Ofereça seus produtos e serviços solares além de ter acesso aos
          outros produtos!</p>
        <p class="little_text">Destinado somente para empresas.</p>
      </div>
    </div>

    <div class="container" id="PROFISSIONAL">
      <div class="imagem2"></div>
      <div class="card-content">
        <p class="sub_title">Profissional</p>
        <p class="little_text">Realize serviços para nossos clientes e procure novos empregos nas
          empresas fornecedoras cadastradas!</p>
        <p class="little_text">Destinado somente para pessoas fisicas.</p>
      </div>
    </div>
  </div>
  <div class="button-text-group">
    <form method="POST" id="formulario-selecao"
          action="${pageContext.request.contextPath}/cadastro-tipo">
      <button class="sub_title" type="submit">Continuar</button>

    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/telaSelecao.js" defer></script>
<meta name="context-path" content="${pageContext.request.contextPath}/erro">


</body>
</html>
