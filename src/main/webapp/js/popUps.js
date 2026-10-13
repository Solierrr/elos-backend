//Constantes utilizadas
const popUpInsert = document.getElementById('pop-up-insert');
const popUpFiltro = document.getElementById('pop-up-filtro');

const botaoInsertEntrar = document.getElementById('insert-entrar');
const botaoFiltroEntrar = document.getElementById('filtro-entrar');

const botaoInsertSair = document.getElementById('insert-sair');
const botaoFiltroSair = document.getElementById('filtro-sair');

//Funcionalidades de abrir os pop-ups
if(popUpInsert && botaoInsertEntrar && popUpFiltro ){
    botaoInsertEntrar.addEventListener('click', () => {
        popUpFiltro.close();
        popUpInsert.showModal();
    });
}

if(popUpInsert && botaoFiltroEntrar && popUpFiltro ){
    botaoFiltroEntrar.addEventListener('click', () => {
        popUpInsert.close();
        popUpFiltro.showModal();}
    );
}

//Funcionalidades de fechar os pop-ups
if(popUpInsert && botaoInsertSair)
    botaoInsertSair.addEventListener('click', () => popUpInsert.close());

if(popUpFiltro && botaoFiltroSair)
    botaoFiltroSair.addEventListener('click', () => popUpFiltro.close());