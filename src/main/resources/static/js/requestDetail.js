function setModalScrolling() {
  const isModalOpened = document.body.classList.contains("modal-open");
  document.documentElement.style.overflowY = isModalOpened ? "hidden" : "initial";
}

const commentBtn = document.getElementById("comment-btn");

if (commentBtn != null)
	commentBtn.addEventListener("click", setModalScrolling);

document.getElementById("comment-popup").addEventListener("hidden.bs.modal", () => {
  setModalScrolling();
});