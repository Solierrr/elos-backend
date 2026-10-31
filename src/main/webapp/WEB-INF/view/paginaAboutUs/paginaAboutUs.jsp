<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Sobre nós</title>

  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg"
        type="image/x-icon">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/aboutus.css">

</head>
<body>
<div id="hero">
  <header>
    <div class="header_container">
      <a  href="${pageContext.request.contextPath}/">
        <img src="${pageContext.request.contextPath}/assets/icone/logotipo.svg" alt="Logo"
             class="logo-header">
      </a>
      <div class="divisor"></div>
      <nav class="navigation_header">
        <a href="${pageContext.request.contextPath}/" class="text">Início</a>
      </nav>
      <nav class="navigation_header">
        <a href="#secao1" class="text">Proposta</a>
      </nav>
      <nav class="navigation_header">
        <a href="#secao4" class="text">Diferenciais</a>
      </nav>
      <nav class="navigation_header">
        <a href="#hero" class="text">Sobre nós</a>
      </nav>
      <div class="buttons_header">
        <a href="${pageContext.request.contextPath}/login">
          <button class="button_entrar" type="button" aria-label="Entrar">
            <span class="text">Entrar</span>
          </button>
        </a>
        <a href="${pageContext.request.contextPath}/cadastro">
          <button class="button_cadastrar" type="button" aria-label="Cadastrar">
            <span class="text">Cadastrar</span>
          </button>
        </a>
      </div>
    </div>
  </header>
  <div class="destaqueHero">
    <div class="fraseHero">
      <h1 class="big_text">Sobre o Elos</h1>
      <p class="sub_title">Te ajudamos a encontrar quem precisar no setor solar, seja fornecedor,
        profissional ou cliente</p>
    </div>
  </div>

  <svg id="divisaoArco" viewBox="0 0 1440 70" preserveAspectRatio="none">
    <path d="M0,50 Q720,0 1440,50 L1440,70 L0,70 Z"></path>
  </svg>
</div>

<section id="secao1">
  <div class="container-secao1">
    <div class="desc-secao1">
      <div class="gap-1rem">
        <h2 class="big_text flex-column ">O setor solar <span class="accent">em um só lugar.</span>
        </h2>
        <div class="flex-column gap-2rem texto-secao1">
          <p>Nosso objetivo é facilitar a <span class="accent">conexão</span> entre os agentes do
            setor solar, aproximando empresas, fornecedores e profissionais <span class="accent">em uma única plataforma.</span>
          </p>

          <p>Buscamos centralizar informações, oportunidades e demandas, além de facilitar a <span
                  class="accent">comunicação</span> entre os participantes.</p>

          <p>A <span class="accent">Elos</span> é voltada exclusivamente ao <span class="accent">mercado corporativo.</span>
          </p>
        </div>
      </div>

      <img class="img-secao1 " src="${pageContext.request.contextPath}/assets/img/imgSecao1.jpg"
           alt="Imagem de uma casa com painéis solares no telhado.">
    </div>
  </div>
</section>

<section id="secao2">
  <div>
    <h2 class="big_text secoes-title">Quais possibilidades o <span class="accent">Elos traz?</span>
    </h2>

    <div id="secao-possibilidades">

      <div class="possibilidades-secao2">
        <div class="flex-column gap-1rem">
          <h3 class="title texto-secao2">Fornecedor</h3>

          <p class="text texto-secao2">Venda seus produtos e encontre novos clientes</p>
        </div>

        <div class="flex-column gap-1rem">
          <div class="divisor-secao2"></div>
          <h4 class="sub_title texto-secao2">O que <span class="accent">você</span> pode fazer</h4>

          <ul class="lista-secao2">
            <li>Contatar clientes</li>
            <li>Gerenciar projetos solares</li>
            <li>Criar um catalogo de produtos</li>
            <li>Encontrar profissionais</li>
          </ul>
        </div>

      </div>

      <div class="possibilidades-secao2">
        <div class="flex-column gap-1rem">
          <h3 class="title texto-secao2">Profissional</h3>

          <p class="text texto-secao2">Encontre oportunidades e organize seus trabalhos</p>
        </div>

        <div class="flex-column gap-1rem">
          <div class="divisor-secao2"></div>

          <h4 class="sub_title texto-secao2">O que <span class="accent">você</span> pode fazer</h4>
          <ul class="lista-secao2">
            <li>buscar serviços e empregos</li>
            <li>Organizar sua agenda</li>
            <li>Cadastrar suas qualificações</li>
          </ul>
        </div>
      </div>

      <div class="possibilidades-secao2">
        <div id="title-card3-secao2" class="flex-column gap-1rem">
          <h3 class="title texto-secao2">Cliente</h3>

          <p class="text texto-secao2">Encontre fornecedores e monte seu projeto</p>
        </div>

        <div id="list-card3-secao2" class="flex-column gap-1rem">
          <div class="divisor-secao2"></div>

          <h4 class="sub_title texto-secao2">O que <span class="accent">você</span> pode fazer</h4>
          <ul class="lista-secao2">
            <li>Buscar fornecedores</li>
            <li>Consultar catalogos</li>
            <li>Criar projetos</li>
          </ul>
        </div>

      </div>

    </div>
  </div>
