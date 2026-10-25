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
        }, "telefone-admin": {
            "ID": `
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
            "ID_ADMIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="id-admin-filtro">ID do admin</label>
                    <div>
                        <input type="text" name="id-admin-filtro" id="id-admin-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-admin" class="span-erro"></span>
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
            "PRINCIPAL": `
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
            "JOIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "acesso-admin": {
            "ID": `
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
            "ID_ADMIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="id-admin-filtro">ID do admin</label>
                    <div>
                        <input type="text" name="id-admin-filtro" id="id-admin-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-admin" class="span-erro"></span>
                </div>`,
            "DATA_ACESSO": `
                <div class="caixa-filtro label-e-input">
                    <label for="data-acesso-filtro">Data de acesso</label>
                    <div>
                        <input type="text" name="data-acesso-filtro" id="data-acesso-filtro" placeholder="ex: 14/07/2026">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-data-acesso" class="span-erro"></span>
                </div>`,
            "NAVEGADOR": `
                <div class="caixa-filtro label-e-input">
                    <label for="navegador-filtro">É o telefone navegador?</label>
                    <div>
                        <select name="navegador-filtro" id="navegador-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="navegador-filtro-estado-vazio">Selecione o navegador
                            </option>
                            <option value="CHROME">Chrome</option>
                            <option value="FIREFOX">Firefox</option>
                            <option value="EDGE">Edge</option>
                            <option value="EXPLORER">Explorer</option>
                            <option value="OPERA">Opera</option>
                            <option value="SAFARI">Safari</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-navegador" class="span-erro"></span>
                </div>`,
            "SISTEMA_OPERACIONAL": `
                <div class="caixa-filtro label-e-input">
                    <label for="sistema-operacional-filtro">É o telefone sistema-operacional?</label>
                    <div>
                        <select name="sistema-operacional-filtro" id="sistema-operacional-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="sistema-operacional-filtro-estado-vazio">Selecione o sistema operacional
                            </option>
                            <option value="WINDOWS">Windows</option>
                            <option value="MAC">Mac</option>
                            <option value="LINUX">Linux</option>
                            <option value="ANDROID">Android</option>
                            <option value="IOS">IOS</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-sistema-operacional" class="span-erro"></span>
                </div>`,
            "JOIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "historico-alteracoes":{
            "ID": `
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
            "ID_ADMIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="id-admin-filtro">ID do admin</label>
                    <div>
                        <input type="text" name="id-admin-filtro" id="id-admin-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-admin" class="span-erro"></span>
                </div>`,
            "ID_REGISTRO": `
                <div class="caixa-filtro label-e-input">
                    <label for="id-registro-filtro">ID do registro</label>
                    <div>
                        <input type="text" name="id-registro-filtro" id="id-registro-filtro" placeholder="ex: 2">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-id-registro" class="span-erro"></span>
                </div>`,
            "DATA_ALTERACAOO": `
                <div class="caixa-filtro label-e-input">
                    <label for="data-alteracao-filtro">Data de alteração</label>
                    <div>
                        <input type="text" name="data-alteracao-filtro" id="data-alteracao-filtro" placeholder="ex: 14/07/2026">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-data-alteracao" class="span-erro"></span>
                </div>`,
            "ACAO": `
                <div class="caixa-filtro label-e-input">
                    <label for="acao-filtro">Ação realizada</label>
                    <div>
                        <select name="acao-filtro" id="acao-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="acao-filtro-estado-vazio">Selecione o acao
                            </option>
                            <option value="UPDATE">Atualização de um registro</option>
                            <option value="INSERT">Cadastro de um registro</option>
                            <option value="DELETE">Apagamento de um registro</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-acao" class="span-erro"></span>
                </div>`,
            "JOIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "admin":{
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
            "FUNCAO": `
                <div class="caixa-filtro label-e-input">
                    <label for="funcao-filtro">Função do admin</label>
                    <div>
                        <select name="funcao-filtro" id="funcao-filtro">
                            <option value="CAMPO_VAZIO" disabled selected hidden
                                    id="funcao-filtro-estado-vazio">Selecione o função
                            </option>
                            <option value="SUPERADMIN">Super admin</option>
                            <option value="REVISOR">Revisor</option>
                            <option value="GERENTE">Gerente</option>
                            <option value="ANALISTA">Analista</option>
                            <option value="MODELADOR">Modelador</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-funcao" class="span-erro"></span>
                </div>`,
            "JOIN": `
                <div class="caixa-filtro label-e-input">
                    <label for="join-filtro">Com qual registro você deseja conectar os dados?</label>
                    <div>
                        <select name="join-filtro" id=join-filtro" required>
                            <option value="INVALIDO" disabled selected hidden>Dados conectaveis</option>
                            <option value="HISTORICO_ALTERACOES">Histórico de alterações</option>
                            <option value="LOG_ACESSOS_ADMIN">Histórico de acessos</option>
                            <option value="TELEFONE_ADMIN">Telefone do admin</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-join" class="span-erro"></span>
                </div>`
        }, "endereco":{
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
            "ESTADO" : `
                <div class="caixa-filtro label-e-input">
                    <label for="estado-filtro">Estado</label>
                    <div>
                        <select name="estado-filtro" id="estado-filtro">
                          <option value="" disabled selected>Selecione um estado</option>
                          <option value="AC">Acre</option>
                          <option value="AL">Alagoas</option>
                          <option value="AP">Amapá</option>
                          <option value="AM">Amazonas</option>
                          <option value="BA">Bahia</option>
                          <option value="CE">Ceará</option>
                          <option value="DF">Distrito Federal</option>
                          <option value="ES">Espírito Santo</option>
                          <option value="GO">Goiás</option>
                          <option value="MA">Maranhão</option>
                          <option value="MT">Mato Grosso</option>
                          <option value="MS">Mato Grosso do Sul</option>
                          <option value="MG">Minas Gerais</option>
                          <option value="PA">Pará</option>
                          <option value="PB">Paraíba</option>
                          <option value="PR">Paraná</option>
                          <option value="PE">Pernambuco</option>
                          <option value="PI">Piauí</option>
                          <option value="RJ">Rio de Janeiro</option>
                          <option value="RN">Rio Grande do Norte</option>
                          <option value="RS">Rio Grande do Sul</option>
                          <option value="RO">Rondônia</option>
                          <option value="RR">Roraima</option>
                          <option value="SC">Santa Catarina</option>
                          <option value="SP">São Paulo</option>
                          <option value="SE">Sergipe</option>
                          <option value="TO">Tocantins</option>
                        </select>
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-estado" class="span-erro"></span>
                </div>`,
            "CIDADE": `
                <div class="caixa-filtro label-e-input">
                    <label for="cidade-filtro">Cidade</label>
                    <div>
                        <input type="text" name="cidade-filtro" id="cidade-filtro" placeholder="ex: São Paulo">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-cidade" class="span-erro"></span>
                </div>`,
            "BAIRRO": `
                <div class="caixa-filtro label-e-input">
                    <label for="bairro-filtro">Bairro</label>
                    <div>
                        <input type="text" name="bairro-filtro" id="bairro-filtro" placeholder="ex: Jaragua">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-bairro" class="span-erro"></span>
                </div>`,
            "CEP": `
                <div class="caixa-filtro label-e-input">
                    <label for="cep-filtro">CEP</label>
                    <div>
                        <input type="text" name="cep-filtro" id="cep-filtro" placeholder="ex: 01311-000">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-cep" class="span-erro"></span>
                </div>`,
            "LOGRADOURO": `
                <div class="caixa-filtro label-e-input">
                    <label for="logradouro-filtro">Logradouro</label>
                    <div>
                        <input type="text" name="logradouro-filtro" id="logradouro-filtro" placeholder="ex: Rua Borboletas Psicodélicas">
                        <button type="button" class="remover-filtro botoes-sair">
                            <img src="${urlApagar}" class="icone-lixeira">
                        </button>
                    </div>
                    <span id="filtro-erro-logradouro" class="span-erro"></span>
                </div>`,
            "JOIN": `
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
