
function setMainSize() {
  const header = document.querySelector("header");
  const footer = document.querySelector("footer");
  const main = document.querySelector("main");

  const calcedHeight = window.innerHeight - (header.offsetHeight + footer.offsetHeight);

  main.style.minHeight = calcedHeight + "px";
}

function resize(obj) {
  // obj.style.height = "1px";
  // obj.style.height = (12+obj.scrollHeight)+"px";
  obj.style.height = 18 + obj.scrollHeight + "px";
}

setMainSize();
window.addEventListener("resize", setMainSize);
