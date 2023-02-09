
const imgChangeBtn = document.getElementById("imgChangeBtn");
const imgDeleteBtn = document.getElementById("imgDeleteBtn");
const fileInput = document.getElementById("profile-img");
const fileInputLabel = document.getElementById("profile-img-label");
const profilePreview = document.getElementById("profile-preview");

function setProfileImg() {
  const fileList = fileInput.files;
    
  if (fileList.length == 0) {
   // imgDeleteBtn.disabled = true;
   // fileInputLabel.innerText = "파일을 선택해주세요.";
  } else {
   // imgDeleteBtn.disabled = false;
   // fileInputLabel.innerText = fileList[0].name;

    readURL(fileInput);
  }
}

function readURL(input) {
  const reader = new FileReader();

  reader.onload = e => {
    profilePreview.src = e.target.result;
  };

  reader.readAsDataURL(input.files[0]);
}

setProfileImg();

fileInput.addEventListener("change", () => {
  setProfileImg();
});

imgChangeBtn.addEventListener("click", () => {
  fileInput.click();
});

/*
imgDeleteBtn.addEventListener("click", () => {
  console.log(fileInput.files.length);
  fileInput.value = "";
  profilePreview.src = "";
  fileInputLabel.innerText = "파일을 선택해주세요.";
  console.log(fileInput.files.length);
});
*/