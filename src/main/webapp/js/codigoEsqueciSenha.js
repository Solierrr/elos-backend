export class Form {
  constructor(campo, form) {
    this.form = form;
    this.campo = campo;
  }

  filtrarDigitacao() {
    this.campo.forEach((input, i) => {
      input.addEventListener("beforeinput", (e) => {
        if (e.data && !/^[0-9]$/.test(e.data)) {
          e.preventDefault();
        }
      });

      input.addEventListener("input", () => {
        input.value = input.value.replace(/[^0-9]/g, "").slice(0, 1);

        if (input.value.length === 1 && this.campo[i + 1]) {
          this.campo[i + 1].focus();
        }
      });

      input.addEventListener("keydown", (e) => {
        const ultimo = i === this.campo.length - 1;

        if (e.key === "Enter" && !ultimo) {
          e.preventDefault();
        }

        if (e.key === "Backspace" && !input.value && this.campo[i - 1]) {
          this.campo[i - 1].focus();
        }
      });
    });
  }

  verificarT() {
    this.form.addEventListener("submit", (e) => {
      let situacao = true;

      this.campo.forEach((input) => {
        if (!/^[0-9]$/.test(input.value)) {
          situacao = false;
        }
      });

      if (!situacao) {
        alert("Há um erro em um campo");
        e.preventDefault();
      }
    });
  }
}

const formulario = document.getElementById("formulario-codigo");
const campo = document.querySelectorAll("#formulario-codigo .text-input");
const contextPath = document.querySelector('meta[name="context-path"]').content;

const validacao = new Form(campo, formulario);
validacao.filtrarDigitacao();
validacao.verificarT();

//Envio dos formularios
formulario.addEventListener('invalid', event => {
  event.preventDefault();
}, true);

const spanErro = document.getElementById("erro-codigo");

formulario.addEventListener("submit", event => {
  event.preventDefault();

  spanErro.textContent = "";

  const dados = new FormData(formulario);

  let token = "";

  dados.forEach((value, key) => token += value);

  const dado = new FormData();
  dado.append("token", token);

  const objetoJs = Object.fromEntries(dado);
  const dadosJSON = JSON.stringify(objetoJs);

  formulario.reset();

  fetch(formulario.action, {
    method: "POST", headers: {"Content-Type": "application/json"}, body: dadosJSON
  })
    .then(respostaServlet => {
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok, texto: texto.trim()
        };
      });
    })
    .then(resultado => {
      if (!resultado.ok) {
        spanErro.textContent = "Algo inesperado aconteceu, tente novamente!";
        return;
      }
      try {
        const dadosResposta = JSON.parse(resultado.texto);
        if (dadosResposta && dadosResposta.token) {
          spanErro.textContent = dadosResposta.token;
          return;
        }
      } catch (e) {
      }
      window.location.href = resultado.texto;
    }).catch(() => window.location.href = contextPath);
});

const textoReenviar = document.getElementById("reenviar-email");

textoReenviar.addEventListener("click", (e) => {

  spanErro.textContent = "";

  const dado = new FormData();
  dado.append("reenviar", "true");

  const objetoJs = Object.fromEntries(dado);
  const dadosJSON = JSON.stringify(objetoJs);

  formulario.reset();

  fetch(formulario.action, {
    method: "POST", headers: {"Content-Type": "application/json"}, body: dadosJSON
  })
    .then(respostaServlet => {
      return respostaServlet.text().then(texto => {
        return {
          ok: respostaServlet.ok, texto: texto.trim()
        };
      });
    })
    .then(resultado => {
      if (!resultado.ok) {
        spanErro.textContent = "Algo inesperado aconteceu, tente novamente!";
        return;
      }
      try {
        const dadosResposta = JSON.parse(resultado.texto);
        if (dadosResposta && dadosResposta.codigo) {
          spanErro.textContent = dadosResposta.codigo;

        }
      } catch (e) {
      }
    }).catch(() => window.location.href = contextPath);

})