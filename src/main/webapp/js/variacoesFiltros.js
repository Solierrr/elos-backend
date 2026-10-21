const gerarCampos = () => {
    const popUp = document.getElementById("pop-up-filtro");

    const urlApagar = popUp ? popUp.dataset.urlApagar : "";

    return {
        "usuario": {
            "ID" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-filtro">ID</label>
                    <div>
                        <input type="text" name="id-filtro" id="id-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id" class="span-erro"></span>
                </div>`,
            "EMAIL": `
                <div class="caixa-filtro label-e-input">
                    <label for="email-filtro">E-mail</label>
                    <div>
                        <input type="text" name="email-filtro" id="email-filtro" placeholder="ex: meu.email@gmail.com">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-email" class="span-erro"></span>

                </div>`,
            "NOME": ` 
                <div class="caixa-filtro label-e-input">
                    <label for="nome-filtro">Nome</label>
                    <div>
                        <input type="text" name="nome-filtro" id="nome-filtro" placeholder="ex: Roger" >
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-nome" class="span-erro"></span>
                </div>`,
            "TIPO_USUARIO":`
                <div class="caixa-filtro label-e-input">
                    <label for="tipo-usuario-filtro">Tipo do usuário</label>
                    <div>
                        <select name="tipo-usuario-filtro" id="tipo-usuario-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Tipo do(s) usuário(s)</option>
                            <option value="EMPRESA_DEMANDANTE">Empresa demandante</option>
                            <option value="FORNECEDOR">Fornecedor</option>
                            <option value="PROFISSIONAL">Profissional</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-tipo-usuario" class="span-erro"></span>
                </div>`,
            "RAIO_PROCURA_KM": `
                <div class="caixa-filtro label-e-input">
                    <label for="raio-procura-km-filtro">Raio Procura (KM)</label>
                    <div>
                        <input type="text" name="raio-procura-km-filtro" id="raio-procura-km-filtro" placeholder="ex: 23" pattern="[0-9.,]+">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-raio-procura-km" class="span-erro"></span>

                </div>`,
            "JOIN":`
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="TELEFONE">Telefone</option>
                            <option value="ENDERECO">Endereço</option>
                            <option value="EMPRESA_DEMANDANTE">Empresa demandante</option>
                            <option value="FORNECEDOR">Fornecedor</option>
                            <option value="PROFISSIONAL">Profissional</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        },
        "profissional": {
            "ID" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-filtro">ID</label>
                    <div>
                        <input type="text" name="id-filtro" id="id-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id" class="span-erro"></span>
                </div>`,
            "ID_USUARIO" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-usuario-filtro">ID do usuário</label>
                    <div>
                        <input type="text" name="id-usuario-filtro" id="id-usuario-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-usuario" class="span-erro"></span>
                </div>`,
            "PROFISSAO": ` 
                <div class="caixa-filtro label-e-input">
                    <label for="profissao-filtro">Profissão</label>
                    <div>
                        <input type="text" name="profissao-filtro" id="profissao-filtro" placeholder="ex: Engenheiro Eletrico" >
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-profissao" class="span-erro"></span>
                </div>`,
            "CPF": `
                <div class="caixa-filtro label-e-input">
                    <label for="cpf-filtro">CPF</label>
                    <div>
                        <input type="text" name="cpf-filtro" id="cpf-filtro" placeholder="ex: 000.000.000-00">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-cpf" class="span-erro"></span>
                </div>`,
            "ID_FORNECEDOR" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-fornecedor-filtro">ID do fornecedor</label>
                    <div>
                        <input type="text" name="id-fornecedor-filtro" id="id-fornecedor-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-fornecedor" class="span-erro"></span>
                </div>`,
            "JOIN":`
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="USUARIO">Usuario</option>
                            <option value="FORNECEDOR">Fornecedor</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "fornecedor":{
            "ID" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-filtro">ID</label>
                    <div>
                        <input type="text" name="id-filtro" id="id-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id" class="span-erro"></span>
                </div>`,
            "ID_USUARIO" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-usuario-filtro">ID do usuário</label>
                    <div>
                        <input type="text" name="id-usuario-filtro" id="id-usuario-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-usuario" class="span-erro"></span>
                </div>`,
            "TIPO_FORNECEDOR":`
                <div class="caixa-filtro label-e-input">
                    <label for="tipo-fornecedor-filtro">Tipo do fornecedor</label>
                    <div>
                        <select name="tipo-fornecedor-filtro" id="tipo-fornecedor-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="tipo-fornecedor-filtro-estado-vazio">Selecione o tipo do
                                fornecedor:
                            </option>
                            <option value="FABRICANTE">Fabricante</option>
                            <option value="DISTRIBUIDOR">Distribuidor</option>
                            <option value="REVENDEDOR">Revendedor</option>
                            <option value="INTEGRADOR">Integrador</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-tipo-fornecedor" class="span-erro"></span>
                </div>`,
            "CNPJ": `
                <div class="caixa-filtro label-e-input">
                    <label for="cnpj-filtro">CNPJ</label>
                    <div>
                        <input type="text" name="cnpj-filtro" id="cnpj-filtro" placeholder="ex: VA.MME.YR0/0001-67">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-cnpj" class="span-erro"></span>
                </div>`,
            "RAZAO_SOCIAL": ` 
                <div class="caixa-filtro label-e-input">
                    <label for="razao-social-filtro">Razão social</label>
                    <div>
                        <input type="text" name="razao-social-filtro" id="razao-social-filtro" placeholder="ex: Solar company Ltda" >
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-razao-social" class="span-erro"></span>
                </div>`,
            "JOIN":`
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="USUARIO">Usuário</option>
                            <option value="PROFISSIONAL">Profissional</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "empresa-demandante" : {
            "ID" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-filtro">ID</label>
                    <div>
                        <input type="text" name="id-filtro" id="id-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id" class="span-erro"></span>
                </div>`,
            "ID_USUARIO" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-usuario-filtro">ID do usuário</label>
                    <div>
                        <input type="text" name="id-usuario-filtro" id="id-usuario-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-usuario" class="span-erro"></span>
                </div>`,
            "CNPJ": `
                <div class="caixa-filtro label-e-input">
                    <label for="cnpj-filtro">CNPJ</label>
                    <div>
                        <input type="text" name="cnpj-filtro" id="cnpj-filtro" placeholder="ex: VA.MME.YR0/0001-67">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-cnpj" class="span-erro"></span>
                </div>`,
            "RAZAO_SOCIAL": ` 
                <div class="caixa-filtro label-e-input">
                    <label for="razao-social-filtro">Razão social</label>
                    <div>
                        <input type="text" name="razao-social-filtro" id="razao-social-filtro" placeholder="ex: Solar company Ltda" >
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-razao-social" class="span-erro"></span>
                </div>`,
            "EH_MANDANTE":`
                <div class="caixa-filtro label-e-input">
                    <label for="eh-mandante-filtro">A empresa é uma filial?</label>
                    <div>
                        <select name="eh-mandante-filtro" id="eh-mandante-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="eh-mandante-filtro-estado-vazio">Selecione a opção que mais se adequa
                            </option>
                            <option value="true">Sim</option>
                            <option value="false">Não</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-eh-mandante" class="span-erro"></span>
                </div>`,
            "JOIN":`
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="USUARIO">Usuário</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "telefone":{
            "ID" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-filtro">ID</label>
                    <div>
                        <input type="text" name="id-filtro" id="id-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id" class="span-erro"></span>
                </div>`,
            "ID_USUARIO" : `
                <div class="caixa-filtro label-e-input">
                    <label for="id-usuario-filtro">ID do usuário</label>
                    <div>
                        <input type="text" name="id-usuario-filtro" id="id-usuario-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-usuario" class="span-erro"></span>
                </div>`,
            "TELEFONE": `
                <div class="caixa-filtro label-e-input">
                    <label for="telefone-filtro">Número do telefone</label>
                    <div>
                        <input type="text" name="telefone-filtro" id="telefone-filtro" placeholder="ex: (11) 99332-7908">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-telefone" class="span-erro"></span>
                </div>`,
            "TIPO": ` 
                <div class="caixa-filtro label-e-input">
                    <label for="tipo-filtro">Tipo do telefone</label>
                    <div>
                        <select name="tipo-filtro" id="tipo-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="tipo-filtro-estado-vazio">Selecione o tipo do telefone
                            </option>
                            <option value="EMPRESARIAL">Empresarial</option>
                            <option value="PESSOAL">Pessoal</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-tipo" class="span-erro"></span>
                </div>`,
            "PRINCIPAL":`
                <div class="caixa-filtro label-e-input">
                    <label for="principal-filtro">É o telefone principal?</label>
                    <div>
                        <select name="principal-filtro" id="principal-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="principal-filtro-estado-vazio">Selecione uma opção
                            </option>
                            <option value="true">Sim</option>
                            <option value="false">Não</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-principal" class="span-erro"></span>
                </div>`,
            "JOIN":`
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="USUARIO">Usuário</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }
    };
};

export const camposValores = (nomeTela) => {
    const campos = gerarCampos();
    return campos[nomeTela];
};
