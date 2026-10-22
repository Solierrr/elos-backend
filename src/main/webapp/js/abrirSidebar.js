const botaoSideBar = document.getElementById("botao-abrir-sidebar");

const caixaBotaoSideBar = document.getElementById("conteiner-abrir-sidebar");

const sidebar = document.getElementById("side-bar-id");

const conteinerDoConteudo = document.getElementById("conteiner-do-conteudo");

caixaBotaoSideBar.addEventListener('click', () => {

  const botaoAbrir = botaoSideBar.dataset.urlAbrir;
  const botaoFechar = botaoSideBar.dataset.urlFechar;

  const todosSpans = document.querySelectorAll("nav span");
  const caixinhas = document.querySelectorAll(".side-bar> div> a:not(#link-da-logo)");

  if (sidebar.classList.contains("side-bar-aberta")) {
    todosSpans.forEach(span => span.classList.remove("visivel"));
    caixinhas.forEach(caixinha => {
        caixinha.style.justifyContent = "center";
        caixinha.style.paddingLeft = "0";
      }
    )
    sidebar.classList.remove("side-bar-aberta");
    botaoSideBar.src = botaoAbrir;
    conteinerDoConteudo.style.width = "calc(100dvw - 3rem)";
    conteinerDoConteudo.style.marginLeft = "3rem";
  } else {
    sidebar.classList.add("side-bar-aberta");

    todosSpans.forEach(span => span.classList.add("visivel"));
    caixinhas.forEach(caixinha => {
        caixinha.style.justifyContent = "flex-start";
        caixinha.style.paddingLeft = "0.2rem";
      }
    )

    botaoSideBar.src = botaoFechar;
    conteinerDoConteudo.style.width = "calc(100dvw - 10rem)";
    conteinerDoConteudo.style.marginLeft = "10rem";
  }

})