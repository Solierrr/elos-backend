//SEÇÃO 2 =-=-=-=-=-=-=-=---=-=-=-=-=-=-

const ehCelular = window.matchMedia('(pointer: coarse)').matches;

const cardsS2 = document.querySelectorAll('.possibilidades-secao2');

if (ehCelular) {
  cardsS2.forEach((card) => {
    card.addEventListener('click', (e) => {
      e.stopPropagation();

      if (card.classList.contains('pressionado')) {
        card.classList.remove('pressionado');
      } else {

        fecharTodosOsCards();
        card.classList.add('pressionado');
      }
    });
  });

  document.addEventListener('click', () => {
    fecharTodosOsCards();
  });
}

function fecharTodosOsCards() {
  cardsS2.forEach(c => c.classList.remove('pressionado'));
}

//SEÇÃO 3 =-=-=-=-=-=-=-=---=-=-=-=-=-=-
let idCardAtivo;
let tipoMensalidadeAtual = "mensal";

const botoesPlano = document.querySelectorAll(".botao-plano");
const cardsS3 = [
  document.getElementById("card-fornecedor"),
  document.getElementById("card-profissional"),
  document.getElementById("card-cliente")
];

const botaoSwitchMensalidade = document.getElementById("alternarPeriodo");
const textoPeriodo = document.getElementById("texto-mensalidade");
const itensDoPeriodo = document.querySelectorAll("[data-mensal]");

function animarCardsSecao3() {
  const card = document.getElementById(idCardAtivo);

  if (!card || card.hidden) return;

  // Seleciona o card e todos os elementos animados dentro dele
  const elementos = [
    card,
    ...card.querySelectorAll('.reveal')
  ];

  // Primeiro, retira a classe que deixa os elementos visíveis
  elementos.forEach(function (elemento) {
    elemento.classList.remove('visivel');
  });

  // Espera o navegador registrar o estado inicial
  requestAnimationFrame(function () {

    // Espera mais um quadro antes de iniciar a animação
    requestAnimationFrame(function () {

      elementos.forEach(function (elemento) {
        elemento.classList.add('visivel');
      });

    });
  });
}

function mostrarCard(plano) {
  idCardAtivo = "card-" + plano;

  cardsS3.forEach(function (card) {
    if (idCardAtivo === card.id) {
      card.hidden = false;
    } else {
      card.hidden = true;
    }
  });

  animarCardsSecao3();

}

botoesPlano.forEach(function (botao) {
  botao.addEventListener("click", function () {

    mostrarCard(botao.dataset.plano);
    marcarBotaoAtivo(botao.dataset.plano);
  });
});

function marcarBotaoAtivo(planoEscolhido) {
  botoesPlano.forEach(function (botao) {
    if (botao.dataset.plano === planoEscolhido) {
      botao.classList.add("ativo");
      botao.setAttribute("aria-pressed", "true");
    } else {
      botao.classList.remove("ativo");
      botao.setAttribute("aria-pressed", "false");
    }
  });
}

function aplicarPeriodo(periodo) {
  if (periodo === "anual") {
    botaoSwitchMensalidade.classList.add("anual");
    botaoSwitchMensalidade.setAttribute("aria-pressed", "true");
    textoPeriodo.textContent = "Anual";
  } else {
    botaoSwitchMensalidade.classList.remove("anual");
    botaoSwitchMensalidade.setAttribute("aria-pressed", "false");
    textoPeriodo.textContent = "Mensal";
  }

  // 2) preços e textos: troca pelo valor do período escolhido
  itensDoPeriodo.forEach(function (item) {
    if (periodo === "anual") {
      item.textContent = item.dataset.anual;
    } else {
      item.textContent = item.dataset.mensal;
    }
  });
}

// 3) clique: se está no anual vai pro mensal, senão vai pro anual
botaoSwitchMensalidade.addEventListener("click", function () {
  if (botaoSwitchMensalidade.classList.contains("anual")) {
    aplicarPeriodo("mensal");
  } else {
    aplicarPeriodo("anual");
  }

  animarCardsSecao3();

});

//SEÇÃO 4 =-=-=-=-=-=-=-=---=-=-=-=-=-=-

const gridDiferenciais = document.querySelector('.grid-container');
const cardsDiferenciais = document.querySelectorAll('.item-diferenciais');
const contadorAtual = document.querySelector('#contador-atual');
const contadorTotal = document.querySelector('#contador-total');
const mqPilha = window.matchMedia('(max-width: 768px)');

// tem que ser igual ao tempo do transition no CSS ( 0.4s )
const DURACAO_ANIMACAO = 400;

let indiceAtual = 0;

function atualizarPilha() {
  cardsDiferenciais.forEach(function (card, i) {
    card.classList.remove('pos-0');
    card.classList.remove('pos-1');
    card.classList.remove('pos-2');
    card.classList.remove('pos-3');

    if (mqPilha.matches) {
      let posicao = (i - indiceAtual + cardsDiferenciais.length) % cardsDiferenciais.length;

      if (posicao > 3) {
        posicao = 3;
      }

      card.classList.add('pos-' + posicao);
    } else {
      card.classList.remove('saindo');
    }
  });

  contadorAtual.textContent = indiceAtual + 1;
}

