import React, { useRef } from "react";


function Flavour({flavours}) {
    const nameInputRef = useRef(null);
    const descriptionInputRef = useRef(null);
    const veganInputRef = useRef(null);
    const imageInputRef = useRef(null);
    const previewInputRef = useRef(null);


    const handleImageInput = (event) => {
        event.preventDefault();
        imageInputRef.current.click();
    }

    const previewImage = (event) => {
        event.preventDefault();

        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onloadend = () => {
            previewInputRef.current.src = reader.result;
        };
        reader.readAsDataURL(file);
    };

    const handleDelete = (event) => {
        event.preventDefault();

        const id = event.target.id;
        fetch(`/api/v2/ice/flavours/${id}`, {
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


    const handleSubmit = (event) => {
        event.preventDefault();

    
        const name = nameInputRef.current.value;
        const description = descriptionInputRef.current.value;
        const image = imageInputRef.current.files[0];

        const postData = new FormData();
        postData.append('name', name);
        postData.append('vegan', veganInputRef.current.checked ? true : false);
        postData.append('description', description);
        postData.append('image', image);

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
        veganInputRef.current.checked = 'false'
        imageInputRef.current.value = '';
        previewInputRef.current.src = '/logo192.png';
    };

    return (
        <>
            <div className="row my-2 border rounded">
                <div className="col-md-12 mt-2 ">
                    <p>Flavours</p>
                </div>
                <div className="col-md-12 mt-2">
                    <div class="row row-cols-1 row-cols-md-5">
                        {flavours.map(element => {
                            if (element.id ===  0) {return null}
                            else {
                                return (
                                    <div className="col my-1">
                                        <div className="card h-100" style={{maxWidth: '540px'}}>
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <img src={`/api/v2/ice/flavours/${element.id}/images`} class="img-fluid rounded-start text-center" alt="..."/>
                                                </div>
                                                <div class="col-md-8">
                                                    <div className="card-body">
                                                        <h5 className="card-title">{element.name}</h5>
                                                        <p className="card-text">{element.description}</p>
                                                    </div>
                                                    <div class="row my-2">
                                                        <div class="col-md-6"></div>
                                                        <div class="col-md-6">
                                                            <div class="input-group text-end">
                                                                <button class="btn btn-outline-danger border-end-0" id={element.id} onClick={handleDelete}><span class="bi bi-x-lg"></span></button>
                                                                <button class="btn btn-outline-warning border-start-0"><span class="bi bi-pen"></span></button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            }
                        )}

                        <div className="col my-1">
                            <form encType="multipart/form-data" onSubmit={handleSubmit}>
                                <div className="card h-100" style={{maxWidth: '540px'}} >
                                    <div class="row">
                                        <div class="col-md-4">
                                            <img src="/logo192.png" class="img-fluid rounded-start text-center" alt="..." ref={previewInputRef} onClick={handleImageInput}/>
                                            <input type="file" class="d-none" ref={imageInputRef} onChange={previewImage} required/>
                                        </div>
                                        <div class="col-md-8">
                                            <div className="card-body">
                                                <input required className="card-title form-control text-start" placeholder="Mintberry Crunch" ref={nameInputRef}/>
                                                <input required className="card-text form-control text-start" placeholder="creamy" ref={descriptionInputRef}/>
                                                <div className="row mt-2">
                                                    <div class="col-md-6">
                                                        <button tabindex="0" type="submit" class="btn btn-outline-primary text-center rounded-bottom text-start" >Speichern</button>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="text-start form-check form-switch form-check-reverse ">
                                                            <input required defaultChecked="false" class="form-check-input" type="checkbox" ref={veganInputRef} />
                                                            <label class="form-check-label" for="sauceVeganInput">Vegan</label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
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