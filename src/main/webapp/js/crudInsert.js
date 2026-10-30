import {mensagensSpans} from './mensagensSpans.js';
import {enviarFiltro} from "./crudRead.js";

const contextPath = document.querySelector('meta[name="context-path"]').content;

const formularioInsert = document.getElementById('formulario-insert');

//Manipulação de erros e envio do formulario de insert
const mensagensInsertSpans = mensagensSpans(document.getElementById("nome-do-arquivo").value);

formularioInsert.addEventListener('invalid', event => {
  event.preventDefault();

  const input = event.target;

  const span = document.getElementById(`erro-${input.name}`);

  span.textContent = mensagensInsertSpans[span.id];

  input.classList.add("caixa-erro");

}, true);

const esperar = (ms) => new Promise(resolve => setTimeout(resolve, ms));
formularioInsert.addEventListener('input', async event => {
  const input = event.target;
  const span = document.getElementById(`erro-${input.name}`);

  await esperar(100);
  if (input.checkValidity()) {
    input.classList.remove('caixa-erro');
    if (span) {
      span.textContent = '';
    }
  }
});

//Envio do formulario
formularioInsert.addEventListener('submit', (event) => {
  event.preventDefault();

  const todosInputs = formularioInsert.querySelectorAll('input, select, textarea');
  todosInputs.forEach(input => {
    input.classList.remove('caixa-erro');

    const spanErro = formularioInsert.querySelector(`#erro-${input.name}`);
    if (spanErro) {
      spanErro.textContent = '';
    }
  });

  const dadosFormulario = new FormData(formularioInsert);
  dadosFormulario.delete('trabalha-para-empresa');

  const objetoJs = Object.fromEntries(dadosFormulario);

  const dadosJSON = JSON.stringify(objetoJs);

  fetch(formularioInsert.action, {
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
      const errosNoServlet = JSON.parse(resultado.texto || '{}');
      const temErros = Object.keys(errosNoServlet).length > 0;

      console.log(errosNoServlet);

      if (resultado.ok && !temErros) {
        alert('Cadastro realizado com sucesso!');
        formularioInsert.reset();
        if(document.getElementById("dados-mutaveis")) {
          document.getElementById("dados-mutaveis").innerHTML = "";
        }
        enviarFiltro();
      } else {
        Object.entries(errosNoServlet).forEach(([nomeDoCampo, mensagemDeErro]) => {

          const inputDoForm = formularioInsert.querySelector(`[name="${nomeDoCampo}"]`);

          if (inputDoForm) {
            inputDoForm.classList.add('caixa-erro');

            const spanErro = formularioInsert.querySelector(`#erro-${nomeDoCampo}`);
            if (spanErro) {
              spanErro.textContent = mensagemDeErro;
            }
          }
        });
      }
    })
    .catch(erro => {
      window.location.href = contextPath;
    });
});
