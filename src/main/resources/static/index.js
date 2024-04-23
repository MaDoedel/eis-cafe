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
        $('#iceCreamForm')[0].reset()
    }

    function setAllBinds() {
        $('#userLoginForm').submit(onLoginSubmit);
        $('#iceCreamForm').submit(onIceCreamFormSubmit);
        $('#jobsForm').submit(onJobsFormSubmit);

        $('#flavourDeleteButton').on("click", onFlavourDelete);
        $('#placeholderImage').on("click", selectImage);
        $('#formFile').on("change", previewImage);

        $('#CVButton').on("click", selectCV);
        $('#CVInput').on("change", previewCV);

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
                    `<div class="alert alert-success alert-dismissible" role="alert"> You in </div>`
                ].join('')
                $('#userLoginForm').remove();
                setAllBinds()
            },
            error: function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-danger alert-dismissible" role="alert"> Naah </div>`
                ].join('')
                $('#userLoginForm')[0].reset();
                setAllBinds()
            }
        });            
    }


    function selectImage() {
        document.getElementById('formFile').click();
    }

    function selectCV(e) {
        e.preventDefault();
        document.getElementById('CVInput').click();
    }

    function previewCV() {
        var input = document.getElementById('CVInput');
        var fileName = input.files[0].name;
        var cvTitle = document.getElementById('cvTitle');
        cvTitle.textContent = fileName;

        if (file.type === 'application/pdf') {
            var reader = new FileReader();

            reader.onload = function(){
                var dataURL = reader.result;
                var img = document.getElementById('placeholderImage'); 
                img.src = dataURL;
            };
            reader.readAsDataURL(file);
        }
    }

    function onJobsFormSubmit(e) {
        e.preventDefault();

        var formData = new FormData($('#jobsForm')[0]);
        const alertPlaceholder = document.getElementById('jobsFormAlert')
        const wrapper = document.createElement('div')

        $.ajax({
            url: '/jobs/apply',
            type: 'post',
            data: formData,
            contentType: false,
            processData: false, 
            success: function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-success" role="alert">
                    <h4 class="alert-heading">Hervorragend!</h4>
                    <p> Ihre Bewerbung ist hinterlegt und wir bedanken uns für Ihr Intersse!</p>
                    <hr>
                    <p class="mb-0">Zusätzlich zu Ihrer E-Mail Bestätigung, kontaktieren wir Sie in den nächsten Tagen per Mail.</p>
                  </div>`
                ].join('')
                setAllBinds()
            },
            error: function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-danger alert-dismissible" role="alert"> Naah </div>`
                ].join('')
                setAllBinds()
            }
        });
    }

    function onIceCreamFormSubmit(e) {
        e.preventDefault();

        var formData = new FormData($('#iceCreamForm')[0]);

        $.ajax({
            url: '/ice/addFlavour',
            type: 'post',
            data: formData,
            contentType: false, // Set contentType to false, FormData will automatically set it correctly
            processData: false, // Set processData to false to prevent jQuery from processing the data
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

    function previewImage(event) {
        event.preventDefault();
        var input = event.target;
        var reader = new FileReader();
        reader.onload = function(){
          var dataURL = reader.result;
          var img = document.getElementById('placeholderImage'); 
          img.src = dataURL;
        };
        reader.readAsDataURL(input.files[0]);
    }

    setAllBinds()
})
