const imagesInput = document.getElementById("images-input");
const imagesPreview = document.getElementById("images-preview");

function readURL(input) {
  for (let f of input.files) {

    const reader = new FileReader();

    reader.onload = e => {
      // profilePreview.src = e.target.result;
      const imgWrap = document.createElement("div");
      const img = document.createElement("img");

      imgWrap.classList.add("col-3");
      img.classList.add("w-100")
      img.src = e.target.result;

      imgWrap.appendChild(img);

      imagesPreview.appendChild(imgWrap);
    };

    reader.readAsDataURL(f);
  }
}

imagesInput.addEventListener("change", () => {
  imagesPreview.innerHTML = "";
  readURL(imagesInput);
})