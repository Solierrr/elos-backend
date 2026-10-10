//Partes relacionadas ao botão da senha:
const botaoSenha = document.getElementById('olho-senha');
const inputSenha = document.getElementById('input-senha');

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


//Envio do formulario
const formulario = document.getElementById('formulario-login');
const erroSenha = document.getElementById('erro-senha');

formulario.addEventListener('invalid', event => {
    event.preventDefault();



    const input = event.target;

    const span = document.getElementById(`erro-${input.name}`);

    if(input.name === 'email')
        span.textContent = 'E-mail inválido';
    else
        span.textContent = 'Senha inválida';


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

formulario.addEventListener('submit', (event) =>{
    event.preventDefault();

    const dadosFormulario = new FormData(formulario);
    dadosFormulario.append('userAgent', navigator.userAgent);

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
                ok: respostaServlet.ok,
                status: respostaServlet.status,
                texto: texto
            };
        });
    })
    .then(resultado => {
        if(resultado.status === 401){
            inputSenha.classList.add('caixa-erro');
            inputSenha.value = '';
            erroSenha.textContent = 'Senha incorreta';
            return;
        }
        if (!resultado.ok) {
            alert('Erro no servidor. Tente novamente.');
            return;
        }
        window.location.href = resultado.texto;
    }).catch(() => alert('Não foi possível conectar ao servidor.'));
});