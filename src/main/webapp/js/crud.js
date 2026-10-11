//Abrir e fechar o pop-up do insert
const popUpInsert = document.getElementById('pop-up-insert');

const botaoInsertEntrar = document.getElementById('insert-entrar');
botaoInsertEntrar.addEventListener('click', () => {
    popUpFiltro.close();
    popUpInsert.showModal();
});

const botaoInsertSair = document.getElementById('insert-sair');
botaoInsertSair.addEventListener('click', () => popUpInsert.close());

//Abrir e fechar o pop-up do filtro
const popUpFiltro = document.getElementById('pop-up-filtro');

const botaoFiltroEntrar = document.getElementById('filtro-entrar');
botaoFiltroEntrar.addEventListener('click', () => {
    popUpInsert.close();
    popUpFiltro.showModal();}
);

const botaoFiltroSair = document.getElementById('filtro-sair');
botaoFiltroSair.addEventListener('click', () => popUpFiltro.close());


//Altera o questionario de acordo com o tipo de usuario
const formularioInsert = document.getElementById('formulario-insert');
const dadosMutaveis = document.getElementById('dados-mutaveis');

const tipoUsuario = document.getElementById('tipo-usuario');
tipoUsuario.addEventListener('change', event => {
    const tipoUsuarioEscolhido = event.target.value;
    
    dadosMutaveis.innerHTML = '';

    if(tipoUsuarioEscolhido.toUpperCase() === 'EMPRESA_DEMANDANTE'){
        const formulario = `
        <div>
            <div class="label-e-input">
                <label for="cnpj">CNPJ</label>
                <input type="text" name="cnpj" id="cnpj" placeholder="ex: 000.000.000-00" required>
            </div>
            <span id="erro-cnpj" class="span-erro"></span>
        </div>
        
        <div>
            <div class="label-e-input">
                <label for="razao-social">Razão social</label>
                <input type="text" name="razao-social" id="razao-social" placeholder="ex: Padaria do pão" required>
            </div>
            <span id="erro-razao-social" class="span-erro"></span>
        </div>
        
        <div>
            <div class="label-e-input">
                <label for="eh-mandante">É a mandante?</label>
                <select name="eh-mandante" id="eh-mandanter" required>
                    <option value="" disabled selected hidden>Selecione uma opção</option>
                    <option value="true">Sim</option>
                    <option value="false">Não</option>
                </select>      
            </div>
            <span id="erro-eh-mandante" class="span-erro"></span>
        </div>
        `;
        
        dadosMutaveis.insertAdjacentHTML('beforeend',formulario);
    } else if(tipoUsuarioEscolhido.toUpperCase() === 'FORNECEDOR'){
        const formulario = `
        <div>
            <div class="label-e-input">
                <label for="tipo-fornecedo" >Tipo do fornecedor</label>
                <select name="tipo-fornecedor" id="tipo-fornecedor" required>
                    <option value="" disabled selected hidden>Selecione o tipo de fornecedor</option>
                    <option value="FABRICANTE">Fabricante</option>
                    <option value="DISTRIBUIDOR">Distribuidor</option>
                    <option value="REVENDEDOR">Revendedor</option>
                    <option value="INTEGRADOR">Integrador</option>
                </select>        
            </div>
            <span id="erro-tipo-fornecedor" class="span-erro"></span>
        </div>

        <div>
            <div class="label-e-input">    
                <label for="cnpj" >CNPJ</label>
                <input type="text" name="cnpj" id="cnpj" placeholder="ex: 000.000.000-00" required>
                </div>
                <span id="erro-cnpj" class="span-erro"></span>
        </div>

        <div>
            <div class="label-e-input">
                <label for="razao-social">Razão social</label>
                <input type="text" name="razao-social" id="razao-social" placeholder="ex: Solaris Placas Fotovoltaicas" required>
            </div>
            <span id="erro-razao-social" class="span-erro"></span>
        </div>
        `;

        dadosMutaveis.insertAdjacentHTML('beforeend',formulario);
    } else if(tipoUsuarioEscolhido.toUpperCase() === 'PROFISSIONAL'){
        const formulario = `
        <div>
            <div class="label-e-input">
                <label for="cpf" required>CPF</label>
                <input type="text" name="cpf" id="cpf" placeholder="ex: 000.000.000-00" required>        
            </div>
            <span id="erro-cpf" class="span-erro"></span>
        </div>

        <div>
            <div class="label-e-input">
                <label for="profissao" required>Profissão</label>
                <input type="text" name="profissao" id="profissao" placeholder="ex: Engenheiro elétrico" required>
            </div>
            <span id="erro-profissao" class="span-erro"></span>
        </div>

        <div>
            <div class="label-e-input">
                <label for="trabalha-para-empresa">Trabalha para alguma empresa?</label>
                <select name="trabalha-para-empresa" id="trabalha-para-empresa" required>
                    <option value="" disabled selected hidden>Selecione uma opção</option>
                    <option value="true">Sim</option>
                    <option value="false">Não</option>
                </select>  
            </div>
            <span id="erro-profissao" class="span-erro"></span>
        </div>

        <div id="caixa-caso-trabelhe-para-empresa"></div>
        `;

        dadosMutaveis.insertAdjacentHTML('beforeend',formulario);
    }
    
});

