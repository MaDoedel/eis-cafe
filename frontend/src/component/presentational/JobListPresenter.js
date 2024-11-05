import React, {useState, useEffect} from "react";

function JobListPresenter() {
    const [jobRequests, setJobRequests] = useState([]);

    const fetchJobRequests = async () => {
        try {
            const token = localStorage.getItem('token');

            const response = await fetch('/api/v2/jobs', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            const data = await response.json();
            
            if (Array.isArray(data)) {
                setJobRequests(data);
            } else {
                console.error("Expected an array but got:", data);
                setJobRequests([]); 
            }
        } catch (error) {
            console.error("Failed to fetch job requests:", error);
            setJobRequests([]);
        }
    };

    useEffect(() => {
        fetchJobRequests();
    }, []);

    const handleAccept = (event, jobRequestId) => {
        event.preventDefault();
        const token = localStorage.getItem('token');

        fetch(`/api/v2/jobs/${jobRequestId}/accept`, {
            method: 'DELETE',
            headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
            }
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
            fetchJobRequests();
        })
        .catch((error) => {
            alert(error.message);
        });
    };

    const handleReject = (event, jobRequestId) => {
        event.preventDefault();
        const token = localStorage.getItem('token');

        fetch(`/api/v2/jobs/${jobRequestId}/reject`, {
            method: 'DELETE',
            headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
            }
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
            fetchJobRequests();
        })
        .catch((error) => {
            alert(error.message);
        });
    };

    const handleDownload = async (event, jobRequestId) => {
        event.preventDefault();
        const token = localStorage.getItem('token');

        const downloadUrl = `/api/v2/jobs/${jobRequestId}`;

        try {
            const response = await fetch(downloadUrl, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
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
            <div className= "row border rounded mt-2">
                <div className="col-md-12">
                    <h1 className="text-center">Bewerbungen</h1>
                    <p className="text-center">Einblick in offene Bewerbungsprozesse mit erhältlichem Lebenslauf und Entscheidungsmäglichkeiten</p>
                </div>
                <div className="col-md-12">
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
                                    <th scope="col">Entscheiden</th>
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
                                        <button class="btn btn-outline-primary" onClick={(event) => handleDownload(event, jobRequest.jobRequestId)}>Lebenslauf</button>
                                    </span></td>
                                    <td>
                                        <div className="d-flex justify-content-center input-group">
                                            <button className="btn btn-outline-success border-end-0" onClick={(event) => handleAccept(event, jobRequest.jobRequestId)}>Annehmen</button>
                                            <button className="btn btn-outline-danger border-start-0" onClick={(event) => handleReject(event, jobRequest.jobRequestId)}>Ablehnen</button>
                                        </div>
                                    </td>
                                </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </>
    );
}

export default JobListPresenter;