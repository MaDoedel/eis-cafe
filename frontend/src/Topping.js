import React, { useCallback, useRef} from "react";
import IceContainer from "./component/container/IceContainer";

function ToppingContent({toppings = [], type}) {
    const nameInputRef = useRef(null);
    const descriptionInputRef = useRef(null);
    const veganInputRef = useRef(null);

    const handleSubmit = (event) => {
        event.preventDefault();

        const name = nameInputRef.current.value;
        const description = descriptionInputRef.current.value;
        const vegan = veganInputRef.current.checked ? true : false;


        const postData = new FormData();
        postData.append('name', name);
        postData.append('description', description);
        postData.append('type', type);
        postData.append('vegan', vegan);

        fetch('/api/v2/ice/toppings', {
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
        veganInputRef.current.checked = "false";
    }

    return (
        <>
            <div class="row-cols-1 row-cols-md-5 my-2">
                {toppings.map(topping => {
                    return (
                        <div className="col">
                            <div className="card h-100" style={{maxWidth: '540px'}}>
                                <div class="row">
                                    <div class="col-md-4">
                                        <img src="/logo192.png" class="img-fluid rounded-start text-center" alt="..."/>
                                    </div>
                                    <div class="col-md-8">
                                        <div className="card-body">
                                            <h5 className="card-title">{topping.name}</h5>
                                            <p className="card-text">{topping.description}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                })}

                <div className="col">
                    <form enctype="multipart/form-data" onSubmit={handleSubmit}>
                        <div className="card h-100" >
                            <div class="row">
                                <div class="col-md-4">
                                    <img src="/logo192.png" class="img-fluid rounded-start text-center" alt="..."/>
                                </div>
                                <div class="col-md-8">
                                    <div className="card-body">
                                        <input className="card-title form-control text-start" placeholder="Mintberry Crunch" ref={nameInputRef}/>
                                        <input className="card-text form-control text-start" placeholder="creamy" ref={descriptionInputRef}/>
                                        <div className="row mt-2">
                                            <div class="col-md-6">
                                                <button tabindex="0" type="submit" class="btn btn-outline-primary text-center rounded-bottom text-start" >Speichern</button>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="text-start form-check form-switch form-check-reverse ">
                                                    <input defaultChecked={type === "Fruit"} class="form-check-input" type="checkbox" ref={veganInputRef} disabled={type === "Fruit"} />
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
        </>
    );
}

function Topping({}) {
    return (
        <>
            <div className="row my-2 border rounded">
                <div className="col-md-12 mt-2 ">
                    <p>Toppings</p>
                </div>
                <div className="col-md-12 mt-2">
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="fruits-tab" data-bs-toggle="tab" data-bs-target="#fruits" type="button" role="tab" aria-controls="fruits" aria-selected="true">Früchte und Obst</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="sweets-tab" data-bs-toggle="tab" data-bs-target="#sweets" type="button" role="tab" aria-controls="sweets" aria-selected="false">Süßigkeiten</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="sauce-tab" data-bs-toggle="tab" data-bs-target="#sauce" type="button" role="tab" aria-controls="sauce" aria-selected="false">Soßen</button>
                        </li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="fruits" role="tabpanel" aria-labelledby="fruits-tab" tabindex="0">
                            <IceContainer api={"toppings/fruits"}/>
                        </div>
                        <div class="tab-pane" id="sweets" role="tabpanel" aria-labelledby="sweets-tab" tabindex="0">
                            <IceContainer api={"toppings/candy"}/>
                        </div>
                        <div class="tab-pane" id="sauce" role="tabpanel" aria-labelledby="sauce-tab" tabindex="0">
                            <IceContainer api={"toppings/sauce"}/>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Topping;