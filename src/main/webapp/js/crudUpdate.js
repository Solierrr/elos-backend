const formularioUpdate = document.getElementById("formulario-update");
const popUpUpdate = document.getElementById('pop-up-update');

formularioUpdate.addEventListener('submit', (event) => {
  event.preventDefault();

  const todosInputs = formularioUpdate.querySelectorAll(
    'input, select, textarea');
  todosInputs.forEach(input => {
    input.classList.remove('caixa-erro');

    const spanErro = formularioUpdate.querySelector(`#erro-${input.name}`);
    if (spanErro) {
      spanErro.textContent = '';
    }
  });

  const dadosFormulario = new FormData(formularioUpdate);

  const objetoJs = Object.fromEntries(dadosFormulario);

  const dadosJSON = JSON.stringify(objetoJs);

  fetch(formularioUpdate.action, {
    method: 'POST', headers: {
      'Content-Type': 'application/json'
    }, body: dadosJSON
  })
    .then(respostaServlet => {
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok, texto: texto
        };
      });
    })
    .then(resultado => {
      const errosNoServlet = JSON.parse(resultado.texto || '{}');
      const temErros = Object.keys(errosNoServlet).length > 0;

      console.log(errosNoServlet);

      if (resultado.ok && !temErros) {
        alert(`Registro atualizado com sucesso!`);
        formularioUpdate.reset();
        popUpUpdate.close();
        enviarFiltro();
      } else {
        Object.entries(errosNoServlet).forEach(
          ([nomeDoCampo, mensagemDeErro]) => {
            const inputDoForm = formularioUpdate.querySelector(
              `[name="${nomeDoCampo}-update"]`);

            if (inputDoForm) {
              inputDoForm.classList.add('caixa-erro');

              const spanErro = formularioUpdate.querySelector(
                `#erro-${nomeDoCampo}-update`);
              if (spanErro) {
                spanErro.textContent = mensagemDeErro;
              }
            }
          });
      }
    })
    .catch(erro => {
      console.error('Erro ao processar requisição:', erro);
      alert(`Ocorreu um erro inesperado ao atualizar o cadastro. Tente novamente.`);
    });
});

formularioUpdate.addEventListener('invalid', event => {
  event.preventDefault();

  const input = event.target;

  const span = document.getElementById(`erro-${input.name}`);

  const chave = span.id.replace(/-update/g, "")

  span.textContent = mensagensInsertSpans[chave];

  input.classList.add("caixa-erro");

}, true);