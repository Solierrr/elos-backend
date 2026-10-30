<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tela de login</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/text.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/variables.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/login.css">
    <link rel="icon" type="image/svg" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg" >

</head>
<body>
<div class ="containerP">
    <header>
        <a href="${pageContext.request.contextPath}">
            <img src="${pageContext.request.contextPath}/assets/icone/Logo_com_nome.svg" alt="logo-elos" width="146" height="60">
        </a>
    </header>
    <div class="container">
        <div class="tela-login">
            <div class="home">
                <a href="${pageContext.request.contextPath}">
                    <img id="iconHome"src="${pageContext.request.contextPath}/assets/icone/home.svg" width="24" height="24">
                </a>
            </div>
            <p class="sub_title">Bem-vindo de volta!</p>
            <p class="title">Acesse a sua conta</p>
            <form id="formulario-login">
                <div class="form-group">
                    <div>
                        <input class="text-input" type="email" name="email"
                               placeholder="Coloque o seu e-mail aqui" value="" autocomplete="on"
                               required pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}">
                        <span id="erro-email" class="span-erro"></span>
                    </div>
                    <div>
                        <div id="conteiner-senha">
                            <input class="text-input" id="input-senha" type="password" name="senha"
                                   placeholder="Coloque sua senha" required pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}">
                            <img id="olho-senha"
                                 src="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                                 data-url-olho="${pageContext.request.contextPath}/assets/icone/olho.svg"
                                 data-url-oculto="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                                 alt="Mostrar senha">
                        </div>

                        <div id="informacoes-caixa-senha">
                            <span id="erro-senha" class="span-erro"></span>
                            <div class="direction">
                                <a href="${pageContext.request.contextPath}/redefinirSenha">
                                    <p class="little_title" style="text-decoration: underline;">Esqueceu
                                        a senha?</p>

                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="button-text-group">
                    <button class="sub_title" type="submit">Logar</button>
                    <p class="little_title">
                        <span class="color">Não tem conta?</span>
                        <a href="${pageContext.request.contextPath}/cadastro">
                            <span class="link-cadastro">Fazer cadastro</span>
                        </a>
                    </p>
                </div>
                <input type="hidden" name="navegador-e-sop">
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/login.js" defer></script>
<meta name="context-path" content="${pageContext.request.contextPath}/erro">

</body>
</html>
