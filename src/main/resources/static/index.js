$(document).ready( function() {
    function refetchIce() {
        $.ajax({
            url: '/ice',
            type: 'get',
            success:function(new_content){
                $("#tab-1").html(new_content)
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
})
