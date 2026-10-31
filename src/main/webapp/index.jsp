<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Landing Page</title>


  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/icone/LOGO.svg"
        type="image/x-icon">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/variables.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/text.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/landing.css">

</head>
<body>
<section id="hero">
  <header>
    <div class="header_container">
      <a href="${pageContext.request.contextPath}/">
        <img src="${pageContext.request.contextPath}/assets/icone/logotipo.svg" alt="Logo">
      </a>
      <div class="divisor"></div>
      <nav class="navigation_header">
        <a href="#hero" class="text">Início</a>
      </nav>
      <nav class="navigation_header">
        <a href="#secao7" class="text">Funcionalidades</a>
      </nav>
      <nav class="navigation_header">
        <a href="#FAQ" class="text">FAQ</a>
      </nav>
      <nav class="navigation_header">
        <a href="${pageContext.request.contextPath}/aboutUs" class="text">Sobre nós</a>
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
  <div class="destaque_hero">
    <div class="frase_hero">
      <h2 class="big_text">Um elo entre</h2>
      <h2 class="big_text espacamento">comprador e fornecedor</h2>
      <h3 class="sub_title">A intermediação entre empresas e setores fotovoltaicos,</h3>
      <h3 class="sub_title"> com a solução adequada a cada escala</h3>

    </div>
    <a href="${pageContext.request.contextPath}/aboutUs">
      <button class="button_saiba" type="button" aria-label="Saiba mais sobre a empresa">
        <span class="little_title">Saiba mais!</span>
      </button>
    </a>
  </div>

  <svg id="divisao_arco" viewBox="0 0 1400 70" preserveAspectRatio="none">
    <path d="M0,50 Q700,8 1440,50 L1400,70 L0,70 Z"></path>
  </svg>
</section>

<section id="secao1">
  <div class="frase_secao1">
    <h2 class="big_text">Explore nossos objetivos!</h2>
  </div>
  <div class="carrossel">
    <div class="busca_G slide_ativo">
      <img src="${pageContext.request.contextPath}/assets/img/busca_geografica.jpg"
           alt="Uma busca geográfica." class="img_BG">
      <div class="infos_carrossel">
        <h3 class="sub_title">Busca Geográfica</h3>
        <h4 class="text"> Encontre empresas e técnicas especializadas em instalação de painéis
          solares na sua região.</h4>
      </div>
    </div>

    <div class="divulgacao slide">
      <img src="${pageContext.request.contextPath}/assets/img/divulgacao.jpg"
           alt="Um homem pesquisando sobre divulgação de produtos." class="img_D">
      <div class="infos_carrossel">
        <h3 class="sub_title">Divulgação</h3>
        <h4 class="text">Conheça publicações sobre empresas técnicas, perfis de profissionais e
          outras informações relevantes do setor.</h4>
      </div>
    </div>

    <div class="comunicacao slide">
      <img src="${pageContext.request.contextPath}/assets/img/comunicacao.jpg"
           alt="Pessoas se comunicando." class="img_C">
      <div class="infos_carrossel">
        <h3 class="sub_title">Comunicação</h3>
        <h4 class="text">Utilize o nosso chat para conversar diretamente com fornecedores e
          técnicos.</h4>
      </div>
    </div>

    <div class="oportunidades slide">
      <img src="${pageContext.request.contextPath}/assets/img/oportunidades.jpg"
           alt="Oportunidades de negócio, sendo mostrada com uma chave e dinheiro ao fundo."
           class="img_O">
      <div class="infos_carrossel">
        <h3 class="sub_title">Oportunidades</h3>
        <h4 class="text">Descubra as melhores condições e ofertas para instalação de painéis solares
          oferecidas por empresas técnicas.</h4>
      </div>
    </div>
  </div>
  <div class="passagem">
    <button class="quadrado" type="button" role="group"
            aria-label="Ir para o primeiro carrossel"></button>
    <button class="quadrado" type="button" aria-label="Ir para o segundo carrossel"></button>
    <button class="quadrado" type="button" role="group"
            aria-label="Ir para o terceiro carrossel"></button>
    <button class="quadrado" type="button" aria-label="Ir para o quarto carrossel"></button>
  </div>
</section>