</section>

<section id="secao3">

  <h2 class="big_text secoes-title">Conheça nossos <span class="accent">planos</span></h2>

  <div class="botoes-secao3">

    <div class="button-mensalidade">
      <button
              id="alternarPeriodo"
              class="interruptor"
              type="button"
              aria-pressed="false">
        <span class="interruptor-bolinha"></span>
      </button>

      <span id="texto-mensalidade" class="medium text">Mensal</span>
    </div>

    <div class="botoes-planos">
      <button class="botao-plano sub_title ativo"
              type="button"
              data-plano="fornecedor"
              aria-pressed="true">
        Fornecedor
      </button>

      <button class="botao-plano sub_title"
              type="button"
              data-plano="profissional"
              aria-pressed="false">
        Profissional
      </button>

      <button class="botao-plano sub_title"
              type="button"
              data-plano="cliente"
              aria-pressed="false">
        Cliente
      </button>
    </div>

  </div>


  <div class="cards-secao3">
    <div id="card-fornecedor">
      <div class="coluna-principal">

        <h3 class="title">Elos Fornece</h3>

        <p class="text">A partir de:</p>

        <p class="big_text accent" id="precoFornece" data-mensal="R$ 300,00"
           data-anual="R$ 3.000,00">R$ 300,00</p>
        <p class="medium text accent periodoPlano" data-mensal="por mês" data-anual="por ano">por
          mês</p>

        <ul class="little_text lista-beneficios">
          <li>Até 49 produtos gratuitamente</li>
          <li>Até 19 profissionais afiliados gratuitamente</li>
          <li>Catalogo de serviços ilimitado</li>
          <li>Capacidade de atuar como empresa demandante</li>
          <li>Infograficos de projetos e vendas</li>
          <li class="unchecked">Profissionais ou mais que 49 produtos tem incremento na
            mensalidade
          </li>
        </ul>

      </div>

      <div class="coluna-faixas">

        <p class="little_title">
          Nosso plano conta com algumas faixas de preço extras. Até:
        </p>

        <ul id="faixas-produtos" class="little_text faixas-fornecedor">
          <li>49 produtos<span class="accent bold">R$ 00,00</span></li>
          <li>99 produtos<span class="accent bold" data-mensal="+R$ 50,00" data-anual="+R$ 500,00">+R$ 50,00</span>
          </li>
          <li>149 produtos<span class="accent bold" data-mensal="+R$ 100,00"
                                data-anual="+R$ 1000,00">+R$ 100,00</span></li>
          <li>150+ produtos<span class="accent bold" data-mensal="+R$ 150,00"
                                 data-anual="+R$ 1500,00">+R$ 150,00</span></li>
        </ul>

        <ul id="faixas-profissionais" class="little_text faixas-fornecedor">
          <li>19 produtos<span class="accent bold">R$ 00,00</span></li>
          <li>39 produtos<span class="accent bold" data-mensal="+R$ 150,00"
                               data-anual="+R$ 1500,00">+R$ 150,00</span></li>
          <li>59 produtos<span class="accent bold" data-mensal="+R$ 300,00"
                               data-anual="+R$ 3000,00">+R$ 300,00</span></li>
          <li>60+ produtos<span class="accent bold" data-mensal="+R$ 450,00"
                                data-anual="+R$ 4500,00">+R$ 450,00</span></li>
        </ul>

        <p class="little_text observacao"
           data-mensal="Ao passar de uma faixa para outra, o novo valor ao lado é acrescido da mensalidade e cobrada a partir do proximo mês"
           data-anual="Ao passar de uma faixa para outra, a diferença entre as faixas com base na quantidade de meses restantes e do contrato é cobrada em 30 dias">
          Ao passar de uma faixa para outra, o novo valor ao lado é acrescido da mensalidade e
          cobrada a partir do proximo mês</p>

      </div>
    </div>

    <div id="card-profissional" hidden>

      <div class="plano-profissional">
        <h3 class="title">Elos Campo</h3>
        <p class="text">A partir de:</p>
        <p class="big_text accent" id="preco-campo" data-mensal="R$ 29,00" data-anual="R$ 290,00">R$
          29,00</p>
        <p class="medium text accent periodoPlano" data-mensal="por mês" data-anual="por ano">por
          mês</p>
        <ul id="beneficios-campo" class="little_text lista-beneficios">
          <li>Primeiro mês gratuito</li>
          <li>Agenda integrada com os projetos que participa</li>
          <li>Registro de qualificações com validação</li>
          <li>Capacidade de buscar serviços e empregos por area</li>
        </ul>
      </div>

      <div class="plano-profissional">
        <h3 class="title">Elos Campo+</h3>
        <p class="text">A partir de:</p>
        <p class="big_text accent" id="preco-campo-mais" data-mensal="R$ 59,00"
           data-anual="R$ 590,00">R$ 59,00</p>
        <p class="medium text accent periodoPlano" data-mensal="por mês" data-anual="por ano">por
          mês</p>
        <ul id="beneficios-campo-mais" class="little_text lista-beneficios">
          <li>Primeiro mês gratuito</li>
          <li>Agenda integrada com os projetos que participa</li>
          <li>Registro de qualificações com validação</li>
          <li>Capacidade de buscar serviços e empregos por area</li>
          <li>Acesso a conteudos de capacitação/treinamento oferecidos por parceiros</li>
        </ul>
      </div>

    </div> <!-- Card dos planos do profissional, o JS que vai ativa-lo. -->

    <div id="card-cliente" hidden>
      <div class="coluna-principal">

        <h3 class="title">Elos Base</h3>

        <p class="text">A partir de:</p>

        <p class="big_text accent" id="preco-base">R$ 00,00</p>
        <p class="medium text accent periodoPlano" data-mensal="por mês" data-anual="por ano">por
          mês</p>

        <ul class="little_text lista-beneficios">
          <li>Limite de 4 unidades empresariais</li>
          <li>Histórico completo por unidade (Fotos, atualizações, etc).</li>
          <li>Recursos informativos por unidade (Radiação solar, dados metereologicos, etc.)</li>
          <li>Busca de fornecedores por distancia e produtos</li>
          <li class="unchecked">5 ou mais unidades tem incremento na mensalidade</li>
        </ul>

      </div>
      <br>
      <div class="coluna-faixas">

        <h3 class="title" id="titulo-coluna-faixas-cliente">Elos Rede</h3>

        <p class="little_title">O preço varia de acordo com a quantidade de unidades. Passa a ser
          cobrado a partir de 5.
          até:</p>

        <ul id="faixas-unidades" class="little_text">
          <li>5 unidades<span class="accent bold">R$ 00,00</span></li>
          <li>24 unidades<span class="accent bold" data-mensal="+R$ 50,00" data-anual="+R$ 500,00">+R$ 50,00</span>
          </li>
          <li>49 unidades<span class="accent bold" data-mensal="+R$ 100,00"
                               data-anual="+R$ 1000,00">+R$ 100,00</span></li>
          <li>74 unidades<span class="accent bold" data-mensal="+R$ 150,00"
                               data-anual="+R$ 1500,00">+R$ 150,00</span></li>
          <li>99 unidades<span class="accent bold" data-mensal="+R$ 200,00"
                               data-anual="+R$ 2000,00">+R$ 200,00</span></li>
          <li>100+ unidades<span class="accent bold" data-mensal="+R$ 250,00"
                                 data-anual="+R$ 2500,00">+R$ 250,00</span></li>

        </ul>

        <p class="little_text observacao"
           data-mensal="Ao passar de uma faixa para outra, o novo valor ao lado é acrescido da mensalidade e cobrada a partir do proximo mês"
           data-anual="Ao passar de uma faixa para outra, a diferença entre as faixas com base na quantidade de meses restantes e do contrato é cobrada em 30 dias">
          Ao passar de uma faixa para outra, o novo valor ao lado é acrescido da mensalidade e
          cobrada a partir do proximo mês</p>

      </div>
    </div> <!-- Card dos planos do cliente, o JS que vai ativa-lo.  -->
  </div>
