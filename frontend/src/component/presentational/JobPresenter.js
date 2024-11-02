import React, {useRef} from "react";

function JobPresenter({ nameInputRef, surnameInputRef, emailInputRef, commentInputRef, cvInputRef, jobDescription, handleSubmit, fileTitle}) {

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
            <div className="row  my-2 ">
                <div className="col-md-6 mt-2">
                    <div className="input-group">
                        <span className="input-group-text">Vorname</span>
                        <input type="text" className="form-control" placeholder="Max" aria-label="name" required ref={nameInputRef}/>
                    </div>
                </div>

                <div className="col-md-6 mt-2">
                    <div className="input-group">
                        <span className="input-group-text">Nachname</span>
                        <input type="text" className="form-control" placeholder="Mustermann" aria-label="surname" required ref={surnameInputRef} />
                    </div>
                </div>

                <div className="col-md-6 mt-2">
                    <div className="input-group">
                        <span className="input-group-text" id="MailID">@</span>
                        <input type="text" className="form-control" placeholder="max.mustermann@example.com" aria-label="email" required  ref={emailInputRef}/>
                    </div>
                </div>

                <div className="col-md-6 mt-2">
                    <div className="input-group">
                        <label className="input-group-text" for="applicantTypeInput">Bewerbung für</label>
                        <select className="form-select" id="applicantTypeInput" ref={jobDescription}>
                            <option selected value="MiniJob">MiniJob</option>
                            <option value="Praktikum">Praktikum</option>
                        </select>
                    </div>   
                </div>

                <div className="col-md-12 mt-2">
                    <div className="form-floating">
                        <textarea className="form-control" placeholder="Kommentar" style={{height: '100px'}} ref={commentInputRef}></textarea>
                        <label for="applicantCommentInput">Kommentar</label>
                    </div>
                </div>


            </div>

            <div className="col-md-4 mt-2">
                <div className="input-group ">
                    <div className="card" style={{width: '14rem'}}>
                        <img id="pdfPreviewCVImage" src="/logo192.png" className="card-img-top" alt="..." />
                        <div className="card-body">
                            <h5 className="card-title">Lebenslauf</h5>
                            <p className="card-text" ref={fileTitle}>Lebenslauf</p>
                            <button tabindex="0" className="btn btn-primary" onClick={handleCVInput}>Hochladen</button>
                            <input type="file" className="d-none" accept="application/pdf" ref={cvInputRef} onChange={previewText} required/>
                        </div>
                    </div>
                </div>
            </div>

            <div className="col-md-12 my-2">
                <button tabindex="0" type="submit" className="btn btn-primary" onClick={handleSubmit} >Anfrage senden</button>
            </div>

            
        </form>
    )
}

export default JobPresenter;