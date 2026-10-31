const cards = document.querySelectorAll('.container');

cards.forEach(card => {
  card.addEventListener('click', () => {
    const selecionado = card.classList.contains('selecionado');
    cards.forEach(crd => crd.classList.remove('selecionado'));
    if (!selecionado) {
      card.classList.add('selecionado');
    }
  });
});

const formulario = document.getElementById("formulario-selecao");
const cardsConteiner = document.getElementById("conteiner-cards");

formulario.addEventListener('submit', event => {
  event.preventDefault();

  const dadosFormulario = new FormData(formulario);
  dadosFormulario.append('tipo-usuario', cardsConteiner.querySelector(".selecionado").id);

  const objetoJs = Object.fromEntries(dadosFormulario);

  const dadosJSON = JSON.stringify(objetoJs);

  fetch(formulario.action, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }, body: dadosJSON
  })
    .then(respostaServlet => {
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok,
          texto: texto
        };
      });
    })
    .then(resultado => {
      if (!resultado.ok) {
        return;
      }
      try {
        const dados = JSON.parse(resultado.texto);
        return;
      } catch (e) {
      }
      window.location.href = resultado.texto;
    })
    .catch(() => window.location.href = contextPath);

});