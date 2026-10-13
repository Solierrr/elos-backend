import { mensagensSpans } from './mensagensSpans.js';

const formularioInsert = document.getElementById('formulario-insert');

//Manipulação de erros e envio do formulario de insert
const mensagensInsertSpans = mensagensSpans(document.getElementById("nome-do-arquivo").value) ;

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
        if (span) span.textContent = '';
    }
});

formularioInsert.addEventListener('submit', (event) =>{
    event.preventDefault();

    const todosInputs = formularioInsert.querySelectorAll('input, select, textarea');
    todosInputs.forEach(input => {
        input.classList.remove('caixa-erro'); // Remove a classe de erro se existir
        
        // Se você colocou span com texto de erro embaixo, pode limpá-lo também:
        const spanErro = formularioInsert.querySelector(`#erro-${input.name}`);
        if (spanErro){ spanErro.textContent = '';}
    });

    const dadosFormulario = new FormData(formularioInsert);
    dadosFormulario.delete('trabalha-para-empresa');

    const objetoJs = Object.fromEntries(dadosFormulario);

    const dadosJSON = JSON.stringify(objetoJs);

    fetch(formularioInsert.action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: dadosJSON
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
                alert('Usuário cadastrado com sucesso!');
                formularioInsert.reset();
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
            console.error('Erro ao processar requisição:', erro);
            alert('Ocorreu um erro inesperado ao cadastrar o usuário. Tente novamente.');
        });
});

import { camposValores } from './variacoesFiltros.js'; 

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

    if(!valorCampoFiltro){return;}

    if(valorCampoFiltro.toUpperCase() === 'CAMPO_VAZIO'){
        return;
    }

    const valores = camposDisponiveis[valorCampoFiltro];
    
    if(!valores){
        return;
    }else{
        console.log(valorCampoFiltro)
        document.querySelector(`#campo-filtro option[value="${valorCampoFiltro.toUpperCase()}"]`).remove();
        campoFiltro.value = "CAMPO_VAZIO";
    }

    formularioFiltro.insertAdjacentHTML('beforeend', valores)

});

//Garantem que as opções vão ser removidas e devolvidas corretamente ao select
formularioFiltro.addEventListener('click', event => {
    const botaoRemover = event.target.closest('.remover-filtro');
    if(!botaoRemover){return;}

    const caixaFiltro = botaoRemover.closest('.caixa-filtro');
    if(!caixaFiltro){return;}

    const campoInterno = caixaFiltro.querySelector('input, select');

    if(campoInterno){
        const chaveFiltro = campoInterno.name.replace(/-filtro$/, '').replace(/-/g, '_').toUpperCase();
        recriarOptionNoSelect(chaveFiltro);
    }

    caixaFiltro.remove();
});

//Recria a option no select na mesma posição em que ela estava
const recriarOptionNoSelect = (chave) => {
    if(campoFiltro.querySelector(`option[value="${chave}"]`)){return;}

    const modelo = document.createElement('template');
    modelo.innerHTML = camposTotais[chave];
    const label = modelo.content.querySelector('label').textContent;

    const novaOption = document.createElement('option');
    novaOption.value = chave;
    novaOption.textContent = label;

    const chavesEmOrdem = Object.keys(camposTotais);
    const indiceAlvo = chavesEmOrdem.indexOf(chave);

    let optionDeReferencia = null;
    for(let i = indiceAlvo + 1; i < chavesEmOrdem.length; i++){
        optionDeReferencia = campoFiltro.querySelector(`option[value="${chavesEmOrdem[i]}"]`);
        if(optionDeReferencia){break;}
    }

    if(optionDeReferencia){
        campoFiltro.insertBefore(novaOption, optionDeReferencia);
    }else{
        campoFiltro.appendChild(novaOption);
    }
};

const popUpFiltro = document.getElementById("pop-up-filtro");