//Para quando selecionar trabalha para empresa
document.addEventListener('change', event => {
    if (event.target && event.target.id === 'trabalha-para-empresa') {
        
        const conteinerIdFornecedor = document.getElementById("caixa-caso-trabelhe-para-empresa");

        if (!conteinerIdFornecedor) return;

        if (event.target.value === "true") {
            conteinerIdFornecedor.innerHTML = `
                <div>
                    <div class="label-e-input">
                        <label for="id-fornecedor">ID do fornecedor</label>
                        <input type="text" name="id-fornecedor" id="id-fornecedor" placeholder="ex: 56" required>                
                    </div>
                    <span id="erro-id-fornecedor" class="span-erro"></span>
                </div>
            `;
        } else {
            conteinerIdFornecedor.innerHTML = '';
        }
    }
});


//Partes relacionadas ao botão da senha:
const botaoSenha = document.getElementById('olho-senha');
const inputSenha = document.getElementById('senha');

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


//Manipulação de erros e envio do formulario de insert
const mensagensInsertSpans = new Map([
    ["erro-email", "E-mail inválido"],
    ["erro-senha", "A senha está fraca"],
    ["erro-nome", "Nome inválido"],
    ["erro-raio-procura-km", "Raio de procura inválido"],
    ["erro-tipo-usuario", "Selecione um tipo de usuário"],
    ["erro-cnpj", "CNPJ inválido"],
    ["erro-razao-social", "Razão social inválida"],
    ["erro-eh-mandante", "Opção inválida"],
    ["erro-tipo-fornecedor", "Selecione um tipo do fornecedor"],
    ["erro-profissao", "Profissão inválida"],
    ["erro-cpf", "CPF inválido"],
    ["erro-id-fornecedor", "ID do fornecedor inválido"]
])

formularioInsert.addEventListener('invalid', event => {
    event.preventDefault();

    const input = event.target;

    const span = document.getElementById(`erro-${input.name}`);

    span.textContent = mensagensInsertSpans.get(span.id);
    
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
        input.classList.remove('span-erro'); // Remove a classe de erro se existir
        
        // Se você colocou span com texto de erro embaixo, pode limpá-lo também:
        const spanErro = formularioInsert.querySelector(`#erro-${input.name}`);
        if (spanErro){ spanErro.textContent = '';}
    });

    const dadosFormulario = new FormData(formularioInsert);
    dadosFormulario.delete('trabalha-para-empresa');

    const objetoJs = Object.fromEntries(dadosFormulario);

    const dadosJSON = JSON.stringify(objetoJs);

    fetch('/crudUsuario-insert', {
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
        if(resultado.ok && resultado.estaVazio){
            alert('Usuário cadastrado com sucesso!');
            formularioInsert.reset();
        } else {

            const errosNoServlet = JSON.parse(resultado.texto);

            Object.entries(errosNoServlet).forEach(([nomeDoCampo, mensagemDeErro]) => {

                const inputDoForm = form.querySelector(`[name="${nomeDoCampo}"]`);
                
                if (inputDoForm) {
                    inputDoForm.classList.add('span-erro');

                    const spanErro = form.querySelector(`#erro-${nomeDoCampo}`);
                    if (spanErro) {
                        spanErro.textContent = mensagemDeErro;
                    }
                }
            });
        }
    });
});


//Dinamismo do pop-up filtrar

