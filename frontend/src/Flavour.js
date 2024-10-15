import React, { useRef } from "react";


function Flavour({flavours}) {
    const nameInputRef = useRef(null);
    const descriptionInputRef = useRef(null);


    const handleSubmit = (event) => {
        event.preventDefault();

    
        const name = nameInputRef.current.value;
        const description = descriptionInputRef.current.value;

        const postData = new FormData();
        postData.append('name', name);
        postData.append('description', description);

        fetch('/api/v2/ice/flavours', {
            method: 'POST',
            body: postData,
        })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
        })
        .catch((error) => {
            alert(error.message);
        });
    
    
        nameInputRef.current.value = '';
        descriptionInputRef.current.value = '';
    
    };

    return (
        <>
            <div className="row my-2 border rounded">
                <div className="col-md-12 mt-2 ">
                    <p>Flavours</p>
                </div>
                <div className="col-md-12 mt-2">
                    <div class="row row-cols-1 row-cols-md-5 my-2">
                        {flavours.map(element => {
                            if (element.id ===  0) {return null}
                            else {
                                return (
                                    <div className="col">
                                        <div className="card h-100" key={element.id}>
                                            <div className="card-body">
                                                <h5 className="card-title">{element.name}</h5>
                                                <p className="card-text">{element.description}</p>
                                            </div>
                                        </div>
                                    </div>
                                )
                            }}
                        )}

                        <div className="col">
                            <form enctype="multipart/form-data" onSubmit={handleSubmit}>
                                <div className="card h-100">
                                    <div className="card-body">
                                        <input className="card-title form-control text-start" placeholder="Mintberry Crunch" ref={nameInputRef}/>
                                        <input className="card-text form-control text-start" placeholder="creamy" ref={descriptionInputRef}/>
                                    </div>
                                    <button tabindex="0" type="submit" class="btn btn-outline-primary text-center" >Speichern</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Flavour;