</section>

<section id="secao4">
  <div>
    <h2 class="big_text  secoes-title">Nossos <span class="accent">diferenciais</span></h2>

    <div class="grid-container">

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text"><span class="accent">Ecossistema</span><span> Unificado</span></h3>
        <p class="little_text">Ambiente único para fornecedores, empresas e profissionais.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text">Exclusividade <span class="accent">B2B</span></h3>
        <p class="little_text">Foco exclusivo nas necessidades e dinâmicas do mercado
          corporativo.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text"><span class="accent">Busca</span> Geográfica</h3>

        <p class="little_text">Encontre talentos e parceiros profissionais próximos ao seu
          projeto.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text"><span class="accent">Agenda</span> Integrada</h3>

        <p class="little_text">Sincronização centralizada para prestação e execução de serviços.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text">Múltiplas <span class="accent">Unidades</span></h3>

        <p class="little_text">Estrutura preparada para gerenciar filiais e demandas complexas.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text"><span class="accent">Qualificações</span> Checadas</h3>

        <p class="little_text">Registro e validação de certificações e competências técnicas.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text"><span class="accent">Gestão</span> de Demandas</h3>

        <p class="little_text">Organização eficiente de projetos e requisitos técnicos
          fotovoltaicos.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text">Catálogo <span class="accent">Consolidado</span></h3>

        <p class="little_text">Acesso rápido a equipamentos e especificações do setor.</p>
      </div>

      <div class="item-diferenciais gap-1rem">
        <h3 class="medium text"><span class="accent">Central</span> de Diálogo</h3>

        <p class="little_text">Comunicação direta e centralizada entre todos os envolvidos.</p>
      </div>

    </div>
    <p class="contador-pilha little_text">Toque para ver mais •
      <span id="contador-atual" class="accent">1</span> / <span id="contador-total"
                                                                class="accent">9</span></p>
  </div>