//O primeiro valor indica como colocar no label, o segundo o nome em kebab para por nos ids e names, o ultimo o placeholder
const camposTotais = new Map([
    ['ID', ['ID', 'id', 'Id do usuário buscado ex: 2']],
    ['EMAIL', ['E-mail', 'email', 'Email do usuário buscado ex: meu.email@gmail.com']],
    ['NOME', ['Nome', 'nome', 'Nome do usuário buscado ex: Roger']],
    ['TIPO_USUARIO', ['Tipo do usuário', 'tipo-usuario', 'Tipo do(s) usuário(s)']],
    ['RAIO_PROCURA_KM', ['Raio de procura em km', 'raio-procura-km', 'Valor máximo a se exibido dos raios de procura ex: 23']]
]);

const camposDisponiveis = new Map([
    ['ID', ['ID', 'id', 'Id do usuário buscado ex: 2']],
    ['EMAIL', ['E-mail', 'email', 'Email do usuário buscado ex: meu.email@gmail.com']],
    ['NOME', ['Nome', 'nome', 'Nome do usuário buscado ex: Roger']],
    ['TIPO_USUARIO', ['Tipo do usuário', 'tipo-usuario', 'Tipo do(s) usuário(s)']],
    ['RAIO_PROCURA_KM', ['Raio de procura em km', 'raio-procura-km', 'Valor máximo a se exibido dos raios de procura ex: 23']]
]);

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

    const valores = camposDisponiveis.get(valorCampoFiltro);
    
    if(!valores){
        return;
    }else{
        console.log(valorCampoFiltro)
        document.querySelector(`#campo-filtro option[value="${valorCampoFiltro.toUpperCase()}"]`).remove();
        campoFiltro.value = "CAMPO_VAZIO";
    }

    if(valorCampoFiltro === 'TIPO_USUARIO'){
        formularioFiltro.insertAdjacentHTML('beforeend',`
            <div class="caixa-filtro">

                <label for="tipo-usuario">Tipo do usuário</label>

                <select name="tipo-usuario" id="tipo-usuario" required>
                    <option value="" disabled selected hidden>Tipo do(s) usuário(s)</option>
                    <option value="EMPRESA_DEMANDANTE">Empresa demandante</option>
                    <option value="FORNECEDOR">Fornecedor</option>
                    <option value="PROFISSIONAL">Profissional</option>
                </select>
                <button type="button" class="remover-filtro botoes-sair" ><img src="/assets/icone/icone_sair.svg"></button>
            </div>`);
    }else{
        formularioFiltro.insertAdjacentHTML('beforeend', `
            <div class="caixa-filtro">
                <label for="${valores[1]}">${valores[0]}</label>
                <input type="text" name="${valores[1]}" id="${valores[1]}">
                <button type="button" class="remover-filtro botoes-sair" ><img src="/assets/icone/icone_sair.svg"></button>
            </div>`);
    }

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
    if (!campoInterno) {
        caixaFiltro.remove();
        return;
    }

    const nomeKebab = campoInterno.name;

    let chaveEncontrada = null;
    for (const [chave, valores] of camposTotais) {
        if (valores[1] === nomeKebab) {
            chaveEncontrada = chave;
            break;
        }
    }

    if (chaveEncontrada) {
        const valoresOriginais = camposTotais.get(chaveEncontrada);

        camposDisponiveis.set(chaveEncontrada, valoresOriginais);

        recriarOptionNoSelect(chaveEncontrada, valoresOriginais[0]);
    }

    caixaFiltro.remove();
});

function recriarOptionNoSelect(chave, label) {
    const optionExistente = campoFiltro.querySelector(`option[value="${chave}"]`);
    if (optionExistente) {
        return;
    }

    const novaOption = document.createElement('option');
    novaOption.value = chave;
    novaOption.textContent = label;

    const chavesEmOrdem = Array.from(camposTotais.keys());
    const indiceAlvo = chavesEmOrdem.indexOf(chave);

    let optionDeReferencia = null;
    for (let i = indiceAlvo + 1; i < chavesEmOrdem.length; i++) {
        optionDeReferencia = campoFiltro.querySelector(`option[value="${chavesEmOrdem[i]}"]`);
        if (optionDeReferencia) {
            break;
        }
    }

    if (optionDeReferencia) {
        campoFiltro.insertBefore(novaOption, optionDeReferencia);
    } else {
        campoFiltro.appendChild(novaOption);
    }
}