formularioFiltro.addEventListener("submit", event => {
    event.preventDefault();

    const todosInputs = formularioFiltro.querySelectorAll('input, select, textarea');
    todosInputs.forEach(input => {
        input.classList.remove('caixa-erro');

        const spanErro = formularioFiltro.querySelector(`#filtro-erro-${input.name}`);
        if (spanErro) { spanErro.textContent = ''; }
    });

    const dadosFormulario = new FormData(formularioFiltro);

    const objetoJs = Object.fromEntries(dadosFormulario);
    const dadosJSON = JSON.stringify(objetoJs);

    fetch(formularioFiltro.action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: dadosJSON
    })
        .then(respostaServlet => {
            return respostaServlet.text().then(texto => {
                return {
                    ok: respostaServlet.ok,
                    estaVazio: texto.trim() === '',
                    texto: texto
                };
            });
        })
        .then(resultado => {
            if (resultado.ok && resultado.estaVazio) {
                popUpFiltro.close();
                return;
            }

            if (resultado.estaVazio) { return; }

            const dadosResposta = JSON.parse(resultado.texto);

            if (Array.isArray(dadosResposta.valores)) {
                // Resposta de sucesso com dados para a tabela
                preencherTabela(dadosResposta.valores);
            } else {
                // Resposta de erro: { nome-campo: msg, ... }
                preencherErros(dadosResposta);
            }
        })
        .catch(erro => {
            console.error('Erro ao processar requisição:', erro);
        });
});

// Preenche os spans de erro de cada campo do formulário
const preencherErros = (errosNoServlet) => {
    Object.entries(errosNoServlet).forEach(([nomeDoCampo, mensagemDeErro]) => {
        const inputDoForm = formularioFiltro.querySelector(`[name="${nomeDoCampo}"]`);

        if (inputDoForm) {
            inputDoForm.classList.add('caixa-erro');

            const spanErro = formularioFiltro.querySelector(`#filtro-erro-${nomeDoCampo}`);
            if (spanErro) {
                spanErro.textContent = mensagemDeErro;
            }
        }
    });
};

// Preenche o cabeçalho e o corpo da tabela dinamicamente,
// a partir das chaves presentes nos objetos de "valores"
const preencherTabela = (valores) => {
    const tabela = document.querySelector('#caixa-tabela table');
    const cabecalho = tabela.querySelector('thead tr');
    const corpoTabela = tabela.querySelector('tbody');

    if (!tabela || !cabecalho || !corpoTabela) return;

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
        th.textContent = formatarNomeColuna(chave);
        cabecalho.appendChild(th);
    });

    const thAcoes = document.createElement('th');
    thAcoes.className = 'littleText';
    thAcoes.textContent = 'Ações';
    cabecalho.appendChild(thAcoes);

    // Monta as linhas
    valores.forEach(item => {
        const linha = document.createElement('tr');

        chaves.forEach(chave => {
            const td = document.createElement('td');
            td.className = 'littleText';
            td.textContent = item[chave] ?? '';
            linha.appendChild(td);
        });

        const tdAcoes = document.createElement('td');
        tdAcoes.className = 'littleText';
        tdAcoes.innerHTML = `
            <button type="button" class="editar-usuario" data-id="${item.id ?? ''}">Editar</button>
            <button type="button" class="excluir-usuario" data-id="${item.id ?? ''}">Excluir</button>
        `;
        linha.appendChild(tdAcoes);

        corpoTabela.appendChild(linha);
    });
};

// Converte os nomes camel case em nomes normais
const formatarNomeColuna = (chave) => {
    return chave
        .replace(/_/g, ' ')
        .replace(/([a-z])([A-Z])/g, '$1 $2')
        .replace(/\b\w/g, letra => letra.toUpperCase());
};

document.addEventListener("DOMContentLoaded", () => {
    carregarDadosIniciais();
});

const carregarDadosIniciais = () => {
    const formDadosIniciais = document.getElementById('receber-dados-iniciais');
    if (!formDadosIniciais) return;

    fetch(formDadosIniciais.action, {
        method: 'GET'
    })
        .then(resposta => resposta.text())
        .then(texto => {
            if (texto.trim() === '') return;

            const dadosResposta = JSON.parse(texto);

            if (Array.isArray(dadosResposta.valores)) {
                preencherTabela(dadosResposta.valores);
            } else {
                preencherErros(dadosResposta);
            }
        })
        .catch(erro => {
            console.error('Erro ao carregar dados iniciais:', erro);
        });
};