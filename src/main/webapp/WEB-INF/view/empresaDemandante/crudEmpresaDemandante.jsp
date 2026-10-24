<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<input type="hidden" value="empresa-demandante" id="nome-do-arquivo">
<span id="imagens-tabela"
      data-url-editar="${pageContext.request.contextPath}/assets/icone/crud/caneta_editar.svg"
      data-url-apagar="${pageContext.request.contextPath}/assets/icone/crud/lixeira_apagar.svg"></span>

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
      <a class="opcao-side-bar-selecionada">
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
      <a href="${pageContext.request.contextPath}/crudAdmin">
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
      <a href="">
        <img src="${pageContext.request.contextPath}/assets/icone/crud/sair.svg"
             alt="Deslogar do CRUD">
        <span id="span-sair">Sair</span>
      </a>
    </div>
  </nav>

  <div id="conteiner-do-conteudo">
    <div id="conteiner-botoes">
      <p id="titulo-tabela" class="text">Empresa demandante</p>
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
    </div>

    <div id="caixa-tabela">
      <table>
        <thead>
        <tr>
          <th class="littleText">ID</th>
          <th class="littleText">ID do Usuário</th>
          <th class="littleText">CNPJ</th>
          <th class="littleText">Razão Social</th>
          <th class="littleText">É uma filial?</th>
          <th class="littleText">Ações</th>
        </tr>
        </thead>
        <tbody>

        </tbody>
      </table>
    </div>

    <dialog class="pop-up" id="pop-up-update">
      <form id="formulario-update" method="POST"
            action="${pageContext.request.contextPath}/crudEmpresaDemandante-update">
        <div id="conteiner-superior-update">
          <button type="submit" name="enviar-update" id="enviar-update"
                  class="botoes-pop-up-laranjas">Atualizar empresa demandante
          </button>
          <button id="update-sair" type="button" class="botoes-sair"><img
                  src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg"
                  alt="" class="icone-sair"></button>
        </div>

        <input id="id-update" name="id-update" type="hidden" value="">
        <div id="conteudo-update">
          <div>
            <div>
              <div class="label-e-input">
                <label for="razao-social-update">Razão social</label>
                <input id="razao-social-update" type="text"
                       name="razao-social-update"
                       placeholder="Deixe em branco caso não for alterar">
              </div>
            </div>
            <span id="erro-razao-social-update" class="span-erro"></span>
          </div>

          <div>
            <div class="label-e-input">
              <label for="eh-mandante-update">A empresa é uma filial?</label>
              <select name="eh-mandante-update" id="eh-mandante-update">
                <option value="CAMPO_VAZIO" disabled selected hidden
                        id="eh-mandante-update-estado-vazio">É uma filial?
                </option>
                <option value="true">Sim</option>
                <option value="false">Não</option>
              </select>
              <span id="erro-eh-mandante-update" class="span-erro"></span>
            </div>
          </div>
          <div>
            <div class="label-e-input">
              <label for="cnpj-update">CNPJ</label>
              <div class="div-update"
                   id="cnpj-update"></div>
            </div>
            <span id="erro-cnpj-update" class="span-erro"></span>
          </div>
        </div>

      </form>
    </dialog>

    <dialog class="pop-up" id="pop-up-filtro"
            data-url-apagar="${pageContext.request.contextPath}/assets/icone/crud/lixeira_apagar.svg">
      <div id="conteiner-superior-filtro">
        <div id="botoes-filtro">
          <select name="campo-filtro" id="campo-filtro">
            <option value="CAMPO_VAZIO" disabled selected hidden
                    id="campo-filtro-estado-vazio">Campo:
            </option>
            <option value="ID">ID</option>
            <option value="ID_USUARIO">ID do usuário</option>
            <option value="CNPJ">CNPJ</option>
            <option value="RAZAO_SOCIAL">Razão social</option>
            <option value="EH_MANDANTE">É uma filial?</option>
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
              action="${pageContext.request.contextPath}/crudEmpresaDemandante">

        </form>
      </div>
    </dialog>
  </div>

</div>

<form action="${pageContext.request.contextPath}/crudEmpresaDemandante-delete" method="POST"
      id="formulario-delete">
</form>

<form action="${pageContext.request.contextPath}/crudEmpresaDemandante-update" method="GET"
      id="formulario-update-dados">
</form>


<script type="module" src="${pageContext.request.contextPath}/js/crudRead.js" defer></script>
<script type="module" src="${pageContext.request.contextPath}/js/crudUpdate.js" defer></script>
<script type="module" src="${pageContext.request.contextPath}/js/crudDelete.js" defer></script>

<script src="${pageContext.request.contextPath}/js/popUps.js" defer></script>
<script src="${pageContext.request.contextPath}/js/abrirSidebar.js"></script>

</body>
</html>