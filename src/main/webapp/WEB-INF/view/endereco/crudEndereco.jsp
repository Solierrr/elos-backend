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
<input type="hidden" value="endereco" id="nome-do-arquivo">
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
      <a class="opcao-side-bar-selecionada">
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
      <p id="titulo-tabela" class="text">Endereço</p>
      <div id="traco-filtros"></div>
      <form id="formulario-filtro-rapido">
        <div id="caixa-busca-rapida">
          <button type="submit">
            <img src="assets/icone/crud/lupa_busca_rapida.svg">
          </button>
          <input placeholder="Busca rápida" id="input-busca-rapida">
        </div>
      </form>

      <button class="botoes-brancos" id="filtro-entrar">Filtrar
      </button>
      <button id="insert-entrar" class="botoes-laranjas ">
        Adicionar
        registro
      </button>
    </div>

    <div id="caixa-tabela">
      <table>
        <thead>
        <tr>
          <th class="littleText">ID</th>
          <th class="littleText">ID do usuário</th>
          <th class="littleText">Estado</th>
          <th class="littleText">Cidade</th>
          <th class="littleText">Bairro</th>
          <th class="littleText">CEP</th>
          <th class="littleText">Logradouro</th>
          <th class="littleText">Número</th>
          <th class="littleText">Complemento</th>
          <th class="littleText">Ações</th>
        </tr>
        </thead>
        <tbody>

        </tbody>
      </table>
    </div>

    <dialog class="pop-up" id="pop-up-update">
      <form id="formulario-update" method="POST"
            action="${pageContext.request.contextPath}/crudEndereco-update">
        <div id="conteiner-superior-update">
          <button type="submit" name="enviar-update" id="enviar-update"
                  class="botoes-pop-up-laranjas">Atualizar telefone
          </button>
          <button id="update-sair" type="button" class="botoes-sair"><img
                  src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg"
                  alt="" class="icone-sair"></button>
        </div>

        <input id="id-update" name="id-update" type="hidden" value="">
        <div id="conteudo-update">
          <div>
            <div class="label-e-input">
              <label for="estado-update">Estado</label>
              <select name="estado-update" id="estado-update">
                <option value="" disabled selected>Selecione um estado</option>
                <option value="AC">Acre</option>
                <option value="AL">Alagoas</option>
                <option value="AP">Amapá</option>
                <option value="AM">Amazonas</option>
                <option value="BA">Bahia</option>
                <option value="CE">Ceará</option>
                <option value="DF">Distrito Federal</option>
                <option value="ES">Espírito Santo</option>
                <option value="GO">Goiás</option>
                <option value="MA">Maranhão</option>
                <option value="MT">Mato Grosso</option>
                <option value="MS">Mato Grosso do Sul</option>
                <option value="MG">Minas Gerais</option>
                <option value="PA">Pará</option>
                <option value="PB">Paraíba</option>
                <option value="PR">Paraná</option>
                <option value="PE">Pernambuco</option>
                <option value="PI">Piauí</option>
                <option value="RJ">Rio de Janeiro</option>
                <option value="RN">Rio Grande do Norte</option>
                <option value="RS">Rio Grande do Sul</option>
                <option value="RO">Rondônia</option>
                <option value="RR">Roraima</option>
                <option value="SC">Santa Catarina</option>
                <option value="SP">São Paulo</option>
                <option value="SE">Sergipe</option>
                <option value="TO">Tocantins</option>
              </select>
            </div>
            <span id="erro-estado-update" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="cidade-update">Cidade</label>
              <input type="text" name="cidade-update" id="cidade-update"
                     placeholder="Deixe em branco caso não for alterar">
            </div>
            <span id="erro-cidade-update" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="bairro-update">Bairro</label>
              <input type="text" name="bairro-update" id="bairro-update"
                     placeholder="Deixe em branco caso não for alterar">
            </div>
            <span id="erro-bairro-update" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="cep-update">CEP</label>
              <input type="text" name="cep-update" id="cep-update"
                     placeholder="Deixe em branco caso não for alterar">
            </div>
            <span id="erro-cep-update" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="logradouro-update">Logradouro</label>
              <input type="text" name="logradouro-update" id="logradouro-update"
                     placeholder="Deixe em branco caso não for alterar">
            </div>
            <span id="erro-logradouro-update" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="numero-update">Número</label>
              <input type="text" name="numero-update" id="numero-update"
                     placeholder="Deixe em branco caso não for alterar">
            </div>
            <span id="erro-numero-update" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="complemento-update">Complemento</label>
              <input type="text" name="complemento-update" id="complemento-update"
                     placeholder="Deixe em branco caso não for alterar">
            </div>
            <span id="erro-complemento-update" class="span-erro"></span>
          </div>
        </div>
      </form>
    </dialog>

    <dialog class="pop-up" id="pop-up-insert">
      <form id="formulario-insert" method="POST"
            action="${pageContext.request.contextPath}/crudEndereco-insert">
        <div id="conteiner-superior-insert">
          <button type="submit" name="enviar-insert" id="enviar-insert"
                  class="botoes-pop-up-laranjas">Adicionar endereço
          </button>
          <button id="insert-sair" type="button" class="botoes-sair"><img
                  src="${pageContext.request.contextPath}/assets/icone/crud/icone_sair.svg"
                  alt="" class="icone-sair"></button>
        </div>

        <div id="conteudo-insert">
          <div>
            <div class="label-e-input">
              <label for="id-usuario">ID do usuário dono do telefone</label>
              <input id="id-usuario" type="text" name="id-usuario"
                     placeholder="ex: 95"
              >
            </div>
            <span id="erro-id-usuario" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="estado">Estado</label>
              <select name="estado" id="estado"  required>
                <option value="" disabled selected>Selecione um estado</option>
                <option value="AC">Acre</option>
                <option value="AL">Alagoas</option>
                <option value="AP">Amapá</option>
                <option value="AM">Amazonas</option>
                <option value="BA">Bahia</option>
                <option value="CE">Ceará</option>
                <option value="DF">Distrito Federal</option>
                <option value="ES">Espírito Santo</option>
                <option value="GO">Goiás</option>
                <option value="MA">Maranhão</option>
                <option value="MT">Mato Grosso</option>
                <option value="MS">Mato Grosso do Sul</option>
                <option value="MG">Minas Gerais</option>
                <option value="PA">Pará</option>
                <option value="PB">Paraíba</option>
                <option value="PR">Paraná</option>
                <option value="PE">Pernambuco</option>
                <option value="PI">Piauí</option>
                <option value="RJ">Rio de Janeiro</option>
                <option value="RN">Rio Grande do Norte</option>
                <option value="RS">Rio Grande do Sul</option>
                <option value="RO">Rondônia</option>
                <option value="RR">Roraima</option>
                <option value="SC">Santa Catarina</option>
                <option value="SP">São Paulo</option>
                <option value="SE">Sergipe</option>
                <option value="TO">Tocantins</option>
              </select>
            </div>
            <span id="erro-estado" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="cidade">Cidade</label>
              <input type="text" name="cidade" id="cidade"
                     placeholder="ex: São Paulo" required>
            </div>
            <span id="erro-cidade" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="bairro">Bairro</label>
              <input type="text" name="bairro" id="bairro"
                     placeholder="ex: Jaragua" required>
            </div>
            <span id="erro-bairro" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="cep">CEP</label>
              <input type="text" name="cep" id="cep"
                     placeholder="ex: 01311-000" required>
            </div>
            <span id="erro-cep" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="logradouro">Logradouro</label>
              <input type="text" name="logradouro" id="logradouro"
                     placeholder="ex: Rua Borboletas Psicodélicas"required>
            </div>
            <span id="erro-logradouro" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="numero">Número</label>
              <input type="text" name="numero" id="numero"
                     placeholder="ex: 606" required>
            </div>
            <span id="erro-numero" class="span-erro"></span>
          </div>
          <div>
            <div class="label-e-input">
              <label for="complemento">Complemento</label>
              <input type="text" name="complemento" id="complemento"
                     placeholder="ex: Predio 3 do bloco A">
            </div>
            <span id="erro-complemento" class="span-erro"></span>
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
            <option value="ESTADO">Estado</option>
            <option value="CIDADE">Cidade</option>
            <option value="BAIRRO">Bairro</option>
            <option value="CEP">CEP</option>
            <option value="LOGRADOURO">Logradouro</option>
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
              action="${pageContext.request.contextPath}/crudEndereco">

        </form>
      </div>
    </dialog>
  </div>

</div>

<form action="${pageContext.request.contextPath}/crudEndereco-delete" method="POST"
      id="formulario-delete">
</form>

<form action="${pageContext.request.contextPath}/crudEndereco-update" method="GET"
      id="formulario-update-dados">
</form>

<script type="module" src="${pageContext.request.contextPath}/js/crudRead.js" defer></script>
<script type="module" src="${pageContext.request.contextPath}/js/crudUpdate.js" defer></script>
<script type="module" src="${pageContext.request.contextPath}/js/crudDelete.js" defer></script>
<script type="module" src="${pageContext.request.contextPath}/js/crudInsert.js" defer></script>


<script src="${pageContext.request.contextPath}/js/popUps.js" defer></script>
<script src="${pageContext.request.contextPath}/js/abrirSidebar.js"></script>

</body>
</html>