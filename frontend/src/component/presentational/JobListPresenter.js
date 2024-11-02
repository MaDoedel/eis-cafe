import React, {useState, useEffect} from "react";

function JobListPresenter() {
    const [jobRequests, setJobRequests] = useState([]);

    useEffect(() => {
        fetch('/api/v2/jobs')
        .then(response => response.json())
        .then(data => {
            setJobRequests(data);
        });
    }, []);

    const handleAccept = (event, jobRequestId) => {
        event.preventDefault();

        fetch(`/api/v2/jobs/${jobRequestId}/accept`, {
            method: 'DELETE',
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
        })
        .catch((error) => {
            alert(error.message);
        });
    };

    const handleReject = (event, jobRequestId) => {
        event.preventDefault();

        fetch(`/api/v2/jobs/${jobRequestId}/reject`, {
            method: 'DELETE',
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
        })
        .catch((error) => {
            alert(error.message);
        });
    };

    const handleDownload = async (event, jobRequestId) => {
        event.preventDefault();

        const downloadUrl = `/api/v2/jobs/${jobRequestId}`;

        try {
            const response = await fetch(downloadUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
    
            if (!response.ok) throw new Error('Network response was not ok');
    
            // somehow nothing else works, so we have to do it this way
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `${jobRequestId}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link); 
        } catch (error) {
            console.error('Download failed:', error);
        }
    }

    return (
        <>
            {jobRequests.length !== 0 && (
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Name</th>
                            <th scope="col">Nachname</th>
                            <th scope="col">E-Mail</th>
                            <th scope="col">Bewerbung für</th>
                            <th scope="col">Lebenslauf</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {jobRequests.map(jobRequest => (
                        <tr>
                            <th scope="row" >{jobRequest.jobRequestId}</th>
                            <td><span>{jobRequest.name}</span></td>
                            <td><span>{jobRequest.surname}</span></td>
                            <td><span>{jobRequest.email}</span></td>
                            <td><span>{jobRequest.applicantType}</span></td>
                            <td><span>
                                <button class="btn btn-primary" onClick={(event) => handleDownload(event, jobRequest.jobRequestId)}>Lebenslauf</button>
                            </span></td>
                            <td><span>
                                <button class="btn btn-primary" onClick={(event) => handleAccept(event, jobRequest.jobRequestId)}>Annehmen</button>
                                <button class="btn btn-primary" onClick={(event) => handleReject(event, jobRequest.jobRequestId)}>Ablehnen</button>
                            </span></td>
                        </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </>
    );
}

export default JobListPresenter;