<section id="secao2">
  <div class="informacoes">

    <div class="frase_secao2">
      <h2 class="big_text">Conheça mais sobre nós!</h2>
      <h3 class="title"><span class="accent">Benefícios</span> que o Elos trará a você</h3>
    </div>

    <div class="informacao_secao2">
      <div class="acordeao_item">
        <button class="acordeao_button" type="button" id="btn_instalacao"
                aria-controle="resp-instalacao">
          <img src="${pageContext.request.contextPath}/assets/icone/engrenagem.svg"
               alt="Ícone de engrenagem" class="icone_engrenagem">
          <span class="sub_title">Instalação em múltiplas filais</span>
          <img src="${pageContext.request.contextPath}/assets/icone/setaPBaixo.svg"
               alt="Ícone de seta" class="icone_seta"></button>
        <div class="acordeao_painel" id="resp_instalacao" role="region"
             aria-labelledby="btn-instalacao">
          <div class="acordeao_conteudo">
            <p class="text">Faça a instalação e gerenciamento individual de cada filial.</p>
            <p class="text">Funcionalidades:</p>
            <ul>
              <li class="text">
                Localização;
              </li>
              <li class="text">
                Tipo de instalação;
              </li>
              <li class="text">
                Histórico de informações do local;
              </li>
              <li class="text">
                Registros fotográficos;
              </li>
              <li class="text">
                Atualizações ao longo do tempo.
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="informacao_secao2">
      <div class="acordeao_item">
        <button class="acordeao_button" type="button" id="btn_tecnicos"
                aria-controle="resp_tecnicos">
          <img src="${pageContext.request.contextPath}/assets/icone/pessoas.svg"
               alt="Ícone de pessoas" class="icone_pessoas">
          <span class="sub_title">Técnicos especializados</span>
          <img src="${pageContext.request.contextPath}/assets/icone/setaPBaixo.svg"
               alt="Ícone de seta" class="icone_seta"></button>
        <div class="acordeao_painel" id="resp_tecnicos" role="region"
             aria-labelledby="btn_tecnicos">
          <div class="acordeao_conteudo">
            <p class="text">A plataforma terá os registros de qualificações e credenciais
              profissionais que são submetidas a processos de validação documental.</p>
            <p class="text">Esses registros poderão incluir:</p>
            <ul>
              <li class="text">
                Formação profissional;
              </li>
              <li class="text">
                Registros em conselhos profissionais;
              </li>
              <li class="text">
                Certificações;
              </li>
              <li class="text">
                Documentações comprobatórias.
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="informacao_secao2">
      <div class="acordeao_item">
        <button class="acordeao_button" type="button" id="btn_ofertas" aria-controle="resp_ofertas">
          <img src="${pageContext.request.contextPath}/assets/icone/carrinho.svg" alt="Ícone de carrinho"
               class="icone_carrinho">
          <span class="sub_title">Ofertas únicas</span>
          <img src="${pageContext.request.contextPath}/assets/icone/setaPBaixo.svg"
               alt="Ícone de seta" class="icone_seta"></button>
        <div class="acordeao_painel" id="resp_ofertas" role="region" aria-labelledby="btn_ofertas">
          <div class="acordeao_conteudo">
            <p class="text">Divulge e explore produtos e ofertas dentro da plataforma.</p>
            <p class="text">Funcionalidades:</p>
            <ul>
              <li class="text">
                Cadastro de modelos de equipamentos;
              </li>
              <li class="text">
                Controle de disponibilidade;
              </li>
              <li class="text">
                Publicação de ofertas;
              </li>
              <li class="text">
                Consulta de produtos por empresas interessadas;
              </li>
              <li class="text">
                Negociação entre fornecedores e demandantes.
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>


