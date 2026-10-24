import {camposValores} from './variacoesFiltros.js';

let camposTotais;
let camposDisponiveis;

document.addEventListener("DOMContentLoaded", () => {
  const elementoNomeArquivo = document.getElementById("nome-do-arquivo");

  if (elementoNomeArquivo) {
    const nomeTela = elementoNomeArquivo.value;

    camposTotais = camposValores(nomeTela);
    camposDisponiveis = camposValores(nomeTela);
  }
});

const botaoAdicionarFiltro = document.getElementById('adicionar-filtro');
const formularioFiltro = document.getElementById('formulario-filtro');

const campoFiltro = document.getElementById('campo-filtro')

//Adiciona os filtros
botaoAdicionarFiltro.addEventListener('click', () => {
  const valorCampoFiltro = campoFiltro.value;

  if (!valorCampoFiltro) {
    return;
  }

  if (valorCampoFiltro.toUpperCase() === 'CAMPO_VAZIO') {
    return;
  }

  const valores = camposDisponiveis[valorCampoFiltro];

  if (!valores) {
    return;
  } else {
    document.querySelector(
      `#campo-filtro option[value="${valorCampoFiltro.toUpperCase()}"]`).remove();
    campoFiltro.value = "CAMPO_VAZIO";
  }

  formularioFiltro.insertAdjacentHTML('beforeend', valores)
});

//Garantem que as opções vão ser removidas e devolvidas corretamente ao select
formularioFiltro.addEventListener('click', event => {
  const botaoRemover = event.target.closest('.remover-filtro');
  if (!botaoRemover) {
    return;
  }

  const caixaFiltro = botaoRemover.closest('.caixa-filtro');
  if (!caixaFiltro) {
    return;
  }

  const campoInterno = caixaFiltro.querySelector('input, select');

  if (campoInterno) {
    const chaveFiltro = campoInterno.name.replace(/-filtro$/, '').replace(/-/g,
      '_').toUpperCase();
    recriarOptionNoSelect(chaveFiltro);
  }

  caixaFiltro.remove();
});

//Recria a option no select na mesma posição em que ela estava
const recriarOptionNoSelect = (chave) => {
  if (campoFiltro.querySelector(`option[value="${chave}"]`)) {
    return;
  }

  const modelo = document.createElement('template');
  modelo.innerHTML = camposTotais[chave];
  const label = modelo.content.querySelector('label').textContent;

  const novaOption = document.createElement('option');
  novaOption.value = chave;
  novaOption.textContent = label;

  const chavesEmOrdem = Object.keys(camposTotais);
  const indiceAlvo = chavesEmOrdem.indexOf(chave);

  let optionDeReferencia = null;
  for (let i = indiceAlvo + 1; i < chavesEmOrdem.length; i++) {
    optionDeReferencia = campoFiltro.querySelector(
      `option[value="${chavesEmOrdem[i]}"]`);
    if (optionDeReferencia) {
      break;
    }
  }

  if (optionDeReferencia) {
    campoFiltro.insertBefore(novaOption, optionDeReferencia);
  } else {
    campoFiltro.appendChild(novaOption);
  }
};

const popUpFiltro = document.getElementById("pop-up-filtro");
const inputFiltroRapido = document.getElementById("input-busca-rapida");

//Função para enviar o filtro
const enviarFiltro = () => {
  const todosInputs = formularioFiltro.querySelectorAll(
    'input, select, textarea');
  todosInputs.forEach(input => {
    input.classList.remove('caixa-erro');

    const nomeBase = input.name.replace(/-filtro$/, '');
    const spanErro = formularioFiltro.querySelector(`#filtro-erro-${nomeBase}`);
    if (spanErro) {
      spanErro.textContent = '';
    }
  });

  const dadosFormulario = new FormData(formularioFiltro);
  dadosFormulario.append("filtro-rapido", inputFiltroRapido.value);

  const objetoJs = Object.fromEntries(dadosFormulario);
  const dadosJSON = JSON.stringify(objetoJs);

  fetch(formularioFiltro.action, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }, body: dadosJSON
  })
    .then(respostaServlet => {
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok, estaVazio: texto.trim() === '', texto: texto
        };
      });
    })
    .then(resultado => {
      if (resultado.ok && resultado.estaVazio) {
        popUpFiltro.close();
        return;
      }

      if (resultado.estaVazio) {
        return;
      }

      const dadosResposta = JSON.parse(resultado.texto);

      if (Array.isArray(dadosResposta.valores)) {
        preencherTabela(dadosResposta.valores);
      } else if (Object.keys(dadosResposta).length === 0) {
        alert('Nenhum registro encontrado.');
      } else {
        preencherErros(dadosResposta);
      }
    })
    .catch(erro => {
      console.error('Erro ao processar requisição:', erro);
    });
};

//Chamada do filtro quando enviada
formularioFiltro.addEventListener("submit", event => {
  event.preventDefault();
  enviarFiltro();
});

//Garantia de envio do formulario filtro usando o proprio botão
const botaoEnviarFiltro = document.getElementById("enviar-filtro");
botaoEnviarFiltro.addEventListener("click", () => {
  enviarFiltro()
})

// Dispara o envio automaticamente ao carregar a página,
enviarFiltro();

const formularioFiltroBuscaRapida = document.getElementById("formulario-filtro-rapido")

