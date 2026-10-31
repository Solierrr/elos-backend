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
    '.frase_hero',
    '.frase_secao1',
    '.frase_secao2',
    '.informacao1_secao2',
    '.informacao2_secao2',
    '.informacao3_secao2',
    '.grid-container > div',
    '.frase_secao5',
    '#container_secao5 > img',
    '.texto-secao6',
    '.janela-secao6',
    '.imagem-atuacao-secao6',
    '.container_secao7 > h2',
    '.frases_FAQ',
    '.quadrado_info',
    '.title-marketing',
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

      //quando a animação termina, tira as classes e devolve o elemento ao normal
      el.addEventListener('transitionend', function limpar(e) {
        if (e.target !== el || e.propertyName !== 'opacity') return;
        el.classList.remove('reveal', 'visivel');
        el.style.removeProperty('--i');
        el.removeEventListener('transitionend', limpar);
      });
    });
  }, {
    threshold: 0.1,     //só vai avisar quando pelo menos 10% do elemento estiver visivel
    rootMargin: '0px 0px -50px 0px'
  });

  //entrega ao observer a lista de elementos
  document.querySelectorAll('.reveal').forEach((el) => observer.observe(el));

})();

/*=============================== Carrosséis (seção 1 e 4) ===============================*/
(function () {
  "use strict"; //o JS avisa erros

  //acessibilidade, o caso o usuário queira menos movimento
  const prefereMenosMovimento = window.matchMedia("(prefers-reduced-motion: reduce)");

  //seletorSecao: qual seção
  //tempoEntreTrocas: ms entre as trocas
  //cardsPorPaginaDesktop: quantos cards por página a partir de 768px
  //tempoSaida: ms da animação de saída (igual ao tempo do CSS)
  function criarCarrossel(seletorSecao, tempoEntreTrocas, cardsPorPaginaDesktop = 1, tempoSaida = 400) {
    const secaoCarrossel = document.querySelector(seletorSecao); //acha a seção
    if (!secaoCarrossel) return; //se não achar a seção, vai retornar nada.

    const listaCards = secaoCarrossel.querySelectorAll(".slide, .slide_ativo");
    const listaBotoes = secaoCarrossel.querySelectorAll(".quadrado");
    const mediaTelaGrande = window.matchMedia("(min-width: 768px)");

    //acessibilidade, fica sem animação
    const esperaDaSaida = prefereMenosMovimento.matches ? 0 : tempoSaida;

    //variaveis modificaveis ao longo do codigo
    let paginaAtual = 0;
    let relogioAutoplay = null; //guarda o relógio que passa sozinho
    let relogioSaida = null;    //guarda o relógio da animação de saída

    //quantos cards cabem por página.
    function cardsPorPagina() {
      return mediaTelaGrande.matches ? cardsPorPaginaDesktop : 1;
      //se a tela for grande, como no desktop, o número de cards é 2, se não for grande, como no celular, é apenas 1.
    }

    //quantas páginas tem agora:
    function totalDePaginas() {
      return Math.ceil(listaCards.length / cardsPorPagina());
      //tem um cálculo para definir quants cards terá no total, 4(quantidade total)/2(quantidade dependendo do tamanho da tela) = 2, ou seja, no desktop terá 2 cards.
    }

    //mostra só os cards da página escolhida
    function exibirPagina(pagina) {
      paginaAtual = pagina;
      const quantidade = cardsPorPagina();

      listaCards.forEach((card, posicao) => {
        //vai ver qual é a posição do card agora
        const estaNaPagina = Math.floor(posicao / quantidade) === pagina;
        //cálculo para descobrir a posição, é importante pois no celular fica com 4 botões, ou seja, 4 cards diferentes.

        card.classList.remove("saindo"); //limpeza
        card.classList.toggle("slide_ativo", estaNaPagina);//está na página
        card.classList.toggle("slide", !estaNaPagina);//não está na página
      });
    }

    //deixa os botões accent
    function pintarBotao(pagina) {
      const total = totalDePaginas();

      listaBotoes.forEach((botao, posicao) => {
        botao.hidden = posicao >= total; //vai esconder o botão se a posição for menor do que o total.
        botao.classList.toggle("ativo", posicao === pagina); //ativa a cor laranja
        botao.setAttribute("aria-current", posicao === pagina ? "true" : "false");
      });

    }

    //animação
    function irParaPagina(pagina) {
      clearTimeout(relogioSaida); //impede cliques rápidos

      if (pagina === paginaAtual) {
        secaoCarrossel.querySelectorAll(".saindo").forEach((card) => {
          card.classList.remove("saindo");
        });
        pintarBotao(pagina);
        return; //cancela a animação em cliques rápidos.
      }

      pintarBotao(pagina);
      secaoCarrossel.querySelectorAll(".slide_ativo").forEach((card) => {
        card.classList.add("saindo");
      });

      relogioSaida = setTimeout(() => exibirPagina(pagina), esperaDaSaida);
    }

    function avancarPagina() {
      irParaPagina((paginaAtual + 1) % totalDePaginas());
    }

    function ligarAutoplay() {
      desligarAutoplay();
      if (prefereMenosMovimento.matches) return;
      relogioAutoplay = setInterval(avancarPagina, tempoEntreTrocas);
    }

    function desligarAutoplay() {
      clearInterval(relogioAutoplay);
    }

    listaBotoes.forEach((botao, posicao) => {
      botao.addEventListener("click", () => {
        irParaPagina(posicao);
        ligarAutoplay(); // reinicia a contagem
      });
    });

    // Se a tela cruzar os 768px, o número de páginas muda: recomeça do início
    if (cardsPorPaginaDesktop > 1) {
      mediaTelaGrande.addEventListener("change", () => {
        clearTimeout(relogioSaida);
        exibirPagina(0);
        pintarBotao(0);
        ligarAutoplay();
      });
    }

    // funções definidas
    exibirPagina(0);
    pintarBotao(0);
    ligarAutoplay();
  }

  // Um carrossel por seção
  criarCarrossel("#secao7", 10000);
  criarCarrossel("#secao1", 8000, 2, 400);
  criarCarrossel("#secao4", 8000, 2, 400);
})();
/*=============================== Seção 2 e FAQ ===============================*/
(() => {
  (function () {
    "use strict";

    const listaBotoes = document.querySelectorAll(".acordeao_button");

    listaBotoes.forEach((botao) => {
      botao.addEventListener("click", () => {
        const item = botao.closest(".acordeao_item");
        const secaoDoItem = botao.closest("section");
        if (!item || !secaoDoItem) return;

        const vaiAbrir = !item.classList.contains("aberto");

        secaoDoItem.querySelectorAll(".acordeao_item.aberto").forEach((outro) => {
          outro.classList.remove("aberto");
          outro.querySelector(".acordeao_button").setAttribute("aria-expanded", "false");
        });

        item.classList.toggle("aberto", vaiAbrir);
        botao.setAttribute("aria-expanded", vaiAbrir);
      });
    });
  })();
})();

