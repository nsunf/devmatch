function setModalScrolling() {
  const isModalOpened = document.body.classList.contains("modal-open");
  document.documentElement.style.overflowY = isModalOpened ? "hidden" : "initial";
}

document.querySelectorAll(".portfolio").forEach(el => {
  el.style.cursor = "pointer";
  el.addEventListener("click", () => setModalScrolling());
});

document.querySelector("#portfolio-popup").addEventListener("hidden.bs.modal", () => {
  setModalScrolling();
});