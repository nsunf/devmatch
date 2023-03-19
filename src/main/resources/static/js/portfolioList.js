window.onload = () => {
  setTimeout(() => {
    window.scrollTo(0, 0);
  }, 100);
}

const footer = document.querySelector("footer");
let pageCount = 0;

const observer = new IntersectionObserver(loadPortfolioList);
observer.observe(footer);

function loadPortfolioList() {
  const query = location.search;
  const qq = query.slice(1).split("&").filter(q => q.startsWith("searchQuery")).map(q => q.split("="));
  const searchQuery = qq.length > 0 ? qq[0][1] : "";
  let reqUrl = "/portfolios/load";

  if (query == "")
    reqUrl += "?page=" + pageCount++;
  else
    reqUrl += query + "&page=" + pageCount++;


  fetch(reqUrl, {
  }).then(res => res.json())
  .then(data => {
    const list = data.result.content;

    if (list.length == 0) {
      pageCount--;
      return;
    }

    for (let i = 0; i < list.length; i++) {
      appendPortfolio(list[i]);
    }

  })
  .catch(e => {
    pageCount--;
    console.log(e);
  })
}

function appendPortfolio(portfolioDto) {
  const portfolioCardList = document.getElementById("portfolioCardList");

  const col = document.createElement("div");
  const portfolio = document.createElement("div");
  col.classList.add("col");
  portfolio.classList.add("portfolio");
  portfolio.classList.add("col");
  portfolio.innerHTML = `
    <div class="portfolio col" style="cursor: pointer;">
      <div class="card rounded-4 p-0 overflow-hidden portfolio-card" data-bs-toggle="modal" data-bs-target="#portfolio-popup">
        <img src="${portfolioDto.imgUrlList.length > 0 ? portfolioDto.imgUrlList[0] : '/images/no-img.png'}" class="card-img-top" alt="portfolio-img" style="width: 100%; aspect-ratio: 1/1; object-fit: cover;">
        <div class="card-body mb-3 mb-xl-0" style="box-sizing: border-box;">
          <p class="card-text fs-5" style="text-overflow:ellipsis; white-space:nowrap; word-wrap:normal; overflow:hidden;">${portfolioDto.title}</p>
          <p class="card-text d-none d-xl-block">${portfolioDto.regDate}</p>
        </div>
        <a class="hstack position-absolute bottom-0 end-0 p-2 text-primary" href="/partners/${portfolioDto.profileId}" onclick="flase;">
          <span class="card-text me-2">${portfolioDto.memberDto.name}</span>
          <img class="rounded-circle" src="${portfolioDto.memberDto.imgUrl ?? '/images/empty-profile-img.jpg'}" alt="profile-img" width="40" height="40" style="object-fit: cover;">
        </a>
      </div>
    </div>
  `;

  col.appendChild(portfolio);

  col.addEventListener("click", () => {
    loadPortfolio(portfolioDto.id);
		setModalScrolling()
  });

  portfolioCardList.appendChild(col);
}

function loadPortfolio(portfolioId) {
  const modal_title = document.getElementById("modalLabel");
  const modal_regDate = document.getElementById("modalRegDate");
  const modal_content = document.getElementById("modalContent");
  const modal_imgs = document.getElementById("modalImgs");
  modal_imgs.innerHTML = "";
 
  const url = "/portfolios/load/" + portfolioId;

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

/*
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
*/