// Validation Bridge
// left
class AbstractFormValidator{
    constructor(implList){
        this.implList = implList;
    }
    isValid(){}
}

// Works fine for non responsive forms
class Form extends AbstractFormValidator {
    constructor(implList){
        super(implList)
    }
    isValid(){
        for (let implementation of this.implList) {
            if (!implementation.check()){
                return false; 
            }
        }
        return true;
    }
}


// Works for responsive forms
class ListeningForm extends AbstractFormValidator {
    isValid(){
        for (let implementation of this.implList) {
            implementation.check();
        }
    }
}

// right
class AbstractCheckerImplementation{
    constructor(id){
        if (document.getElementById(id)) {
            this.input = document.getElementById(id).value;
        } else {
            this.input = '';
        }
    }
    check(){}
}


// decorator, is more a proxy, but... who cares 
class CheckerDecorator extends AbstractCheckerImplementation{
    constructor(checker){
        super();
        this.checker = checker;
    }
    check(){}
}

// InputListener
class InputListenerDecorator extends CheckerDecorator{
    constructor(checker, id){
        super(checker);
        this.id = id; 
    }

    check(){
        const inputElement = document.getElementById(this.id);
        var self = this;

        if (inputElement === null) {
            return;
        }
        
        inputElement.addEventListener('input', function() {
            self.checker.input = inputElement.value;
            if (!self.checker.check()) {
                inputElement.classList.remove('is-valid');
                inputElement.classList.add('is-invalid');
            } else {
                inputElement.classList.remove('is-invalid');
                inputElement.classList.add('is-valid');
            }
        });     
    }
}

// TextAreaListener
class TextAreaListenerDecorator extends CheckerDecorator{
    constructor(checker, id){
        super(checker);
        this.id = id; 
    }

    check(){
        const inputElement = document.getElementById(this.id);

        if (inputElement === null) {
            return;
        }

        var self = this;
        inputElement.addEventListener('input', function() {
            self.checker.input = inputElement.value;
            
            if (!self.checker.check()) {
                inputElement.value = inputElement.value.substring(0, 255);
            }

            document.getElementById(this.id +'Count').innerText = inputElement.value.length + '/255';
        });     
    }
}


class TextLengthChecker extends AbstractCheckerImplementation{
    check(){
        return this.input.length <= 255;
    }
}

class TextRegexChecker extends AbstractCheckerImplementation{
    check(){
        return /^[a-zA-Z-_]+$/.test(this.input);
    }
}

class CommentRegexChecker extends AbstractCheckerImplementation{
    check(){
        return /^[a-zA-Z0-9\s\S]*$/.test(this.input);
    }
}

class PriceRegexChecker extends AbstractCheckerImplementation{
    check(){
        return /^\d+(\.\d{1,2})?$/.test(this.input);
    }
}

class EmailRegexChecker extends AbstractCheckerImplementation{
    check(){
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.input);
    }
}