<section id="secao3">
  <div class="grid-container">
    <div>
      <div class="card-imagem-secao3" id="gerenciamento-secao3">
        <h2 class="title">Gerenciamento</h2></div>
      <div class="card-texto-secao3">
        <p class="text">Gerencie, de forma centralizada, todas as unidades e demandas da sua empresa
          com controle e agilidade.</p>
      </div>
    </div>
    <div>
      <div class="card-imagem-secao3" id="servicos-secao3">
        <h2 class="title">Serviços técnicos</h2>
      </div>
      <div class="card-texto-secao3">
        <p class="text">Solicite serviços técnicos especializados a profissionais qualificados do
          setor fotovoltaico, com rapidez e segurança.</p>
      </div>
    </div>
    <div>
      <div class="card-imagem-secao3" id="catalogo-secao3">
        <h2 class="title">Catálogo</h2>
      </div>
      <div class="card-texto-secao3">
        <p class="text">Consulte produtos e ofertas de fornecedores especializados do setor
          fotovoltaico, com praticidade e agilidade.</p>
      </div>
    </div>
  </div>
</section>

<section id="secao4">
  <div class="frase_secao1">
    <h2 class="title">Transforme sua <span class="accent">economia</span> com energia solar!</h2>
  </div>
  <div class="carrossel">
    <div class="vida_util slide_ativo">
      <img src="${pageContext.request.contextPath}/assets/img/vida_util.jpg"
           alt="Foto de painéis solares." class="img_carrossel">
      <div class="infos_carrossel">
        <h3 class="sub_title">Vida útil</h3>
        <h4 class="text">Conte com painéis solares que possuem vida útil média de aproximadamente
          trinta anos.</h4>
      </div>
    </div>

    <div class="payback slide">
      <img src="${pageContext.request.contextPath}/assets/img/payback.jpg"
           alt="Imagem de uma pessoa guardando dinheiro" class="img_carrossel">
      <div class="infos_carrossel">
        <h3 class="sub_title">Payback</h3>
        <h4 class="text">Obtenha retorno financeiro em prazo determinado, calculado a partir da
          instalação do painel solar.</h4>
      </div>
    </div>

    <div class="sustentavel slide">
      <img src="${pageContext.request.contextPath}/assets/img/sustentavel.jpg"
           alt="Pessoas andando por uma paisagem florida." class="img_carrossel">
      <div class="infos_carrossel">
        <h3 class="sub_title">Sustentável</h3>
        <h4 class="text">Promova o uso equilibrado e responsável dos recursos naturais.</h4>
      </div>
    </div>

    <div class="renovavel slide">
      <img src="${pageContext.request.contextPath}/assets/img/renovavel.jpg"
           alt="Fotos de painéis fotovoltaicos, que são um tipo de energia renovável" class="img_O">
      <div class="infos_carrossel">
        <h3 class="sub_title">Renovável</h3>
        <h4 class="text">Utilize energia solar, uma fonte capaz de se regenerar naturalmente.</h4>
      </div>
    </div>
  </div>
  <div class="passagem">
    <button class="quadrado" type="button" role="group"
            aria-label="Ir para o primeiro carrossel"></button>
    <button class="quadrado" type="button" aria-label="Ir para o segundo carrossel"></button>
    <button class="quadrado" type="button" role="group"
            aria-label="Ir para o terceiro carrossel"></button>
    <button class="quadrado" type="button" aria-label="Ir para o quarto carrossel"></button>
  </div>
</section>

<section id="secao5">
  <div id="container_secao5">
    <img src="${pageContext.request.contextPath}/assets/img/img_secao5.jpg">
    <div class="frase_secao5">
      <h2 class="title">Como a energia solar muda a sua vida</h2>
      <div class="titulo_secao5">
        <h3 class="text">
          Em 2023, um sistema fotovoltaico em Franca (SP) com 220 painéis reduziu o gasto mensal de
          energia de <span class="accent_white">R$6.500,00</span> para <span class="accent_white">R$1.600,00.</span>
        </h3>
        <h3 class="text">Gerando economia de <span class="accent_white">R$4.900,00</span> por mês e
          retorno do investimento em cerca de <span class="accent_white">8,5</span> anos.</h3>
        <p class="little_text">Observação: valores baseados no <span class="accent_white">caso específico</span>;
          o retorno pode variar conforme tarifa e perfil de consumo de cada cliente.</p>
      </div>
    </div>
  </div>
</section>

