const voltar = document.getElementById("voltar")

voltar.addEventListener("click", (event) => {
  event.preventDefault();
  window.history.back();
});