$(document).ready( function() {
    // Validation Client-Side
    
    function refetchIce() {
        $.ajax({
            url: '/ice',
            type: 'get',
            success:function(new_content){
                $("#tab-1").html(new_content)
                $('#iceCreamForm').submit(onIceCreamFormSubmit);

                $('#fruitForm').submit(onFruitFormSubmit);
                $('#candyForm').submit(onCandyFormSubmit);
                $('#sauceForm').submit(onSauceFormSubmit);

                // This must be solvable through patterns and stuff, or react
                $('#placeholderImage').on("click", selectImage);
                $('#placeholderCandiesImage').on("click", selectCandiesImage);
                $('#placeholderSauceImage').on("click", selectSauceImage);
                $('#placeholderFruitsImage').on("click", selectFruitsImage);

                $('#formFile').on("change", previewImage);
                $('#formFileCandies').on("change", previewCandiesImage);
                $('#formFileSauce').on("change", previewSauceImage);
                $('#formFileFruits').on("change", previewFruitsImage);

                ListeningIceForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'iceCreamNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'iceCreamDescriptionInput')]);
                ListeningFruitForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'fruitNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'fruitDescriptionInput')]);
                ListeningCandyForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'candyNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'candyDescriptionInput')]);
                ListeningSauceForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'sauceNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'sauceDescriptionInput')]);
                ListeningCupForm = new ListeningForm([new InputListenerDecorator(new PriceRegexChecker(''), 'cupPriceInput'), new InputListenerDecorator(new TextRegexChecker(''), 'cupNameInput'), new TextAreaListenerDecorator(new TextLengthChecker(''), 'cupDescriptionTextArea')]);

                ListeningIceForm.isValid();
                ListeningFruitForm.isValid();
                ListeningCandyForm.isValid();
                ListeningSauceForm.isValid();
                ListeningCupForm.isValid();               

            },
            error: function(){
                alert("Failed refetching ice")
            }
        });
        $('#iceCreamForm')[0].reset()
    }

    function refetchProfile() {
        $.ajax({
            url: '/profile',
            type: 'get',
            success:function(new_content){
                $("#tab-3").html(new_content)
            },
            error: function(){
                alert("Failed refetching jobs")
            }
        });
    }


    function refetchJobs() {
        $.ajax({
            url: '/jobs',
            type: 'get',
            success:function(new_content){
                $("#tab-3").html(new_content)
                $('#jobsForm').submit(onJobsFormSubmit);
                $('#CVButton').on("click", selectCV);
                $('#CVInput').on("change", previewCV);
                const ListeningJobsForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'applicantNameInput'), new InputListenerDecorator(new TextRegexChecker(''), 'applicantSurnameInput'), new InputListenerDecorator(new EmailRegexChecker(''), 'applicantMailInput'), new TextAreaListenerDecorator(new TextLengthChecker(''), 'applicantCommentTextArea')]);
                ListeningJobsForm.isValid();

            },
            error: function(){
                alert("Failed refetching jobs")
            }
        });
        $('#jobsForm')[0].reset()
    }

    function refetchProfile() {
        $.ajax({
            url: '/profile',
            type: 'get',
            success:function(new_content){
                $("#tab-3").html(new_content)
            },
            error: function(){
                alert("Failed refetching me")
            }
        });
    }

    function setAllBinds() {
        //var formData = new FormData($('#iceCreamForm')[0]);
        const ListeningIceForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'iceCreamNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'iceCreamDescriptionInput')]);
        const ListeningJobsForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'applicantNameInput'), new InputListenerDecorator(new TextRegexChecker(''), 'applicantSurnameInput'), new InputListenerDecorator(new EmailRegexChecker(''), 'applicantMailInput'), new TextAreaListenerDecorator(new TextLengthChecker(''), 'applicantCommentTextArea')]);
        const ListeningCupForm = new ListeningForm([new InputListenerDecorator(new PriceRegexChecker(''), 'cupPriceInput'), new InputListenerDecorator(new TextRegexChecker(''), 'cupNameInput'), new TextAreaListenerDecorator(new TextLengthChecker(''), 'cupDescriptionTextArea')]);
        const ListeningFruitForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'fruitNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'fruitDescriptionInput')]);
        const ListeningCandyForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'candyNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'candyDescriptionInput')]);
        const ListeningSauceForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'sauceNameInput'), new InputListenerDecorator(new CommentRegexChecker(''), 'sauceDescriptionInput')]);



        ListeningIceForm.isValid();
        ListeningJobsForm.isValid();
        ListeningCupForm.isValid();
        ListeningFruitForm.isValid();
        ListeningCandyForm.isValid();
        ListeningSauceForm.isValid();



        $('#userLoginForm').submit(onLoginSubmit);
        $('#iceCreamForm').submit(onIceCreamFormSubmit);
        $('#jobsForm').submit(onJobsFormSubmit);
        $('#addCupForm').submit(onAddCupSubmit);

        $('#fruitForm').submit(onFruitFormSubmit);
        $('#candyForm').submit(onCandyFormSubmit);
        $('#sauceForm').submit(onSauceFormSubmit);

        $('#roleForm').submit(onRoleFormSubmit);


        $(document).on("click",'.file-delete-btn', onFileDelete);
        $(document).on("click",'.flavour-delete-btn', onFlavourDelete);
        $(document).on("click",'.topping-delete-btn', onToppingDelete);
        $(document).on("click",'.cup-delete-btn', onCupDelete);

        $(document).on("click",'.cv-download-btn', onDownloadCV); 
        $(document).on("click",'.btn-number', onCupCreateButtons); 


        $(document).on("click",'.request-accept-btn', onRequestAccept);
        $(document).on("click",'.request-reject-btn', onRequestReject); 


        //$('#showMoreFlavourBtn').on("click", onShowMoreFlavour)

        $('#placeholderImage').on("click", selectImage);
        $('#placeholderCandiesImage').on("click", selectCandiesImage);
        $('#placeholderSauceImage').on("click", selectSauceImage);
        $('#placeholderFruitsImage').on("click", selectFruitsImage);

        $('#formFile').on("change", previewImage);
        $('#formFileCandies').on("change", previewCandiesImage);
        $('#formFileSauce').on("change", previewSauceImage);
        $('#formFileFruits').on("change", previewFruitsImage);

        $('#CVButton').on("click", selectCV);
        $('#CVInput').on("change", previewCV);
    }

    function onCupDelete(e) {
        e.preventDefault(); 

        cups = $(this).attr('data-field');

        $.ajax({
            url: '/ice/deleteCup/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchIce()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    function onToppingDelete(e) {
        e.preventDefault(); 

        cups = $(this).attr('data-field');

        $.ajax({
            url: '/ice/deleteTopping/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchIce()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    function onAddCupSubmit(e) {
        e.preventDefault(); 

        let formData = $(this).serialize(); 

        $.ajax({
          type: 'POST',
          url: '/ice/addCup',
          data: formData,
          success: function () {
            refetchIce();
          },
          error: function () {
            alert('Form submission failed');
          }
        });
    }

    function onRoleFormSubmit(e) {
        e.preventDefault(); 

        let formData = $(this).serialize(); 

        $.ajax({
          type: 'POST',
          url: '/profile/addRole',
          data: formData,
          success: function () {
            refetchProfile();
          },
          error: function () {
            alert('Form submission failed');
          }
        });
    }

    function onCupCreateButtons(e){
        e.preventDefault();
        
        fieldName = $(this).attr('data-field');
        type      = $(this).attr('data-type');
        var input = $("input[name='"+fieldName+"']");
        var currentVal = parseInt(input.val());        
        if (!isNaN(currentVal)) {
            if(type == 'minus') {
                
                if(currentVal > input.attr('min')) {
                    input.val(currentVal - 1).change();
                } else {
                    input.val = 0;
                }
               
            } else if(type == 'plus') {
    
                if(currentVal < input.attr('max')) {
                    input.val(currentVal + 1).change();
                } else {
                    input.val = 30;
                }
            }
        } else {
            input.val(0);
        }
    }

    function onShowMoreFlavour(e){
        e.preventDefault();
        var maxFlavour = $(this).attr('data-count');
    }

    function onRequestAccept(e) {
        e.preventDefault();
        $.ajax({
            url: '/jobs/accept/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchProfile()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    function onRequestReject(e) {
        e.preventDefault();
        $.ajax({
            url: '/jobs/reject/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchProfile()
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

    function onFileDelete(e) {
        e.preventDefault();
        $.ajax({
            url: '/profile/deleteFile/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchProfile()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    function onLoginSubmit(e){
        e.preventDefault();
        const alertPlaceholder = document.getElementById('liveAlertPlaceholder')

        $.ajax({
            url: '/login',
            type: 'post',
            data: $('#userLoginForm').serialize(),
            Credentials: 'include',
            success:function(){
                window.location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // Handle failed login
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-danger alert-dismissible" role="alert"> Something Wrong</div>`
                ].join('')
                $('#userLoginForm')[0].reset();
                
            }
        });            
    }


    function selectImage() {
        document.getElementById('formFile').click();
    }

    function selectCandiesImage() {
        document.getElementById('formFileCandies').click();
    }

    function selectFruitsImage() {
        document.getElementById('formFileFruits').click();
    }

    function selectSauceImage() {
        document.getElementById('formFileSauce').click();
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
                var img = document.getElementById('pdfPreviewCVImage'); 
                img.src = dataURL;
            };
            reader.readAsDataURL(file);
        }
    }

    function onJobsFormSubmit(e) {
        e.preventDefault();
        const alertPlaceholder = document.getElementById('jobsFormAlert');
        const jobsForm = new Form([new TextRegexChecker('applicantNameInput'), new TextRegexChecker('applicantSurnameInput'), new EmailRegexChecker('applicantMailInput'), new CommentRegexChecker('applicantCommentTextArea')]);

        
        if (!jobsForm.isValid()){
            const alertPlaceholder = document.getElementById('jobsFormAlert');
            alertPlaceholder.innerHTML = [
                `<div class="alert alert-danger alert-dismissible" role="alert"> Eingabe unvollständig </div>`
            ].join('')
            return;
        }
        

        var formData = new FormData($('#jobsForm')[0]);

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
                $('#jobsForm')[0].reset();
            },
            error: function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-danger alert-dismissible" role="alert"> Naah </div>`
                ].join('')
            }
        });
    }

    function onIceCreamFormSubmit(e) {
        e.preventDefault();

        var formData = new FormData($('#iceCreamForm')[0]);
        
        const iceForm = new Form([new TextRegexChecker('iceCreamNameInput'), new CommentRegexChecker('iceCreamDescriptionInput')]);
        if(!iceForm.isValid()){
            alert('nnnnnnah');
        }
        

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

    function onFruitFormSubmit(e) {
        e.preventDefault();

        var formData = new FormData($('#fruitForm')[0]);
        
        // const iceForm = new Form([new TextRegexChecker('iceCreamNameInput'), new TextRegexChecker('iceCreamDescriptionInput')]);
        // if(!iceForm.isValid()){
        //     alert('nnnnnnah');
        // }
        

        $.ajax({
            url: '/ice/addFruit',
            type: 'post',
            data: formData,
            contentType: false,
            processData: false, 
            success: function(){
                refetchIce()
            },
            error: function(data){
                alert(data.responseText)
            }
        });
    }

    function onCandyFormSubmit(e) {
        e.preventDefault();

        var formData = new FormData($('#candyForm')[0]);
        
        // const iceForm = new Form([new TextRegexChecker('iceCreamNameInput'), new TextRegexChecker('iceCreamDescriptionInput')]);
        // if(!iceForm.isValid()){
        //     alert('nnnnnnah');
        // }
        

        $.ajax({
            url: '/ice/addCandy',
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

    function onSauceFormSubmit(e) {
        e.preventDefault();

        var formData = new FormData($('#sauceForm')[0]);
        
        // const iceForm = new Form([new TextRegexChecker('iceCreamNameInput'), new TextRegexChecker('iceCreamDescriptionInput')]);
        // if(!iceForm.isValid()){
        //     alert('nnnnnnah');
        // }
        

        $.ajax({
            url: '/ice/addSauce',
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

    function onDownloadCV(e) {
        e.preventDefault();
        var userId = $(this).attr('data-id');
        window.location.href = '/download/cv/' + userId;
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

    function previewCandiesImage(event) {
        event.preventDefault();
        var input = event.target;
        var reader = new FileReader();
        reader.onload = function(){
          var dataURL = reader.result;
          var img = document.getElementById('placeholderCandiesImage'); 
          img.src = dataURL;
        };
        reader.readAsDataURL(input.files[0]);
    }

    function previewFruitsImage(event) {
        event.preventDefault();
        var input = event.target;
        var reader = new FileReader();
        reader.onload = function(){
          var dataURL = reader.result;
          var img = document.getElementById('placeholderFruitsImage'); 
          img.src = dataURL;
        };
        reader.readAsDataURL(input.files[0]);
    }

    function previewSauceImage(event) {
        event.preventDefault();
        var input = event.target;
        var reader = new FileReader();
        reader.onload = function(){
          var dataURL = reader.result;
          var img = document.getElementById('placeholderSauceImage'); 
          img.src = dataURL;
        };
        reader.readAsDataURL(input.files[0]);
    }


    setAllBinds()
})