<section id="secao6">
  <div id="container_secao6">
    <div class="texto-secao6">
      <h2 class="big_text">Agora também temos <span class="accent">fornecedores!</span></h2>
      <div>
        <h3 class="sub_title">Ampliamos nossa atuação para gerar ainda mais valor</h3>
        <h4 class="text">Nosso aplicativo abrange:</h4>
      </div>
    </div>
    <div id="atuacoes_fornecedores_secao6">
      <div class="text">
        <div class="coisas">
          <p class="sub_title">Empresas</p>
          <ul class="lista">
            <li><img src="${pageContext.request.contextPath}/assets/icone/fabrica.svg"> Fábricas
            </li>
            <li><img src="${pageContext.request.contextPath}/assets/icone/globo.svg"> Distribuidores
            </li>
            <li><img src="${pageContext.request.contextPath}/assets/icone/casa.svg"> Revendedores
            </li>
            <li><img src="${pageContext.request.contextPath}/assets/icone/regualoca.svg"> Empresas
              técnicas especializadas
            </li>
          </ul>
        </div>
        <div class="coisas">
          <p class="sub_title">Profissionais</p>
          <ul class="lista">
            <li><img src="${pageContext.request.contextPath}/assets/icone/userCheck.svg">
              Independentes
            </li>
            <li><img src="${pageContext.request.contextPath}/assets/icone/pasta.svg"> De empresas
            </li>
            <li><img src="${pageContext.request.contextPath}/assets/icone/coracao.svg"> Parceiros
            </li>
          </ul>
        </div>
      </div>
      <div class="imagem-atuacao-secao6">
        <img src="${pageContext.request.contextPath}/assets/img/img_fornecedor_secao6.jpeg"
             alt="Imagem de um tecnico eletricista em cima de uma placa solar">
      </div>
    </div>
  </div>
</section>


<section id="secao7">
  <div class="container_secao7">
    <h2 class="big_text">Conheça nossas <span class="accent">funcionalidades!</span></h2>

    <div class="slide_ativo">
      <div class="img_secao7">
        <img src="${pageContext.request.contextPath}/assets/img/celular.png"
             alt="Tela do app Elos mostrando a funcionalidade de gerenciamento de carga horária.">
      </div>

      <div class="infos_secao7">
        <h3 class="title"><span class="accent">Gerenciamento</span> da carga horária</h3>

        <div class="container_frases">

          <p class="little_title">Fornecedores independentes, utilizem a nossa agenda para otimizar
            o seu tempo. Marque os seus <span class="accent">compromissos</span> de acordo com a
            data que você quer, navegando de forma simples pelo calendário, mês a mês.</p>


          <p class="little_title">Você ainda pode personalizar a sua jornada, conectar o <span
                  class="accent">Google Agenda</span> e ajustar as configurações do jeito que
            preferir.</p>

        </div>
      </div>
    </div>

    <div class="slide">
      <div class="img_secao7">
        <img src="${pageContext.request.contextPath}/assets/img/celular1.png"
             alt="Tela do app Elos mostrando a funcionalidade de chat para a organização.">
      </div>

      <div class="infos_secao7">
        <h3 class="title"><span class="accent">Chat</span> para organização</h3>

        <div class="container_frases">

          <p class="little_title">Utilize o chat para marcar visitas técnicas, <span class="accent">negociar</span>,
            falar com clientes e empresas técnicas, tudo em uma conversa simples e direta, <span
                    class="accent">sem sair</span> do aplicativo.</p>

          <p class="little_title">Você pode enviar mensagens, compartilhar links, usar o áudio
            quando preferir e acompanhar o histórico das suas conversas para manter tudo
            organizado.</p>

        </div>
      </div>
    </div>

    <div class="slide">
      <div class="img_secao7">
        <img src="${pageContext.request.contextPath}/assets/img/celular2.png"
             alt="Tela do app Elos mostrando a funcionalidade de seleção de projetos.">
      </div>

      <div class="infos_secao7">
        <h3 class="title"><span class="accent">Seleção</span> de projeto</h3>

        <div class="container_frases">

          <p class="little_title">Fornecedores, selecionem os projetos nos quais você quer
            trabalhar, escolhendo a empresa e o local que são mais<span
                    class="accent"> próximos</span>.</p>


          <p class="little_title">Além de buscar um projeto pelo nome e percorrer a lista de forma
            <span class="accent">simples</span>, vendo a empresa e a região de cada um logo de cara.
          </p>

        </div>
      </div>
    </div>

    <div class="slide">
      <div class="img_secao7">
        <img src="${pageContext.request.contextPath}/assets/img/celular3.png"
             alt="Tela do app Elos mostrando a funcionalidade de localização de projetos próximos">
      </div>

      <div class="infos_secao7">
        <h3 class="title"><span class="accent">Localização</span> de projetos próximos</h3>

        <div class="container_frases">

          <p class="little_title">Através da nossa <span class="accent">busca geográfica</span>, é
            possível que os fornecedores encontrem onde há projetos disponíveis para o seu local de
            trabalho, vendo tudo direto no mapa.</p>


          <p class="little_title">Além de conseguir abrir o endereço no <span class="accent">Google Maps</span>
            ou salvar o local para consultar depois. </p>

        </div>
      </div>
    </div>

    <div class="slide">
      <div class="img_secao7">
        <img src="${pageContext.request.contextPath}/assets/img/celular4.png"
             alt="Tela do app Elos mostrando a funcionalidade do perfil do usuário.">
      </div>

      <div class="infos_secao7">
        <h3 class="title"><span class="accent">Perfil</span> do usuário.</h3>

        <div class="container_frases">

          <p class="little_title">No perfil do usuário, você encontra <span class="accent">informações</span>
            pessoais, além da profissão, e pode adicionar a pessoa como conexão, gerando a sua <span
                    class="accent">confiança</span> antes da negociação.</p>


          <p class="little_title">Além de conseguir ver a agenda, iniciar uma conversa e
            compartilhar o perfil com quem quiser. </p>

        </div>
      </div>
    </div>


    <div class="passagem" role="group" aria-label="Escolha a funcionalidade.">
      <button class="quadrado" type="button" aria-label="Ir para o primeiro carrossel"></button>
      <button class="quadrado" type="button" aria-label="Ir para o segundo carrossel"></button>
      <button class="quadrado" type="button" aria-label="Ir para o terceiro carrossel"></button>
      <button class="quadrado" type="button" aria-label="Ir para o quarto carrossel"></button>
      <button class="quadrado" type="button" aria-label="Ir para o quinto carrossel"></button>
    </div>
  </div>
