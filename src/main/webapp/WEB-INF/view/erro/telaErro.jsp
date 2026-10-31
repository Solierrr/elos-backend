<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Erro</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg"
        type="image/svg+xml">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/erro.css">
</head>
<body>
<header>
  <img src="${pageContext.request.contextPath}/assets/icone/Logo_com_nome_laranja.svg"
       alt="logo-elos" width="146" height="60">
</header>
<div class="containerP">
  <div class="container">
    <div class="containerTB">
      <p class="big_text">Oops...</p>
      <p class="title">Parece que algo deu <span class="destaque">errado!</span></p>
      <p class="sub">A página que você está procurando não existe ou foi movida para outro endereço.
        Verifique o link ou volte para a página anterior para continuar navegando.</p>
      <button class="sub_title" id="voltar">Voltar</button>
    </div>
    <img src="${pageContext.request.contextPath}/assets/img/Leao_dormindo_erro.png" width="1092"
         height="566" alt="Leão dormindo">
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/erro.js" defer></script>

</body>
</html>