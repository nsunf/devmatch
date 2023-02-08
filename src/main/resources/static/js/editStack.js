const stackImgPreview = document.getElementById("stack-img-preview");
const stackImgInput = document.getElementById("stack-img-input");
const stackNameInput = document.getElementById("name");
const stackIdInput = document.getElementById("id");

function setStackImg() {
  const fileList = stackImgInput.files;

  if (fileList.length == 0) {
    stackImgInput.src = "";
  }

  readURL(stackImgInput);
}

function readURL(input) {
  if (input.value) {
    const reader = new FileReader();

    reader.onload = e => {
      stackImgPreview.src = e.target.result;
    };

    reader.readAsDataURL(input.files[0]);
  }
}

setStackImg();
stackImgInput.addEventListener("change", setStackImg);


function setModalScrolling() {
  const isModalOpened = document.body.classList.contains("modal-open");
  document.documentElement.style.overflowY = isModalOpened ? "hidden" : "initial";
}

document.getElementById("add-stack-btn").addEventListener("click", () => {
		document.stackForm.action = "/mypage/stacks/add";
		stackImgInput.required = true;
		setModalScrolling();
	});

document.getElementById("stack-popup").addEventListener("hidden.bs.modal", () => {
	document.stackForm.reset();
	stackIdInput.value = null;
  stackImgPreview.src = "";
  setModalScrolling();
});


const editStackBtnList = document.querySelectorAll(".edit-stack-btn");
const deleteStackBtnList = document.querySelectorAll(".delete-stack-btn");

editStackBtnList.forEach(btn => {
  btn.addEventListener("click", e => {
		document.stackForm.action = "/mypage/stacks/update";
    stackNameInput.value = btn.dataset.name;
    stackImgPreview.src = btn.dataset.src;
    stackIdInput.value = btn.dataset.stackId;
    stackImgInput.required = false;
  });
});

deleteStackBtnList.forEach(btn => {
  btn.addEventListener("click", () => {
    location.href="/mypage/stacks/delete?id=" + btn.dataset.stackId;
  });
});