<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Elos CRUD</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/crud.css"/>
  <link rel="icon" type="image/svg"
        href="${pageContext.request.contextPath}/assets/icone/LOGO.svg">

</head>
<body>
<input type="hidden" value="admin" id="nome-do-arquivo">
<span id="imagens-tabela"
      data-url-editar="${pageContext.request.contextPath}/assets/icone/crud/caneta_editar.svg"
      data-url-apagar="${pageContext.request.contextPath}/assets/icone/crud/botao_ligar.svg"></span>

<div class="conteiner">
  <nav class="side-bar" id="side-bar-id">
    <div class="conteiner-logo" id="caixa-logo">
      <a id="link-da-logo">
        <img src="${pageContext.request.contextPath}/assets/icone/LOGO.svg" id="logo"
             alt="botão para a tela inicial"/>
        <span id="span-logo">Elos</span>
      </a>
      <div class="traco-abaixo-logo"></div>
    </div>

    <div class="caixa-sidebar" id="conteiner-abrir-sidebar">
      <img src="${pageContext.request.contextPath}/assets/icone/crud/abrir_sidebar.svg"
           alt="abrir side bar" id="botao-abrir-sidebar"
           data-url-abrir="${pageContext.request.contextPath}/assets/icone/crud/abrir_sidebar.svg"
           data-url-fechar="${pageContext.request.contextPath}/assets/icone/crud/fechar_sidebar.svg">
    </div>

    <div id="botoes-navegacao" class="caixa-sidebar">
      <span id="span-botoes-navegacao">Navegação</span>
      <a href="${pageContext.request.contextPath}/crudUsuario">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/usuario.svg"
             alt="Ir para tela do CRUD do usuário"/>
        <span id="span-usuario">Usuário</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudEmpresaDemandante">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/empresa_demandante.svg"
             alt="Ir para tela do CRUD da empresa demandante"/>
        <span id="span-empresa-demandante">Empresa Demandante</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudFornecedor">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/fornecedor.svg"
             alt="Ir para tela do CRUD do fornecedor"/>
        <span id="span-fornecedor">Fornecedor</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudProfissional">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/profissional.svg"
             alt="Ir para tela do CRUD do profissional"/>
        <span id="span-profissional">Profissional</span>
      </a>
    </div>

    <div id="botoes-localizacao" class="caixa-sidebar">
      <span id="span-botoes-localizacao">Localização</span>
      <a href="${pageContext.request.contextPath}/crudTelefone">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/telefone.svg"
             alt="Ir para tela do CRUD do telefone"/>
        <span id="span-telefone">Telefone</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudEndereco">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/endereco.svg"
             alt="Ir para tela do CRUD do endereço"/>
        <span id="span-endereco">Endereço</span>
      </a>
    </div>

    <div id="botoes-admin" class="caixa-sidebar">
      <span id="span-botoes-admin">Admin</span>
      <a class="opcao-side-bar-selecionada">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/admin.svg"
             alt="Ir para tela do CRUD do admin">
        <span id="span-admin">Admin</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudTelefoneAdmin">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/telefone.svg"
             alt="Ir para tela do CRUD do telefone do admin">
        <span id="span-telefone-admin">Telefone do admin</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudHistoricoAlteracoes">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/historico_alteracoes.svg"
             alt="Ir para tela do CRUD do histórico de alterações">
        <span id="span-hitorico-alteracoes">Histórico de Alterações</span>
      </a>
      <a href="${pageContext.request.contextPath}/crudAcessoAdmin">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/acesso_admin.svg"
             alt="Ir para tela do CRUD do histórico de acessos dos admins">
        <span id="span-acessos-admin">Acessos dos admins</span>
      </a>
    </div>

    <div class="caixa-sidebar">
      <span id="span-opcoes">Opções</span>
      <a href="${pageContext.request.contextPath}/logout">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/sair.svg"
             alt="Deslogar do CRUD">
        <span id="span-sair">Sair</span>
      </a>
    </div>
  </nav>

  <div id="conteiner-do-conteudo">
    <div id="conteiner-botoes">
      <p id="titulo-tabela" class="text">Admin</p>
      <div id="traco-filtros"></div>
      <form id="formulario-filtro-rapido" method="POST"
            action="crudUsuario">
        <div id="caixa-busca-rapida">
          <button type="submit">
            <img src="assets/icone/crud/lupa_busca_rapida.svg">
          </button>
          <input placeholder="Busca rápida" id="input-busca-rapida">
        </div>
      </form>

      <button class="botoes-brancos" id="filtro-entrar">Filtrar
      </button>
      <c:if test="${not empty acessoTotal}">
        <button id="insert-entrar" class="botoes-laranjas ">
          Adicionar
          registro
        </button>
      </c:if>
    </div>

    <div id="caixa-tabela">
      <table>
        <thead>
        <tr>
          <th class="littleText">ID</th>
          <th class="littleText">E-mail</th>
          <th class="littleText">Nome</th>
          <th class="littleText">Função</th>
          <th class="littleText">Em atividade?</th>

          <c:if test="${not empty acessoTotal}">
            <th class="littleText">Ações</th>
          </c:if>
        </tr>
        </thead>
        <tbody>

        </tbody>
      </table>
    </div>

    <c:if test="${not empty acessoTotal}">
      <dialog class="pop-up" id="pop-up-update">
        <form id="formulario-update" method="POST"
              action="${pageContext.request.contextPath}/crudAdmin-update">
          <div id="conteiner-superior-update">
            <button type="submit" name="enviar-update" id="enviar-update"
                    class="botoes-pop-up-laranjas">Atualizar admin
            </button>
            <button id="update-sair" type="button" class="botoes-sair"><img
                    src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg"
                    alt="" class="icone-sair"></button>
          </div>

          <input id="id-update" name="id-update" type="hidden" value="">
          <div id="conteudo-update">
            <div>
              <div class="label-e-input">
                <label for="email-update">E-mail</label>
                <input type="email" name="email-update" id="email-update"
                       placeholder="Deixe em branco caso não for alterar">
              </div>
              <span id="erro-email-update" class="span-erro"></span>
            </div>
            <div>
              <div class="label-e-input">
                <label for="senha-update">Senha</label>
                <div class="senha-container">
                  <input id="senha-update" type="password" name="senha-update"
                         placeholder="Deixe em branco caso não for alterar"
                         autocomplete="new-password"
                         pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}">
                  <img id="olho-senha-update"
                       src="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                       data-url-olho="${pageContext.request.contextPath}/assets/icone/olho.svg"
                       data-url-oculto="${pageContext.request.contextPath}/assets/icone/olho-oculto.svg"
                       alt="Mostrar senha">
                </div>
              </div>
              <span id="erro-senha-update" class="span-erro"></span>
            </div>

            <div>
              <div class="label-e-input">
                <label for="nome-update">Nome</label>
                <input type="text" name="nome-update" id="nome-update"
                       placeholder="Deixe em branco caso não for alterar">
              </div>
              <span id="erro-nome-update" class="span-erro"></span>
            </div>

            <div>
              <div class="label-e-input">
                <label for="em-atividade-update">O admin está ativo?</label>
                <select name="em-atividade-update" id="em-atividade-update" required>
                  <option value="" disabled selected hidden>O admin está ativo?
                  </option>
                  <option value="true">Sim</option>
                  <option value="false">Não</option>
                </select>
              </div>
              <span id="erro-em-atividade-update" class="span-erro"></span>
            </div>
          </div>
        </form>
      </dialog>

      <dialog class="pop-up" id="pop-up-insert">
        <form id="formulario-insert" method="POST"
              action="${pageContext.request.contextPath}/crudAdmin-insert">
          <div id="conteiner-superior-insert">
            <button type="submit" name="enviar-insert" id="enviar-insert"
                    class="botoes-pop-up-laranjas">Adicionar telefone
            </button>
            <button id="insert-sair" type="button" class="botoes-sair"><img
                    src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg"
                    alt="" class="icone-sair"></button>
          </div>

          <div id="conteudo-insert">
            <div>
              <div class="label-e-input">
                <label for="email">E-mail</label>
                <input type="email" name="email" id="email" required
                       placeholder="ex: meu.email@gmail.com">
              </div>
              <span id="erro-email" class="span-erro"></span>
            </div>
            <div>
              <div class="label-e-input">
                <label for="senha">Senha</label>
                <div class="senha-container">
                  <input id="senha" type="password" name="senha"
                         placeholder="Coloque a senha do usuário"
                         autocomplete="new-password"
                         pattern="(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}">
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
                <input type="text" name="nome" id="nome" required
                       placeholder="ex: Roger dos santos"
                >
              </div>
              <span id="erro-nome" class="span-erro"></span>
            </div>

            <div>
              <div class="label-e-input">
                <label for="funcao">Função do admin</label>
                <select name="funcao" id="funcao" required>
                  <option value="" disabled selected hidden>Selecione a função do admin
                  </option>
                  <option value="REVISOR">Revisor</option>
                  <option value="GERENTE">Gerente</option>
                  <option value="ANALISTA">Analista</option>
                  <option value="MODELADOR">Modelador</option>
                </select>
              </div>
              <span id="erro-funcao" class="span-erro"></span>
            </div>

            <div>
              <div class="label-e-input">
                <label for="em-atividade">O admin está ativo</label>
                <select name="em-atividade" id="em-atividade" required>
                  <option value="" disabled selected hidden>Selecione uma opção
                  </option>
                  <option value="true">Sim</option>
                  <option value="false">Não</option>
                </select>
              </div>
              <span id="erro-em-atividade" class="span-erro"></span>
            </div>
          </div>
        </form>
      </dialog>
    </c:if>


    <dialog class="pop-up" id="pop-up-filtro"
            data-url-apagar="${pageContext.request.contextPath}/assets/icone/crud/lixeira_apagar.svg">
      <div id="conteiner-superior-filtro">
        <div id="botoes-filtro">
          <select name="campo-filtro" id="campo-filtro">
            <option value="CAMPO_VAZIO" disabled selected hidden
                    id="campo-filtro-estado-vazio">Campo:
            </option>
            <option value="ID">ID</option>
            <option value="EMAIL">E-mail</option>
            <option value="NOME">Nome</option>
            <option value="FUNCAO">Função</option>
            <option value="EM_ATIVIDADE">Em atividade</option>
            <option value="JOIN">Conectar com registro interligado</option>
          </select>
          <button id="adicionar-filtro" class="botoes-pop-up-laranjas">Adicionar filtro
          </button>
          <button id="enviar-filtro" class="botoes-pop-up-laranjas">Filtrar</button>
        </div>
        <button id="filtro-sair" type="button" class="botoes-sair"><img
                src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg"
                alt="" class="icone-sair"></button>
      </div>

      <div id="conteudo-filtro">
        <form method="POST" id="formulario-filtro"
              action="${pageContext.request.contextPath}/crudAdmin">

        </form>
      </div>
    </dialog>
  </div>

</div>

<c:if test="${not empty acessoTotal}">
  <form action="${pageContext.request.contextPath}/crudAdmin-delete" method="POST"
        id="formulario-delete">
  </form>

  <form action="${pageContext.request.contextPath}/crudAdmin-update" method="GET"
        id="formulario-update-dados">
  </form>

  <script type="module" src="${pageContext.request.contextPath}/js/crudUpdate.js" defer></script>
  <script type="module" src="${pageContext.request.contextPath}/js/crudDelete.js" defer></script>
  <script type="module" src="${pageContext.request.contextPath}/js/crudInsert.js" defer></script>
</c:if>


<script type="module" src="${pageContext.request.contextPath}/js/crudRead.js" defer></script>

<script src="${pageContext.request.contextPath}/js/popUps.js" defer></script>
<script src="${pageContext.request.contextPath}/js/abrirSidebar.js"></script>
<script src="${pageContext.request.contextPath}/js/botoesSenha.js"></script>

<meta name="context-path" content="${pageContext.request.contextPath}/erro">

</body>
</html>