</section>

<section id="marketing">
  <div class="title-marketing">
    <h2 class="big_text">Os elos certos para transformar o </h2>
    <h2 class="big_text">seu investimento em resultado.</h2>
  </div>


  <div>
    <a href="${pageContext.request.contextPath}/cadastro">
      <button class="button-cadastre-se">
        <span class="sub_title">Cadastrar</span>
      </button>
    </a>

  </div>
</section>

<section id="footer">
  <div id="logo-container-footer">

    <div id="logo-footer">
      <img src="${pageContext.request.contextPath}/assets/img/logo_laranja.png" alt="Logo">
    </div>

    <div>
      <p class="little_title" id="slogan-footer">Compra, manutenção,<br> históricos, <b>tudo</b>
        aqui.</p>
    </div>

  </div>
  <div class="grid-footer">

    <div class="flex-column gap-1rem">
      <h3 class="sub_title">Sobre</h3>
      <div class="flex-column gap-1rem" id="sobre-footer">
        <a href="#secao2" class="text">Nossa proposta</a>

        <a href="${pageContext.request.contextPath}#FAQ/" class="text">FAQ</a>
      </div>
    </div>

    <div class="flex-column gap-1rem" id="navegacao-footer">
      <h3 class="sub_title">Navegação</h3>
      <div class="flex-column gap-1rem">
        <a href="${pageContext.request.contextPath}/" class="text">Início</a>

        <a href="${pageContext.request.contextPath}/login" class="text">Portal</a>
      </div>
    </div>

    <div class="flex-column gap-1rem">
      <h3 class="sub_title" id="contatos-footer">Contatos</h3>
      <div class="flex-column gap-1rem">
        <a href="https://www.instagram.com/interdisciplinar.elos/" class="text" id="instagram">Instagram</a>

        <a href="mailto:interdisciplinar.elos@gmail.com" class="text" id="gmail">Gmail</a>
      </div>
    </div>

  </div>
</section>

<script src="${pageContext.request.contextPath}/js/aboutusScript.js" defer></script>
</body>
</html>

