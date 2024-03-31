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

    function refetchIce() {
        $.ajax({
            url: '/ice',
            type: 'get',
            success:function(new_content){
                $("#content-1").html(new_content)
                setAllBinds()
            },
            error: function(){
                alert("Failed refetching ice")
            }
        });
    }

    function setAllBinds() {
        $('#iceCreamForm').submit(onIceCreamFormSubmit);
        $('.flavourDeleteButton').on("click", onFlavourDelete);
    }

    function onIceCreamFormSubmit(e) {
        e.preventDefault();
        $.ajax({
            url: '/ice/addFlavour',
            type: 'post',
            data:$('#iceCreamForm').serialize(),
            success: function(){
                refetchIce()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    function onFlavourDelete(e) {
        e.preventDefault();
        $.ajax({
            url: '/ice/deleteFlavour/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchIce()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    setAllBinds()
}
