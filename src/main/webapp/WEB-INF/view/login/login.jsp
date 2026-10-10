<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tela de login</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/text.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="icon" type="image/svg" href="${pageContext.request.contextPath}/asset/icone/LOGO.svg">

</head>
<body>
<div class="containerP">
    <header>
        <img src="${pageContext.request.contextPath}/asset/icone/Logo_com_nome.svg" alt="logo-elos">
    </header>
    <div class="container">
        <div class="tela-login">
            <p class="subTitle">Bem-vindo de volta!</p>
            <p class="title">Acesse a sua conta</p>
            <form id="formulario-login" method="POST"
                  action="${pageContext.request.contextPath}/login">
                <div class="form-group">
                    <div>
                        <input class="text-input" type="email" name="email"
                               placeholder="Coloque o seu e-mail aqui" value="" autocomplete="on"
                               required>
                        <span id="erro-email" class="span-erro"></span>
                    </div>
                    <div>
                        <div id="conteiner-senha">
                            <input class="text-input" id="input-senha" type="password" name="senha"
                                   placeholder="Coloque sua senha" required>
                            <img id="olho-senha"
                                 src="${pageContext.request.contextPath}/asset/icone/olho-oculto.svg"
                                 data-url-olho="${pageContext.request.contextPath}/asset/icone/olho.svg"
                                 data-url-oculto="${pageContext.request.contextPath}/asset/icone/olho-oculto.svg"
                                 alt="Mostrar senha">
                        </div>

                        <div id="informacoes-caixa-senha">
                            <span id="erro-senha" class="span-erro"></span>
                            <div class="direction">
                                <p class="little-title" style="text-decoration: underline;">Esqueceu
                                    a senha?</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="button-text-group">
                    <button class="subTitle" type="submit">Logar</button>
                    <p class="little-title">
                        <span class="color">Não tem conta?</span>
                        <span class="link-cadastro">Fazer cadastro</span>
                    </p>
                </div>
                <input type="hidden" name="navegador-e-sop">
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/login.js" defer></script>
</body>
</html>