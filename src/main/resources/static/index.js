window.onload = function() {
    let selected_index = 0;

    for (let i = 0; i < document.getElementById("navbar").children.length - 1; i++) {
        document.getElementById("navbar").children[i+1].addEventListener("click", function() {
            document.getElementById("navbar").children[selected_index+1].classList.remove("selected")
            document.getElementById("content-"+ selected_index).classList.remove("selected")
            selected_index = i
            this.classList.add("selected")
            document.getElementById("content-"+ i).classList.add("selected")
        });
    }

    function onIceCreamFormSubmit(e) {
        e.preventDefault();
        $.ajax({
            url: '/ice/addFlavour',
            type: 'post',
            data:$('#iceCreamForm').serialize(),
            success:function(data){
                new_content = $($.parseHTML(data))
                $("#content-1").html(new_content.html())
                $('#iceCreamForm').submit(onIceCreamFormSubmit)
            }
        });
    }

    $('#iceCreamForm').submit(onIceCreamFormSubmit);
}