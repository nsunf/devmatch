function setModalScrolling() {
  const isModalOpened = document.body.classList.contains("modal-open");
  document.documentElement.style.overflowY = isModalOpened ? "hidden" : "initial";
}

document.getElementById("comment-btn").addEventListener("click", setModalScrolling);

document.getElementById("comment-popup").addEventListener("hidden.bs.modal", () => {
  setModalScrolling();
});