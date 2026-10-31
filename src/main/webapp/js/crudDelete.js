import {enviarFiltro} from "./crudRead.js";

const formularioDelete = document.getElementById("formulario-delete");
const corpoTabelaGlobal = document.querySelector('#caixa-tabela table tbody');
const contextPath = document.querySelector('meta[name="context-path"]').content;

corpoTabelaGlobal.addEventListener("click", event => {
  const botao = event.target.closest('.excluir');
  if (!botao) {
    return;
  }

  const id = botao.dataset.id;

  const dadoFormulario = new FormData();
  dadoFormulario.append('id', id);

  const objetoJs = Object.fromEntries(dadoFormulario);
  const dadosJSON = JSON.stringify(objetoJs);

  fetch(formularioDelete.action, {
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
      if (resultado.texto.length === 2) {
        if (document.getElementById("nome-do-arquivo").value === "admin") {
          alert("Status do admin atualizado com sucesso!");
        } else {
          alert("Exclusão feita com sucesso!");
        }
        enviarFiltro();
      } else {
        if (document.getElementById("nome-do-arquivo").value === "admin") {
          alert("Erro ao atualizar o status do admin, tente novamente!");
        } else {
          alert("Erro na exclusão, tente novamente!");
        }
      }
    })
    .catch(erro => {
      window.location.href = contextPath;
    });
});