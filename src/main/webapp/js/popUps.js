// Constantes
const popUpInsert = document.getElementById('pop-up-insert');
const popUpFiltro = document.getElementById('pop-up-filtro');
const popUpUpdate = document.getElementById('pop-up-update');

const botaoInsertEntrar = document.getElementById('insert-entrar');
const botaoFiltroEntrar = document.getElementById('filtro-entrar');

const botaoInsertSair = document.getElementById('insert-sair');
const botaoFiltroSair = document.getElementById('filtro-sair');
const botaoUpdateSair = document.getElementById('update-sair');

// Função auxiliar para fechar todos os modais abertos com segurança
const fecharTodos = () => {
  if (popUpInsert) {
    popUpInsert.close();
  }
  if (popUpFiltro) {
    popUpFiltro.close();
  }
  if (popUpUpdate) {
    popUpUpdate.close();
  }
};

// Abrir os pop-ups
if (botaoInsertEntrar && popUpInsert) {
  botaoInsertEntrar.addEventListener('click', () => {
    fecharTodos();
    popUpInsert.showModal();
  });
}

if (botaoFiltroEntrar && popUpFiltro) {
  botaoFiltroEntrar.addEventListener('click', () => {
    fecharTodos();
    popUpFiltro.showModal();
  });
}

// Funcionalidades de fechar os pop-ups
if (popUpInsert && botaoInsertSair) {
  botaoInsertSair.addEventListener('click', () => popUpInsert.close());
}
if (popUpFiltro && botaoFiltroSair) {
  botaoFiltroSair.addEventListener('click', () => popUpFiltro.close());
}

if (popUpUpdate && botaoUpdateSair) {

  formularioUpdate = document.getElementById('formulario');
  const todosInputs = formularioUpdate.querySelectorAll(
    'input, select, textarea');
  todosInputs.forEach(input => {
    input.classList.remove('caixa-erro');

    const spanErro = formularioUpdate.querySelector(`#erro-${input.name}`);
    if (spanErro) {
      spanErro.textContent = '';
    }
  });

  formularioUpdate.reset();

  botaoUpdateSair.addEventListener('click', () => popUpUpdate.close());
}

const corpoTabelaDelegado = document.querySelector('#caixa-tabela table tbody');
const formularioPegarDadosUpdate = document.getElementById(
    'formulario-update-dados');

corpoTabelaDelegado.addEventListener('click', event => {
  const botaoEditar = event.target.closest('.editar');
  if (botaoEditar) {
    fecharTodos();
    const id = botaoEditar.dataset.id;
    preencherFormularioUpdate(id);
    popUpUpdate.showModal();
  }
});

//Coleta dos dados do pop-up do update
function preencherFormularioUpdate(id) {

  const url = `${formularioPegarDadosUpdate.action}?id=${encodeURIComponent(
      id)}`;

  fetch(url, {
    method: 'GET'
  })
  .then(respostaServlet => {
    return respostaServlet.text().then(texto => {
      return {
        ok: respostaServlet.ok, estaVazio: texto.trim() === '', texto: texto
      };
    })
  })
  .then(resposta => {
    if (resposta.estaVazio) {
      alert("Algo inesperado aconteceu tente novamente");
      return;
    }

    const dados = JSON.parse(resposta.texto)[0];

    Object.keys(dados).forEach(campo => {

      const elemento = document.getElementById(`${porSneakCase(campo)}-update`)

      if (elemento) {
        if (elemento.tagName === 'DIV') {
          elemento.textContent = dados[campo];
        } else {
          elemento.value = dados[campo];
        }
      }
    })
  })
  .catch(erro => {
    console.error('Erro ao processar requisição:', erro);
  });

}

function porSneakCase(texto) {
  return texto.replace(/_/g, '-').toLowerCase();
}