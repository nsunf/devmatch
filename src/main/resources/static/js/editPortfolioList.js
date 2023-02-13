const portfolios = document.querySelectorAll(".portfolio");

portfolios.forEach(el => {
  el.addEventListener("click", () => {
    const portfolioId = el.dataset.id;
    loadPortfolio(portfolioId);
  });
});


function loadPortfolio(portfolioId) {
  const modal_title = document.getElementById("modalLabel");
  const modal_regDate = document.getElementById("modalRegDate");
  const modal_content = document.getElementById("modalContent");
  const modal_imgs = document.getElementById("modalImgs");
  modal_imgs.innerHTML = "";

  const url = "/mypage/portfolios/load/" + portfolioId;

  fetch(url)
    .then(res => res.json())
    .then(data => {
      const portfolioDto = data.result;
      modal_title.innerText = portfolioDto.title;
      modal_regDate.innerText = portfolioDto.regDate;
      modal_content.innerText = portfolioDto.content;

      const imgUrls = portfolioDto.imgUrlList;

      for (let i = 0; i < imgUrls.length; i++) {
        const imgWrap = document.createElement("div");
        imgWrap.classList.add("col-12");
        imgWrap.innerHTML = `<img src="${imgUrls[i]}" alt="portfolio-img-${i}" width="100%">`;

        modal_imgs.appendChild(imgWrap);
      }
    });
}