import React, {useRef} from "react";

function JobPresenter({ nameInputRef, surnameInputRef, emailInputRef, commentInputRef, cvInputRef, jobDescriptionRef, handleSubmit, fileTitle}) {

    const handleCVInput = (event) => {
        event.preventDefault();
        cvInputRef.current.click();
    }

    const previewText = (event) => {
        event.preventDefault();

        var fileName = cvInputRef.current.files[0].name;
        fileTitle.current.textContent = fileName;
    };

    return (
        <form method="POST" enctype="multipart/form-data">
            <div class="col-md-6">
                <div class="input-group">
                    <span class="input-group-text">Vorname</span>
                    <input type="text" class="form-control" placeholder="Max" aria-label="name" required ref={nameInputRef}/>
                </div>
            </div>

            <div class="col-md-6">
                <div class="input-group">
                    <span class="input-group-text">Nachname</span>
                    <input type="text" class="form-control" placeholder="Mustermann" aria-label="surname" required ref={surnameInputRef} />
                </div>
            </div>

            <div class="col-md-6">
                <div class="input-group">
                    <span class="input-group-text" id="MailID">@</span>
                    <input type="text" class="form-control" placeholder="max.mustermann@example.com" aria-label="email" required  ref={emailInputRef}/>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="input-group">
                    <label class="input-group-text" for="applicantTypeInput">Bewerbung für</label>
                    <select class="form-select" id="applicantTypeInput" ref={jobDescriptionRef}>
                        <option selected value="MiniJob">MiniJob</option>
                        <option value="Praktikum">Praktikum</option>
                    </select>
                </div>   
            </div>

            <div class="col-md-6">
                <div class="form-floating">
                    <textarea class="form-control" placeholder="Kommentar" style={{height: '100px'}} ref={commentInputRef}></textarea>
                    <label for="applicantCommentInput">Kommentar</label>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="input-group">
                    <div class="card" style={{width: '14rem'}}>
                        <img id="pdfPreviewCVImage" src="/logo192.png" class="card-img-top" alt="..." />
                        <div class="card-body">
                            <h5 class="card-title">Lebenslauf</h5>
                            <p class="card-text" ref={fileTitle}>Lebenslauf</p>
                            <button tabindex="0" class="btn btn-primary" onClick={handleCVInput}>Hochladen</button>
                            <input type="file" class="d-none" accept="application/pdf" ref={cvInputRef} onChange={previewText} required/>
                        </div>
                    </div>
                </div>
            </div>

            <button tabindex="0" type="submit" class="btn btn-primary" onClick={handleSubmit} >Anfrage senden</button>
        </form>
    )
}

export default JobPresenter;