function irParaProximo() {
  if (mqPilha.matches) {
    const cardSaindo = cardsDiferenciais[indiceAtual];
    cardSaindo.classList.add('saindo');

    indiceAtual = (indiceAtual + 1) % cardsDiferenciais.length;
    atualizarPilha();

    setTimeout(function () {
      cardSaindo.classList.remove('saindo');
      animando = false;
    }, DURACAO_ANIMACAO);

  }
}

gridDiferenciais.addEventListener('click', irParaProximo);
mqPilha.addEventListener('change', atualizarPilha);

atualizarPilha();

/* ===================== SCROLL ANIMADO ===================== */
(() => { // impede dar conflito com as demais variaveis

  const reduzirMovimento = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  const header = document.querySelector('header');
  let animacaoScroll = null;

  // começa devagar, acelera no meio e desacelera no final
  const easeInOutCubic = (t) =>
    t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;

  // o header é fixo, precisa medir onde ele termina na tela
  function offsetHeader() {
    return header ? header.getBoundingClientRect().bottom + 16 : 0;
  }

  function scrollParaElemento(alvo) {
    cancelAnimationFrame(animacaoScroll); //impede que aconteça conflitos, caso a pessoa aperte 2 scrolls diferentes

    const inicioY = window.scrollY; //ponto de partida, quantos pixeis a tela já rodou
    const destino = Math.max(0, inicioY + alvo.getBoundingClientRect().top - offsetHeader()); //onde a seção está na pagina inteira (- o header)
    const distancia = destino - inicioY; //quantos pixels faltam scrollar, da posição atual até o destino.

    //acessibilidade para quem não quer movimento
    if (reduzirMovimento) {
      window.scrollTo(0, destino);
      return;
    }

    //duração da animação
    const duracao = Math.min(Math.max(Math.abs(distancia) * 0.5, 800), 2000);
    //abs tira o sinal negativo.
    //max escolhe o maior dos dois, ou seja, não pode estar abaixo do número colocad, sendo o minimo.
    //min escolhe o minimo dos dois, ou seja, não pode ultrapassar desse numero, sendo o máximo.

    //variavel modificavel
    let inicioTempo = null;
    //para guardar o horario que a animação começou

    //animação do scroll através dos quadros da tela para descobrir quanto tempo passou
    function passo(agora) {
      if (!inicioTempo) inicioTempo = agora;
      const progresso = Math.min((agora - inicioTempo) / duracao, 1);
      window.scrollTo(0, inicioY + distancia * easeInOutCubic(progresso));
      if (progresso < 1) animacaoScroll = requestAnimationFrame(passo);
    }

    animacaoScroll = requestAnimationFrame(passo);
  }

  //se o usuário mexer no scroll durante a animação, ele assume o controle
  ['wheel', 'touchstart', 'keydown'].forEach((evento) => {
    //wheel = mouse, touchstart = clicar com o dedo, keydown = apertar uma tecla
    window.addEventListener(evento, () => cancelAnimationFrame(animacaoScroll), {passive: true});
    //passive: true avisa o navegador que essa função não bloqueia o scroll, então ele não precisa esperar ela terminar pra rolar a tela.
  });


  //^= começa com: procura todos os <a> cujo href começa com # (id de uma seção).
  document.querySelectorAll('a[href^="#"]').forEach((link) => {
    link.addEventListener('click', (e) => {
      const id = link.getAttribute('href').slice(1);
      const alvo = id && document.getElementById(id);
      if (alvo) {
        e.preventDefault();
        scrollParaElemento(alvo);
      }
    });
  });


  //elementos que vao ser animados
  const seletores = [
    '.fraseHero > h1',
    '.fraseHero > p',
    '.texto-secao1',
    '.desc-secao1 > div > h2',
    '.desc-secao1 > img',
    '.secoes-title',
    '.possibilidades-secao2',
    '.botoes-secao3',
    '#card-fornecedor, #card-profissional, #card-cliente',
    '.coluna-principal > h3',
    '.coluna-principal > p',
    '.coluna-principal > ul > li',
    '.coluna-faixas > ul > li',
    '.plano-profissional > ul > li',
    '.coluna-faixas > p',
    '.grid-container > div',
    '.title-marketing',
    '.button-cadastre-se',
    '#logo-container-footer',
    '.grid-footer > div'
  ];


  //marca os elementos com .reveal
  const contagemPorPai = new Map();
  document.querySelectorAll(seletores.join(',')).forEach((el) => {          //o join junta a lista seletores em um texto só, separado por ,
    el.classList.add('reveal');
    const i = contagemPorPai.get(el.parentElement) ?? 0;
    el.style.setProperty('--i', i);
    contagemPorPai.set(el.parentElement, i + 1);
  });

  //ve quais elementos saem e entram
  const observer = new IntersectionObserver((entradas) => {
    entradas.forEach((entrada) => {
      if (!entrada.isIntersecting) return;

      const el = entrada.target;    //elemento que entrou
      el.classList.add('visivel');
      observer.unobserve(el);   //anima só na primeira vez

    });
  }, {
    threshold: 0.1,     //só vai avisar quando pelo menos 10% do elemento estiver visivel
    rootMargin: '0px 0px -50px 0px'
  });

  document.querySelectorAll('.reveal').forEach((el) => {
    // Não entrega os cards da seção 3 ao observer.
    // Eles serão animados manualmente.
    if (el.closest('#card-fornecedor, #card-profissional, #card-cliente')) {
      return;
    }

    observer.observe(el);
  });

})();

mostrarCard("fornecedor");
marcarBotaoAtivo("fornecedor");
aplicarPeriodo("mensal");