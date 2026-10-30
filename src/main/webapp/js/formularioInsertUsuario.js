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
                <input type="text" name="cnpj" id="cnpj" placeholder="ex: 00.000.000/0001-00" required>
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
                <input type="text" name="cnpj" id="cnpj" placeholder="ex: 00.000.000/0001-00" required>
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
                <div>
                    <div class="label-e-input">
                        <label for="documento-contrato">Link do documento do contrato</label>
                        <input type="text" name="documento-contrato" id="documento-contrato" required>                
                    </div>
                    <span id="erro-documento-contrato" class="span-erro"></span>
                </div>
            `;
        } else {
            conteinerIdFornecedor.innerHTML = '';
        }
    }
});
