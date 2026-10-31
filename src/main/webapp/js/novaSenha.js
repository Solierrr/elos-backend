//Partes relacionadas ao botão da senha
const botaoSenha = document.getElementById('olho-senha');
const inputSenha = document.getElementById('input-senha');

const botaoSenhaConfirmacao = document.getElementById('olho-senha-confirmacao');
const inputSenhaConfirmacao = document.getElementById('input-senha-confirmacao');

const contextPath = document.querySelector('meta[name="context-path"]').content;

//Botão de mostrar a senha
botaoSenha.addEventListener('click', () => {
  const tipo = inputSenha.type === 'password' ? 'text' : 'password';

  if (tipo === 'text') {
    botaoSenha.src = botaoSenha.dataset.urlOlho;
  } else {
    botaoSenha.src = botaoSenha.dataset.urlOculto;
  }

  inputSenha.type = tipo;
});

botaoSenhaConfirmacao.addEventListener('click', () => {
  const tipo = inputSenhaConfirmacao.type === 'password' ? 'text' : 'password';

  if (tipo === 'text') {
    botaoSenhaConfirmacao.src = botaoSenhaConfirmacao.dataset.urlOlho;
  } else {
    botaoSenhaConfirmacao.src = botaoSenhaConfirmacao.dataset.urlOculto;
  }

  inputSenhaConfirmacao.type = tipo;
});

//Envio do formulario
const formulario = document.getElementById('formulario-senha');

formulario.addEventListener('invalid', event => {
  event.preventDefault();

  const input = event.target;

  const span = document.getElementById(`erro-${input.name}`);

  span.textContent = 'A senha está fraca';


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

const senhaInput = document.getElementById('input-senha');
const erroSenhaInput = document.getElementById('erro-senha');

const senhaConfirmacaoInput = document.getElementById('input-senha-confirmacao');
const erroSenhaConfirmacaoInput = document.getElementById('erro-senha-confirmacao');

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
    .then(respostaServlet => {
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok, texto: texto.trim()
        };
      });
    })
    .then(resultado => {
      if (!resultado.ok) {
        inputSenha.classList.add('caixa-erro');
        erroSenhaInput.textContent = 'Senha inválida';
        return;
      }
      try {
        const dados = JSON.parse(resultado.texto);
        if (dados && dados["senha"]) {
          inputSenha.classList.add('caixa-erro');
          erroSenhaInput.textContent = dados["senha"];
          return;
        } else if (dados && dados["senha-confirmacao"]) {
          inputSenhaConfirmacao.classList.add('caixa-erro');
          erroSenhaConfirmacaoInput.textContent = dados["senha-confirmacao"];
          return;
        } else {
          inputSenhaConfirmacao.classList.add('caixa-erro');
          erroSenhaConfirmacaoInput.textContent = "Algo inesperado aconteceu tente novamente";
        }
      } catch (e) {
      }
      alert("Senha alterada com sucesso!");
      window.location.href = resultado.texto;
    }).catch(() => window.location.href = contextPath);
})


