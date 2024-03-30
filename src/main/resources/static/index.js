window.onload = function() {
    for (let i = 0; i < document.getElementById("navbar").children.length; i++) {
        document.getElementById("navbar").children[i].addEventListener("click", function() {
            for (let j = 0; j < document.getElementById("navbar").children.length; j++) {
                document.getElementById("navbar").children[j].classList.remove("active");
            }
            this.classList.add("active");
        });
    }
}