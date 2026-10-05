const formulario = document.getElementById('formulario-login');

formulario.addEventListener('submit', () =>{
    formulario.querySelector('input[name="navegador-e-sop"]').value = navigator.userAgent;
})