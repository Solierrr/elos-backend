//Envio do formulario
const formulario = document.getElementById('formulario-cadastro');
formulario.addEventListener('invalid', event => {
  event.preventDefault();

  const input = event.target;

  input.classList.add("caixa-erro");

}, true);

const esperar = (ms) => new Promise(resolve => setTimeout(resolve, ms));

formulario.addEventListener('input', async event => {
  const input = event.target;
  const span = document.getElementById(`erro-${input.name}`);

  await esperar(100);
  if (input.checkValidity()) {
    input.classList.remove('caixa-erro');
    if (span) span.textContent = '';
  }
});

formulario.addEventListener('submit', (event) => {
  event.preventDefault();

  const todosInputs = formulario.querySelectorAll('input, select, textarea');
  todosInputs.forEach(input => {
    input.classList.remove('caixa-erro');

    const spanErro = formulario.querySelector(`#erro-${input.name}`);
    if (spanErro) {
      spanErro.textContent = '';
    }
  });

  const dadosFormulario = new FormData(formulario);

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
      try {
        const errosNoServlet = JSON.parse(resultado.texto || '{}');

        console.log(errosNoServlet);

        Object.entries(errosNoServlet).forEach(([nomeDoCampo, mensagemDeErro]) => {

          const inputDoForm = formulario.querySelector(`[name="${nomeDoCampo}"]`);

          if (inputDoForm) {
            inputDoForm.classList.add('caixa-erro');

            const spanErro = formulario.querySelector(`#erro-${nomeDoCampo}`);
            if (spanErro) {
              spanErro.textContent = mensagemDeErro;
            }
          }
        });
      } catch (error) {
        window.location.href = resultado.texto;
      }
    })
    .catch(erro => {
      console.error('Erro ao processar requisição:', erro);
      window.location.href = contextPath;
    });
});