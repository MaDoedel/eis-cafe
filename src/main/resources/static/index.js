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
        $('#userLoginForm').submit(onLoginSubmit);
        $('#iceCreamForm').submit(onIceCreamFormSubmit);
        $('.flavourDeleteButton').on("click", onFlavourDelete);
    }

    function onLoginSubmit(e){
        e.preventDefault();
        const alertPlaceholder = document.getElementById('liveAlertPlaceholder')
        const wrapper = document.createElement('div')

        $.ajax({
            url: '/login',
            type: 'post',
            success:function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-success alert-dismissible" role="alert"> sexy </div>`
                ].join('')
                $('#userLoginForm').remove();
                setAllBinds()
            },
            error: function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-danger alert-dismissible" role="alert"> no no </div>`
                ].join('')
                $('#userLoginForm')[0].reset();
                setAllBinds()
            }
        });            
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
