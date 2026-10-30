const formulario = document.getElementById("formulario-email");
const inputEmail = document.getElementById("email");
const erroEmail = document.getElementById("erro-email");
//Envio do formulario

formulario.addEventListener('invalid', event => {
  event.preventDefault();

  const input = event.target;

  const span = document.getElementById(`erro-${input.name}`);

  span.textContent = 'E-mail inválido';

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

const contextPath = document.querySelector('meta[name="context-path"]').content;
formulario.addEventListener('submit', event => {
  event.preventDefault();

  const dadosFormulario = new FormData(formulario);

  const objetoJs = Object.fromEntries(dadosFormulario);
  const dadosJSON = JSON.stringify(objetoJs);

  const spansErro = document.querySelectorAll('.span-erro');
  const inputs = document.querySelectorAll(".caixa-erro");

  spansErro.forEach(span => span.textContent = '');
  inputs.forEach(input => input.classList.remove("caixa-erro"));

  fetch(formulario.action, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: dadosJSON
  })
    .then(respostaServlet =>{
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok, texto: texto.trim()
        };
      });
    })
    .then(resultado => {
      if(!resultado.ok){
        inputEmail.classList.add('caixa-erro');
        erroEmail.textContent = 'Email inválido';
        return;
      }
      try{
        const dados = JSON.parse(resultado.texto);
        if (dados && dados.email) {
          inputEmail.classList.add('caixa-erro');
          erroEmail.textContent = dados.email;
          return;
        }
      }catch(e){}
      window.location.href = resultado.texto;
    })
    .catch(() => window.location.href = contextPath);
})


