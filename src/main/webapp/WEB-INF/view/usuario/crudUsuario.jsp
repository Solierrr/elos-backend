<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>CRUD Usuário</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/landing.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/crud.css" />
    <link rel="icon" type="image/svg" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg" >

</head>
<body>
<div class="conteiner">
    <nav class="side-bar">
        <div class="conteiner-logo">
            <a>
                <img src="${pageContext.request.contextPath}/assets/icone/LOGO.svg" id="logo" alt="botão para a tela inicial"/>
            </a>
            <div class="traco-abaixo-logo"></div>
        </div>

        <div class="caixa-sidebar">
            <img src="${pageContext.request.contextPath}/assets/icone/crud/abrir_sidebar.svg" alt="abrir side bar" id="botao-abrir-sidebar">
        </div>

        <div id="botoes-navegacao" class="caixa-sidebar">
            <a class="opcao-side-bar-selecionada">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/usuario.svg"  alt="Ir para tela do CRUD do usuário"/>
            </a>
            <a>
                <img src="${pageContext.request.contextPath}/assets/icone/crud/empresa_demandante.svg" alt="Ir para tela do CRUD da empresa demandante"/>
            </a>
            <a>
                <img src="${pageContext.request.contextPath}/assets/icone/crud/fornecedor.svg" alt="Ir para tela do CRUD do fornecedor"/>
            </a>
            <a href="">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/profissional.svg" alt="Ir para tela do CRUD do profissional"/>
            </a>
        </div>

        <div id="botoes-localizacao" class="caixa-sidebar">
            <a>
                <img src="${pageContext.request.contextPath}/assets/icone/crud/telefone.svg" alt="Ir para tela do CRUD do telefone"/>
            </a>
            <a>
                <img src="${pageContext.request.contextPath}/assets/icone/crud/endereco.svg" alt="Ir para tela do CRUD do endereço"/>
            </a>
        </div>

        <div id="botoes-admin" class="caixa-sidebar">
            <a href="">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/admin.svg" alt="Ir para tela do CRUD do admin">
            </a>
            <a href="">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/telefone.svg" alt="Ir para tela do CRUD do telefone do admin">
            </a>
            <a href="">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/historico_altecacoes.svg" alt="Ir para tela do CRUD do histórico de alterações">
            </a>
            <a href="">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/acesso admin.svg" alt="Ir para tela do CRUD do histórico de acessos dos admins">
            </a>
        </div>

        <div class="caixa-sidebar">
            <a href="">
                <img src="${pageContext.request.contextPath}/assets/icone/crud/sair.svg" alt="Deslogar do CRUD">
            </a>
        </div>
    </nav>

    <div id="conteiner-do-conteudo">
        <div id="conteiner-botoes">
            <button class="botoes-brancos botoes-pop-up-superiores" id="filtro-entrar">Filtrar</button>
            <button id="insert-entrar" class="botoes-laranjas botoes-pop-up-superiores ">Adicionar registro</button>
        </div>

        <div id="caixa-tabela">
            <table>
                <tr>
                    <th class="littleText">ID</th>
                    <th class="littleText">Email</th>
                    <th class="littleText">Nome</th>
                    <th class="littleText">Tipo do Usuario</th>
                    <th class="littleText">Raio de Procura em km</th>
                    <th class="littleText">Ações</th>
                </tr>
                <tr>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                </tr>
                <tr>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                    <td>a</td>
                </tr>

            </table>
        </div>

        <dialog class="pop-up" id="pop-up-insert" >
            <form id="formulario-insert" method="POST" action="${pageContext.request.contextPath}/crudUsuario-insert">
                <div id="conteiner-superior-insert" class="conteiner-superior-pop-up">
                    <button type="submit" name="enviar-insert" id="enviar-insert" class="botoes-insert-laranjas">Adicionar usuário</button>
                    <button id="insert-sair" class="botoes-sair"><img src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg" alt="" class="icone-sair"></button>
                </div>

                <div id="conteudo-insert">
                    <div>
                        <div class="label-e-input">
                            <label for="email">E-mail</label>
                            <input type="email" name="email" id="email" required placeholder="ex: meu.email@gmail.com">
                        </div>
                        <span id="erro-email" class="span-erro"></span>
                    </div>

                    <div>
                        <div class="label-e-input">
                            <label for="senha">Senha</label>
                            <div id="senha-container">


                                <input class="text-input" id="senha" type="password" name="senha"
                                       placeholder="Coloque a senha do usuário" required pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}">
                                <img id="olho-senha"
                                     src="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                                     data-url-olho="${pageContext.request.contextPath}/assets/icone/olho.svg"
                                     data-url-oculto="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                                     alt="Mostrar senha">
                            </div>

                        </div>
                        <span id="erro-senha" class="span-erro"></span>
                    </div>

                    <div>
                        <div class="label-e-input">
                            <label for="nome">Nome</label>
                            <input type="text" name="nome" id="nome" required placeholder="ex: Roger dos santos">
                        </div>
                        <span id="erro-nome" class="span-erro"></span>
                    </div>

                    <div>
                        <div class="label-e-input">
                            <label for="raio-procura-km">Raio de procura (KM)</label>
                            <input type="text" placeholder="ex: 10" name="raio-procura-km" id="raio-procura-km" required>
                        </div>
                        <span id="erro-raio-procura-km" class="span-erro"></span>
                    </div>

                    <div>
                        <div class="label-e-input">
                            <label for="tipo-usuario">Tipo do usuário</label>
                            <select name="tipo-usuario" id="tipo-usuario" required>
                                <option value="" disabled selected hidden>Selecione o tipo de usuário</option>
                                <option value="EMPRESA_DEMANDANTE">Empresa demandante</option>
                                <option value="FORNECEDOR">Fornecedor</option>
                                <option value="PROFISSIONAL">Profissional</option>
                            </select>
                        </div>
                        <span id="erro-tipo-usuario" class="span-erro"></span>
                    </div>

                    <div id="dados-mutaveis">
                    </div>

                </div>
            </form>
        </dialog>

        <dialog class="pop-up" id="pop-up-filtro">
            <div id="conteiner-filtro" class="conteiner-pop-up">

                <div id="conteiner-sair-filtro" class="conteiner-superior-pop-up">
                    <div id="botoes-filtro">
                        <select name="campo-filtro" id="campo-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden id="campo-filtro-estado-vazio">Campo: </option>
                            <option value="ID">ID</option>
                            <option value="EMAIL">E-mail</option>
                            <option value="NOME">Nome</option>
                            <option value="TIPO_USUARIO">Tipo do usuário</option>
                            <option value="RAIO_PROCURA_KM">Raio de procura em km</option>
                        </select>
                        <button id="adicionar-filtro" class="botoes-laranjas">Adicionar filtro</button>
                        <button id="enviar-filtro" class="botoes-laranjas">Filtrar</button>
                    </div>
                    <button id="filtro-sair" class="botoes-sair"><img src="assets/icone/icone_sair.svg" alt="" class="icone-sair"></button>
                </div>

                <div id="conteudo-filtro">
                    <form action="POST" id="formulario-filtro">



                    </form>
                </div>
            </div>
        </dialog>
    </div>
</div>


<script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</body>
</html>