/*=============================== Seção 7 =============================== */
// Variaveis
const secao = document.querySelector("#secao7");
const slides = secao.querySelectorAll(".slide, .slide_ativo");
const botoes = secao.querySelectorAll(".quadrado");

// Acessibilidade, caso a pessoa queira - movimento
const reduzirMovimento = window.matchMedia("(prefers-reduced-motion: reduce)");

//letra maiuscula é configuração fixa.
const INTERVALO = 10000;
const DURACAO_SAIDA = reduzirMovimento.matches ? 0 : 400; // ?: = se a pessoa pediu menos movimento vai ser 0, se não vai ser 400ms (0,4s).

// variaveis modificaveis ao longo do código.
let atual = 0;
let timer = null;
let saidaTimer = null;


//deixa só o slide do índice recebido visivel, os outros ficam escondidos.
function trocarSlide(indice) {
  atual = indice;

  //percorre todos os slides, porém só o indice fica na tela atual
  slides.forEach((slide, i) => {
    slide.classList.remove("saindo");   // é uma limpeza para não ter animação do slide anterior
    slide.classList.toggle("slide_ativo", i === indice);
    slide.classList.toggle("slide", i !== indice);
  });
}

//deixa a cor do botão em laranja se ele for o slide ativo/atual
function marcarBotao(indice) {
  botoes.forEach((botao, i) => {
    botao.classList.toggle("ativo", i === indice);
    botao.setAttribute("aria-current", i === indice ? "true" : "false");
  });
}

// função de animação
function mostrar(indice) {
  clearTimeout(saidaTimer); //se a pessoa clicar rápido, ele só vai considerar o último slide clicado. Ou seja, não há bug ou tela parada no primeiro botão clicavel, fazer teste para perceber isso.

  if (indice === atual) {
    slides[atual].classList.remove("saindo"); //se clicar no botão do carrossel atual não vai trocar nada e vai cancelar a saida
    marcarBotao(indice);
    return;
  }

  //quando a pessoa trocar de botão
  marcarBotao(indice);
  slides[atual].classList.add("saindo"); //botão laranja na hora e o slide atual vai ganhar o saindo.

  saidaTimer = setTimeout(() => trocarSlide(indice), DURACAO_SAIDA); //vai esperar um tempo para depois trocar o slide.
}

//função de clicar nos botões
function proximo() {
  mostrar((atual + 1) % slides.length);//atual + 1 = próximo slide.
  //pega o resto da divisão, quando estiver no último ele voltará para o primeiro.
}

//função de passar sozinho
function iniciar() {
  parar();
  timer = setInterval(proximo, INTERVALO); //um ciclo infinito que sempre se repete, onde tem o que vai se repetir e de quanto em quanto tempo irá se repetir.
}


//função para parar o carrossel
function parar() {
  clearInterval(timer);
}

//ir para qualquer botão
botoes.forEach((botao, i) => {
  botao.addEventListener("click", () => {
    mostrar(i);
    iniciar(); //reinicia a contagem
  });
});

//definição das funções
trocarSlide(0); //mostra o primeiro slide
marcarBotao(0); //deixa o botão laranja
iniciar();      //deixa o carrossel trocar sozinho

//acessibilidade caso a pessoa não queira movimento.
if (reduzirMovimento.matches) {
  parar(); //desliga o carrossel(autoplay)
  secao.removeEventListener("mouseleave", iniciar);
  secao.removeEventListener("focusout", iniciar);
}