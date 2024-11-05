import React, { useRef , useState, useCallback, useEffect} from "react";
import IcePresenter from "../presentational/IcePresenter";


function IceContainer({api}) {
    const nameInputRef = useRef(null);
    const descriptionInputRef = useRef(null);
    const veganInputRef = useRef(null);
    const imageInputRef = useRef(null);
    const previewInputRef = useRef(null);

    const [container, setContainer] = useState([]);
    
    const fetchData = useCallback(async () => {
        try {
            const response = await fetch(`/api/v2/ice/${api}`);
            const newContainer = await response.json();
    
            if (response.ok) {
                setContainer(newContainer);
            } else {
                throw new Error(response.status);
            }
        } catch (error) {
            console.error(error);
        }
    }, [api]);

    useEffect(() => {
        Promise.all([
            fetchData(),
        ]);
    }, [fetchData]);
    
    const handleDelete = (event) => {
        event.preventDefault();
        const token = localStorage.getItem('token');


        const id = event.currentTarget.id;
        fetch(`/api/v2/ice/${api}/${id}`, {
            headers: {'Authorization': `Bearer ${token}`},
            method: 'DELETE',
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
            fetchData();
        })
        .catch((error) => {
            alert(error.message);
        });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const token = localStorage.getItem('token');

        const name = nameInputRef.current.value;
        const description = descriptionInputRef.current.value;
        const image = imageInputRef.current.files[0];

        const postData = new FormData();
        postData.append('name', name);
        postData.append('vegan', veganInputRef.current.checked ? true : false);
        postData.append('description', description);
        postData.append('image', image);

        fetch(`/api/v2/ice/${api}`, {
            method: 'POST',
            headers: {'Authorization': `Bearer ${token}`},
            body: postData,
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
            fetchData();
        })
        .catch((error) => {
            alert(error.message);
        });

        nameInputRef.current.value = '';
        descriptionInputRef.current.value = '';
        veganInputRef.current.checked = 'false'
        imageInputRef.current.value = '';
        previewInputRef.current.src = '/logo192.png';
    };

    return (
        <div>
            <IcePresenter 
            api= {api}
            container={container}
            handleSubmit={handleSubmit}
            handleDelete={handleDelete}
            nameInputRef={nameInputRef}
            descriptionInputRef={descriptionInputRef}
            veganInputRef={veganInputRef}
            imageInputRef={imageInputRef}
            previewInputRef={previewInputRef} 
            />
        </div>
    )
}

export default IceContainer;