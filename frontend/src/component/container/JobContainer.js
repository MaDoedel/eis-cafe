import React, {useRef} from "react";
import JobPresenter from "../presentational/JobPresenter";

function JobContainer() {
    const nameInputRef = useRef();
    const surnameInputRef = useRef();
    const emailInputRef = useRef();
    const commentInputRef = useRef();
    const cvInputRef = useRef();
    const jobDescription = useRef(null);
    const fileTitle = useRef();


    function handleSubmit(event) {
        event.preventDefault();
        alert(nameInputRef.current.value);
        alert(surnameInputRef.current.value);
        alert(emailInputRef.current.value);
        alert(commentInputRef.current.value);
        alert(cvInputRef.current.files[0].name);
        alert(jobDescription.current.value);

        const formData = new FormData();
        formData.append('name', nameInputRef.current.value);
        formData.append('surname', surnameInputRef.current.value);
        formData.append('email', emailInputRef.current.value);
        formData.append('comment', commentInputRef.current.value);
        formData.append('cv', cvInputRef.current.files[0]);
        formData.append('jobDescription', jobDescription.current.value);

        fetch('/api/v2/jobs', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok) {
                alert('Bewerbung erfolgreich eingereicht');
            } else {
                alert('Fehler beim Einreichen der Bewerbung');
            }
        });

        nameInputRef.current.value = '';
        surnameInputRef.current.value = '';
        emailInputRef.current.value = '';
        commentInputRef.current.value = '';
        cvInputRef.current.value = '';
        jobDescription.current.value = "MiniJob";
        fileTitle.current.textContent = 'Lebenslauf';
        
    }

    

    return (
        <JobPresenter 
        nameInputRef={nameInputRef}
        surnameInputRef={surnameInputRef}
        emailInputRef={emailInputRef}
        commentInputRef={commentInputRef}
        cvInputRef={cvInputRef}
        jobDescription={jobDescription}
        handleSubmit={handleSubmit}
        fileTitle={fileTitle}
        />
    );
}

export default JobContainer;