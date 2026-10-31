//Partes relacionadas ao botão da senha:
const botaoSenha = document.getElementById('olho-senha');
const botaoSenhaUpdate = document.getElementById('olho-senha-update');
const inputSenha = document.getElementById('senha');
const inputSenhaUpdate = document.getElementById('senha-update');


if (botaoSenha) {
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
}

if (botaoSenhaUpdate) {

  botaoSenhaUpdate.addEventListener('click', () => {
    const tipo = inputSenhaUpdate.type === 'password' ? 'text' : 'password';

    if (tipo === 'text') {
      botaoSenhaUpdate.src = botaoSenhaUpdate.dataset.urlOlho;
    } else {
      botaoSenhaUpdate.src = botaoSenhaUpdate.dataset.urlOculto;
    }

    inputSenhaUpdate.type = tipo;
  });

}

