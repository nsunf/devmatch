const portfolios = document.querySelectorAll(".portfolio");

portfolios.forEach(el => {
  el.addEventListener("click", () => {
    const portfolioId = el.dataset.id;
    loadPortfolio(portfolioId);
  });
});

window.addEventListener("load", () => {
	
	getRatings(0);
});


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
      console.log(data);
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

// function appendPortfolio(portfolioDto) {
//   const portfolioCardList = document.getElementById("portfolioCardList");

//   const col = document.createElement("div");
//   const portfolio = document.createElement("div");
//   col.classList.add("col");
//   portfolio.classList.add("portfolio");
//   portfolio.classList.add("col");
//   portfolio.innerHTML = `
//     <div class="portfolio col" style="cursor: pointer;">
//       <div class="card rounded-4 p-0 overflow-hidden portfolio-card" data-bs-toggle="modal" data-bs-target="#portfolio-popup">
//         <img src="${portfolioDto.imgUrlList.length > 0 ? portfolioDto.imgUrlList[0] : '/images/no-img.png'}" class="card-img-top" alt="portfolio-img" style="width: 100%; aspect-ratio: 1/1; object-fit: cover;">
//         <div class="card-body mb-3 mb-xl-0" style="box-sizing: border-box;">
//           <p class="card-text fs-5" style="text-overflow:ellipsis; white-space:nowrap; word-wrap:normal; overflow:hidden;">${portfolioDto.title}</p>
//           <p class="card-text d-none d-xl-block">${portfolioDto.regDate}</p>
//         </div>
//         <a class="hstack position-absolute bottom-0 end-0 p-2 text-primary" href="/partners/${portfolioDto.profileId}" onclick="flase;">
//           <span class="card-text me-2">${portfolioDto.memberDto.name}</span>
//           <img class="rounded-circle" src="${portfolioDto.memberDto.imgUrl ?? '/images/empty-profile-img.jpg'}" alt="profile-img" width="40" height="40" style="object-fit: cover;">
//         </a>
//       </div>
//     </div>
//   `;

//   col.appendChild(portfolio);

//   col.addEventListener("click", () => {
//     loadPortfolio(portfolioDto.id);
// 		setModalScrolling();
//   });
// };
  
  function getRatings(page) {
	  const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
	  const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

		const pathArr = location.pathname.split("/");
		const profileId = pathArr[pathArr.length - 1];
	  
	  fetch("/partners/rating", {
		  method: "POST",
		  headers: {
			  header,
			  "Content-Type": "application/json",
			  "X-Requested-With": "XMLHttpRequest",
			  "X-CSRF-Token": token
		  },
		  body: JSON.stringify({ profileId, page })
	  }).then(res => res.json())
	  .then(data => {
		  const { ratingDtoList, page, maxPage } = data;
		  createCommentCard(ratingDtoList.content);
			setPagination(page, maxPage, ratingDtoList.totalPages)
	  }).catch(err => {
		  alert(err);
	  })
  }
  
  function createCommentCard(ratingDtoList) {
	  const commentList = document.getElementById("comment");
	  commentList.innerHTML = "";
	  
	  for (let i = 0; i < ratingDtoList.length; i++) {
		  const { memberName, memberImgUrl, score, content, regDate } = ratingDtoList[i];

			const cardEl = document.createElement("div");
			cardEl.classList.add("card");
			
			cardEl.innerHTML = `
					<div class="card-header hstack">
						<div class="hstack">
							<img class="rounded-circle object-fit-cover" src="${memberImgUrl == null ? '/images/empty-profile-img.jpg' : memberImgUrl }" alt="" width="40px" height="40px">
							<span class="ms-2">${memberName}</span>
						</div>
						<div class="ms-auto">
							<div class="star-group rate-${score}">
								<div class="star"></div>
								<div class="star"></div>
								<div class="star"></div>
								<div class="star"></div>
								<div class="star"></div>
							</div>
						</div>
					</div>
					<div class="card-body">
						<blockquote class="blockquote vstack mb-0">
							<p class="fs-6 mb-1">${content}</p>
							<span class="fs-6 fw-light text-muted ms-auto">${regDate}</span>
						</blockquote>
					</div>
			`;
	  	commentList.appendChild(cardEl); 
	  }
  }
  
  function setPagination(page, maxPage, totalPage) {
	  const commentList = document.getElementById("comment");
	  const pagination = document.createElement("div");
	  pagination.classList.add("row");
	  pagination.classList.add("mt-5");
	  
	  const start = Math.floor(page/maxPage) * maxPage + 1;
	  const end = totalPage == 0 ? 1 : start + (maxPage - 1) < totalPage ? start + (maxPage - 1) : totalPage;
	  
	  let pageItems = "";
	  
	  for (let i = start; i <= end; i++) {
		  pageItems += `
					<li class="page-item ${page == i - 1 ? 'active' : null}" onclick="getRatings(${i - 1})">
						<a class="page-link" href="#comment">${i}</a>
					</li>
		  `;
	  }
	  
	  pagination.innerHTML = `
			<div class="row mt-5">
				<nav class="col hstack" aria-label="Page navigation example">
					<ul class="pagination mx-auto">

						<li class="page-item ${page == 0 ? 'disabled': null}">
							<a class="page-link" aria-label="Previous" href="#comment onclick="getRatings(${page - 1})">
								<span aria-hidden="true">&laquo;</span>
							</a>
						</li>

						${pageItems}

						<li class="page-item ${page + 1 >= totalPage ? 'disabled' : null}">
							<a class="page-link" aria-label="Next" href="#comment onclick="getRatings(${page + 1})">
								<span aria-hidden="true">&raquo;</span>
							</a>
						</li>

					</ul>
				</nav>
			</div>
	  `;
	  
	  commentList.appendChild(pagination);
  }

  function setModalScrolling() {
		const isModalOpened = document.body.classList.contains("modal-open");
		document.documentElement.style.overflowY = isModalOpened ? "hidden" : "initial";
	}