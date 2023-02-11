window.onload = () => {
  setTimeout(() => {
    window.scrollTo(0, 0);
  }, 100);
}

const footer = document.querySelector("footer");
let pageCount = 0;

const observer = new IntersectionObserver(loadProfileList);
observer.observe(footer);

const resetBtn = document.querySelector("button[type=reset]");

resetBtn.addEventListener("click", e => {
  e.preventDefault();
  resetSearchForm();
})

function resetSearchForm() {
  const frm = document.frm;

  const elements = frm.elements;

  for (let i = 0; i < elements.length; i++) {
    const input = elements[i];
    const inputType = input.getAttribute("type");
    if (inputType == "search")
      input.value = "";
    else if (inputType == "checkbox")
      input.checked = false;
  }
}

function loadProfileList() {
  const query = location.search;
  let reqUrl = "/partners/load";

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
      // console.log(list[i]);
      appendProfile(list[i]);
    }

  })
  .catch(e => {
    pageCount--;
    console.log(e);
  })
}

function appendProfile(profileCardDto) {
  const profileCardList = document.getElementById("profileCardList");

  const col = document.createElement("div");
  col.classList.add("col");
  col.innerHTML = `
    <a class="text-decoration-none profile-card-link" href="${''}">
      <div class="profile-card background-gradient-1 mx-auto">
        <div class="profile-card__top">
          <div class="profile-card__grade">
            <img src="../images/grade/Level=1.png" alt="grad-img">
          </div>
          <div class="profile-card__desc">
            <h5 class="desc__greeting">${profileCardDto.title}</h5>
            <p class="desc__intro">${profileCardDto.subTitle}</p>
          </div>
        </div>
        <div class="profile-card__bottom prov-profile">
          <img class="prov-profile__img" src="${profileCardDto.memberImgUrl}" alt="prov-img" onerror="this.src='/images/empty-profile-img.jpg'" style="object-fit: cover;">
          <span class="prov-profile__name">${profileCardDto.memberName}</span>
        </div>
      </div>
    </a>
  `;

  profileCardList.appendChild(col);
}