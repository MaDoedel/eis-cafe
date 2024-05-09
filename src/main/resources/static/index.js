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
        var self = this;
        inputElement.addEventListener('input', function() {
            self.checker.input = inputElement.value;

            if (!self.checker.check()) {
                inputElement.value = inputElement.value.substring(0, 255);
            }

            document.getElementById('applicantCommentTextAreaCount').innerText = inputElement.value.length + '/255';
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
        return /^[a-zA-Z]+$/.test(this.input);
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
                $('#placeholderImage').on("click", selectImage);
                $('#formFile').on("change", previewImage);
                ListeningIceForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'iceCreamNameInput'), new InputListenerDecorator(new TextRegexChecker(''), 'iceCreamDescriptionInput')]);
                ListeningIceForm.isValid();

            },
            error: function(){
                alert("Failed refetching ice")
            }
        });
        $('#iceCreamForm')[0].reset()
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

    function setAllBinds() {
        //var formData = new FormData($('#iceCreamForm')[0]);
        const ListeningIceForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'iceCreamNameInput'), new InputListenerDecorator(new TextRegexChecker(''), 'iceCreamDescriptionInput')]);
        const ListeningJobsForm = new ListeningForm([new InputListenerDecorator(new TextRegexChecker(''), 'applicantNameInput'), new InputListenerDecorator(new TextRegexChecker(''), 'applicantSurnameInput'), new InputListenerDecorator(new EmailRegexChecker(''), 'applicantMailInput'), new TextAreaListenerDecorator(new TextLengthChecker(''), 'applicantCommentTextArea')]);

        ListeningIceForm.isValid();
        ListeningJobsForm.isValid();

        $('#userLoginForm').submit(onLoginSubmit);
        $('#iceCreamForm').submit(onIceCreamFormSubmit);
        $('#jobsForm').submit(onJobsFormSubmit);

        $(document).on("click",'.flavour-delete-btn', onFlavourDelete);
        $(document).on("click",'.cv-download-btn', onDownloadCV); 

        $(document).on("click",'.request-accept-btn', onRequestAccept);
        $(document).on("click",'.request-reject-btn', onRequestReject); 

        $('#placeholderImage').on("click", selectImage);
        $('#formFile').on("change", previewImage);

        $('#CVButton').on("click", selectCV);
        $('#CVInput').on("change", previewCV);
    }

    function onRequestAccept(e) {
        e.preventDefault();
        $.ajax({
            url: '/jobs/accept/' + $(this).attr('data-id'),
            type: 'delete',
            success: function(){
                refetchJobs()
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
                refetchJobs()
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
            },
            error: function(){
                alertPlaceholder.innerHTML = [
                    `<div class="alert alert-danger alert-dismissible" role="alert"> Naah </div>`
                ].join('')
                $('#userLoginForm')[0].reset();
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
                var img = document.getElementById('pdfPreviewCVImage'); 
                img.src = dataURL;
            };
            reader.readAsDataURL(file);
        }
    }

    function onJobsFormSubmit(e) {
        e.preventDefault();
        const alertPlaceholder = document.getElementById('jobsFormAlert');
        const jobsForm = new Form([new TextRegexChecker('applicantNameInput'), new TextRegexChecker('applicantSurnameInput'), new EmailRegexChecker('applicantMailInput'), new TextLengthChecker('applicantCommentTextArea')]);

        
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
        
        const iceForm = new Form([new TextRegexChecker('iceCreamNameInput'), new TextRegexChecker('iceCreamDescriptionInput')]);
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

    setAllBinds()
})
