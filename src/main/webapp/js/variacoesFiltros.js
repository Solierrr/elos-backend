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

                </div>`
        }
    };
};

export const camposValores = (nomeTela) => {
    const campos = gerarCampos();
    return campos[nomeTela];
};