formularioFiltroBuscaRapida.addEventListener("submit", event => {
  event.preventDefault();

  enviarFiltro();
})

// Preenche os spans de erro de cada campo do formulário
const preencherErros = (errosNoServlet) => {
  Object.entries(errosNoServlet).forEach(([nomeDoCampo, mensagemDeErro]) => {
    const inputDoForm = formularioFiltro.querySelector(
      `[name="${nomeDoCampo}-filtro"]`);

    if (inputDoForm) {
      inputDoForm.classList.add('caixa-erro');

      const spanErro = formularioFiltro.querySelector(
        `#filtro-erro-${nomeDoCampo}`);
      if (spanErro) {
        spanErro.textContent = mensagemDeErro;
      }
    }
  });
};

const imagensTabela = document.getElementById("imagens-tabela");
const formularioDelete = document.getElementById("formulario-delete");

const variacoesCabecalho = {
  "id": "ID",
  "email": "E-mail",
  "nome": "Nome",
  "tipo_usuario": "Tipo de usuário",
  "raio_procura_km": "Raio de procura (km)",
  "id_usuario": "ID do usuário",
  "id_profissional": "ID do profissional",
  "profissao": "Profissão",
  "cpf": "CPF",
  "id_fornecedor": "ID do fornecedor",
  "documento_contrato": "Documento de contrato",
  "tipo_fornecedor": "Tipo do fornecedor",
  "cnpj": "CNPJ",
  "razao_social": "Razão social",
  "id_empresa_demandante": "ID da empresa demandante",
  "eh_mandante": "É uma filial?",
  "telefone": "Número do telefone",
  "tipo": "Tipo do telefone",
  "principal": "É o telefone principal?",
  "id_telefone": "ID do telefone",
  "estado": "Estado",
  "id_endereco": "ID do endereço",
  "cidade": "Cidade",
  "bairro": "Bairro",
  "cep": "CEP",
  "logradouro": "Logradouro",
  "numero": "Número",
  "complemento": "Complemento",
  "id_admin": "ID do admin",
  "funcao": "Função do admin",
  "em_atividade": "O admin está ativo?",
  "id_historico_alteracoes": "ID do histórico de alteração",
  "id_registro": "ID do registro",
  "tabela_modificada": "Tipo do dado modificado",
  "acao": "Ação realizada",
  "dados_antigos": "Dados antigos",
  "dados_atualizados": "Dados atualizados",
  "data_alteracao": "Data de alteração",
  "id_log_acessos_admin": "ID do acesso do admin",
  "data_acesso": "Data do acesso",
  "sucesso": "Sucesso?",
  "endereco_ip": "Endereço de IP do acesso",
  "motivo_falha": "Motivo da falha",
  "navegador": "Navegador utilizado",
  "sistema_operacional": "Sistema operacional utilizado"
}

// Preenche o cabeçalho e o corpo da tabela dinamicamente,
// a partir das chaves presentes nos objetos de "valores"
const preencherTabela = (valores) => {
  const tabela = document.querySelector('#caixa-tabela table');
  const cabecalho = tabela.querySelector('thead tr');
  const corpoTabela = tabela.querySelector('tbody');

  if (!tabela || !cabecalho || !corpoTabela) {
    return;
  }

  corpoTabela.innerHTML = '';
  cabecalho.innerHTML = '';

  if (!Array.isArray(valores) || valores.length === 0) {
    return;
  }

  // Chaves do primeiro objeto definem as colunas
  const chaves = Object.keys(valores[0]);

  // Monta o cabeçalho
  chaves.forEach(chave => {
    const th = document.createElement('th');
    th.className = 'littleText';
    th.textContent = variacoesCabecalho[[chave]];
    cabecalho.appendChild(th);
  });

  if (formularioDelete) {
    const thAcoes = document.createElement('th');
    thAcoes.className = 'littleText';
    thAcoes.textContent = 'Ações';
    cabecalho.appendChild(thAcoes);
  }

  // Monta as linhas
  valores.forEach(item => {
    const linha = document.createElement('tr');

    let naoAtivo;

    chaves.forEach(chave => {
      const td = document.createElement('td');
      td.className = 'littleText';
      td.textContent = item[chave] ?? '';
      linha.appendChild(td);

      if (chave === 'em_atividade' && (item[chave].toUpperCase() === 'NÃO' || item[chave].toUpperCase() === 'NAO')) {
        naoAtivo = true;
      } else {
        naoAtivo = false
      }
    });

    if (formularioDelete) {
      const tdAcoes = document.createElement('td');
      tdAcoes.className = 'littleText';

      const botaoExcluir = naoAtivo ? `<button class="excluir botao-ligar-verde" data-id="${item.id
        ?? ''}"><img src="${imagensTabela.dataset.urlApagar}" alt="apagar"></button>` :
        `<button class="excluir" data-id="${item.id
        ?? ''}"><img src="${imagensTabela.dataset.urlApagar}" alt="apagar"></button>`;


      tdAcoes.innerHTML = `
            <div class="acoes-div">
            
            <button class="editar" data-id="${item.id ?? ''}"><img src="${imagensTabela.dataset.urlEditar}" alt="editar"></button>
            ${botaoExcluir}
            
            </div>
        `;
      linha.appendChild(tdAcoes);
    }

    corpoTabela.appendChild(linha);
  });
};

export {enviarFiltro};