</section>

<section id="FAQ">
  <div class="container_FAQ">
    <div class="frases_FAQ">
      <h2 class="title">Perguntas frequentes</h2>
      <h3 class="sub_title">Tem perguntas? Nos contate.</h3>
    </div>

    <a class="button_contate_nos" href="mailto:interdisciplinar.elos@gmail.com">
      <span class="text">Contate-nos</span>
    </a>

    <div class="container_infos">

      <div class="quadrado_info acordeao_item">
        <button class="acordeao_button" type="button" id="btn_faq_1" aria-expanded="false"
                aria-controle="resp_faq_1">
          <img src="${pageContext.request.contextPath}/assets/icone/pergunta.svg"
               alt="Icone de pergunta">
          <span class="sub_title">O que é o Elos?</span>
          <img src="${pageContext.request.contextPath}/assets/icone/seta_menor.svg"
               alt="Icone de seta">
        </button>
        <div class="acordeao_painel" id="resp_faq_1" role="region" aria-labelledby="btn-faq-1">
          <div class="acordeao_conteudo">
            <p class="text">Elos é uma plataforma B2B que faz um elo entre o comprador e o
              fornecedor com o objetivo de realizar as suas compras com facilidade e
              praticidade.</p>
            <p></p>
          </div>
        </div>
      </div>

      <div class="quadrado_info acordeao_item">
        <button class="acordeao_button" type="button" id="btn_faq_2" aria-expanded="false"
                aria-controle="resp_faq_2">
          <img src="${pageContext.request.contextPath}/assets/icone/pergunta.svg"
               alt="Icone de pergunta">
          <span class="sub_title">Como é o processo de instalação?</span>
          <img src="${pageContext.request.contextPath}/assets/icone/seta_menor.svg"
               alt="Icone de seta">
        </button>
        <div class="acordeao_painel" id="resp_faq_2" role="region" aria-labelledby="btn-faq_2">
          <div class="acordeao_conteudo">
            <p class="text">O processo de instalação possui diversas etapas, sendo elas:
              Verificação se é possível a instalação no local;
              Aprovação da concessionária responsável pela energia da região;
              Instalação dos painéis solares;
              Conexão com o sistema elétrico.</p>
          </div>
        </div>
      </div>


      <div class="quadrado_info acordeao_item">
        <button class="acordeao_button" type="button" id="btn_faq_3" aria-expanded="false"
                aria-controle="resp_faq_3">
          <img src="${pageContext.request.contextPath}/assets/icone/pergunta.svg"
               alt="Icone de pergunta">
          <span class="sub_title">Quais são os tipos de placas solares?</span>
          <img src="${pageContext.request.contextPath}/assets/icone/seta_menor.svg"
               alt="Icone de seta">
        </button>
        <div class="acordeao_painel" id="resp_faq_3" role="region" aria-labelledby="btn-faq_3">
          <div class="acordeao_conteudo">
            <p class="text">Sim, existe mais um tipo de placa solar com diferentes potências,
              preços, materiais de fabricação, resistência, benefícios e desvantagens.</p>
          </div>
        </div>
      </div>

      <div class="quadrado_info acordeao_item">
        <button class="acordeao_button" type="button" id="btn_faq_4" aria-expanded="false"
                aria-controle="resp_faq_4">
          <img src="${pageContext.request.contextPath}/assets/icone/pergunta.svg"
               alt="Icone de pergunta">
          <span class="sub_title">A manutenção tem que ser frequente?</span>
          <img src="${pageContext.request.contextPath}/assets/icone/seta_menor.svg"
               alt="Icone de seta">
        </button>
        <div class="acordeao_painel" id="resp_faq_4" role="region" aria-labelledby="btn-faq_4">
          <div class="acordeao_conteudo">
            <p class="text">A manutenção da placa não precisa ser frequente, porém é necessário
              verificar, ao menos, uma vez por semana.
              Isso garante o funcionamento máximo do painel solar gerando mais payback.</p>
          </div>
        </div>
      </div>

      <div class="quadrado_info acordeao_item">
        <button class="acordeao_button" type="button" id="btn_faq_5" aria-expanded="false"
                aria-controle="resp_faq_5">
          <img src="${pageContext.request.contextPath}/assets/icone/pergunta.svg"
               alt="Icone de pergunta">
          <span class="sub_title">O que o Elos não faz?</span>
          <img src="${pageContext.request.contextPath}/assets/icone/seta_menor.svg"
               alt="Icone de seta">
        </button>
        <div class="acordeao_painel" id="resp_faq_5" role="region" aria-labelledby="btn-faq_5">
          <div class="acordeao_conteudo">
            <p class="text">
              O Elos não faz a venda direta de equipamentos pela plataforma e pelo aplicativo;
              Não fornece para o público residencial;
              Não há o monitoramento de hardware;
              Não possui o controle operacional de sistemas fotovoltaicos.
            </p>
          </div>
        </div>
      </div>
    </div>
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
      <img src="${pageContext.request.contextPath}/assets/icone/logo_laranja.svg">
    </div>

    <div>
      <p class="little_title" id="slogan-footer">Compra, manutenção,<br> históricos, <b>tudo</b>
        aqui.</p>
    </div>

  </div>
  <div class="grid-footer">

    <div class="flex-column gap-1rem">
      <h3 class="sub_title">Sobre</h3>
      <div class="flex-column gap-1rem">
        <a href="${pageContext.request.contextPath}/aboutUs" class="text">Nossa proposta</a>

        <a href="#FAQ" class="text">FAQ</a>
      </div>

    </div>

    <div class="flex-column gap-1rem">
      <h3 class="sub_title">Navegação</h3>
      <div class="flex-column gap-1rem">
        <a href="#hero" class="text">Início</a>

        <a href="${pageContext.request.contextPath}/login" class="text">Portal</a>
      </div>
    </div>

    <div class="flex-column gap-1rem">
      <h3 class="sub_title">Contatos</h3>
      <div class="flex-column gap-1rem">
        <a href="https://www.instagram.com/interdisciplinar.elos/" class="text" id="instagram">Instagram</a>

        <a href="mailto:interdisciplinar.elos@gmail.com" class="text" id="gmail">Gmail</a>
      </div>
    </div>

  </div>
</section>

<script src="${pageContext.request.contextPath}/js/landing.js" defer></script>
</body>
</html>