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
        if (popUpInsert) popUpInsert.close();
        if (popUpFiltro) popUpFiltro.close();
        if (popUpUpdate) popUpUpdate.close();
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
    if (popUpInsert && botaoInsertSair) botaoInsertSair.addEventListener('click', () => popUpInsert.close());
    if (popUpFiltro && botaoFiltroSair) botaoFiltroSair.addEventListener('click', () => popUpFiltro.close());
    if (popUpUpdate && botaoUpdateSair) botaoUpdateSair.addEventListener('click', () => popUpUpdate.close());


    const corpoTabelaDelegado = document.querySelector('#caixa-tabela table tbody');

    corpoTabelaDelegado.addEventListener('click', event => {
        const botaoEditar = event.target.closest('.editar');
        if (botaoEditar) {
            fecharTodos();
            popUpUpdate.showModal();
            const id = botaoEditar.dataset.id;
            // ex: preencherFormularioUpdate(id);
        }
    });
