import React, { useState, useRef, useCallback} from "react";

function CupCreator() {
    const nameInputRef = useRef(null);
    const descriptionInputRef = useRef(null);
    const priceInputRef = useRef(null);

    const flavours = [];
    const toppings = [];
    const fruits = toppings.filter(item => item.type === "Fruit");
    const sweets = toppings.filter(item => item.type === "Sweets");
    const sauce = toppings.filter(item => item.type === "Sauce");

    const handleSubmit = (event) => {
        event.preventDefault();

        const name = nameInputRef.current.value;
        const description = descriptionInputRef.current.value;
        const price = priceInputRef.current.value;

        const postData = new FormData();
        postData.append('name', name);
        postData.append('description', description);
        postData.append('price', price);

        fetch('/api/v2/ice/cups', {
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
        priceInputRef.current.value = '';
    }


    return (
        <>
            <div class="offcanvas offcanvas-start" tabindex="-1" id="cupCreator" aria-labelledby="cupCreator">
                <div class="offcanvas-header">
                    <h5 class="offcanvas-title" id="cupCreator">Becher</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>

                <div class="offcanvas-body">
                    {/* <hr class="hr-text gradient" data-content="Eis"> */}
                    <table class="table">
                        <tbody>
                            {flavours.map(element => {
                                return (
                                    <tr>
                                        <th scope="row"></th>
                                        <td><span text={element.name}></span></td>
                                        <td>
                                            <div class="input-group">
                                                <button class="btn btn-outline-secondary btn-number" data-type="minus" type="button"><span class="bi bi-dash-lg"></span></button>
                                                <input type="number" class="form-control" value="0" min="0" max="30" />
                                                <button class="btn btn-outline-secondary btn-number" data-type="minus" type="button"><span class="bi bi-plus-lg"></span></button>
                                            </div>
                                        </td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>

                    {/* <hr class="hr-text gradient" data-content="Topping"> */}

                    <table class="table">
                        <tbody>
                            {fruits.map(element => {
                                return (
                                    <tr>
                                        <th scope="row"></th>
                                        <td><span text={element.name}></span></td>
                                        <td>
                                            <div class="input-group">
                                                <button class="btn btn-outline-secondary btn-number" type="button"><span class="bi bi-dash-lg"></span></button>
                                                <input type="number" class="form-control" value="0" min="0" max="30" />
                                                <button class="btn btn-outline-secondary btn-number" type="button"><span class="bi bi-plus-lg"></span></button>
                                            </div>
                                        </td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>


                    <table class="table">
                        <tbody>
                            {sweets.map(element => {
                                return (
                                    <tr>
                                        <th scope="row"></th>
                                        <td><span text={element.name}></span></td>
                                        <td>
                                            <div class="input-group">
                                                <button class="btn btn-outline-secondary btn-number" type="button"><span class="bi bi-dash-lg"></span></button>
                                                <input type="number" class="form-control" value="0" min="0" max="30" />
                                                <button class="btn btn-outline-secondary btn-number" type="button"><span class="bi bi-plus-lg"></span></button>
                                            </div>
                                        </td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>


                    <table class="table">
                        <tbody>
                            {sauce.map(element => {
                                return (
                                    <tr>
                                        <th scope="row"></th>
                                        <td><span text={element.name}></span></td>
                                        <td>
                                            <div class="input-group">
                                                <button class="btn btn-outline-secondary btn-number" type="button"><span class="bi bi-dash-lg"></span></button>
                                                <input type="number" class="form-control" value="0" min="0" max="30" />
                                                <button class="btn btn-outline-secondary btn-number" type="button"><span class="bi bi-plus-lg"></span></button>
                                            </div>
                                        </td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>
                    
                    <hr/>
                    
                    <form method="POST" onSubmit={handleSubmit}>
                        <div class="row">
                            <div class="col-12">
                                <div class="form-floating">
                                    <textarea class="form-control" placeholder="Leave a comment here" tyle="height: 100px" ref={descriptionInputRef}/>
                                    <label for="cupDescriptionInput">Kommentar</label>
                                    {/* <small class="form-text text-muted" id="cupDescriptionTextAreaCount">0/255</small> */}
                                </div>
                            </div>

                            <div class="col-12">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Becher" ref={nameInputRef}/>
                                    <input type="number" class="form-control" min="1" step="any" valur="0" ref={priceInputRef} />
                                    <button type="submit" class="btn btn-outline-primary" data-bs-dismiss="offcanvas" aria-label="Close">Speichern</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </>
    )
}

function Cup({cups}) {
    return (
        <>
            <div className="row my-2 border rounded">
                <div className="col-md-12 mt-2 ">
                    <p>Cups</p>
                </div>
                <div className="col-md-12 mt-2">
                    <div class="row row-cols-1 row-cols-md-5 my-2">
                        {cups.map(element => {
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

                        <div class="col">
                            <div class="card h-100" aria-hidden="true">
                                <div class="card-body">
                                    <h5 class="card-title placeholder-glow"> 
                                        <span class="placeholder col-6"></span>
                                    </h5>
                                    <p class="card-text placeholder-glow">
                                        <span class="placeholder col-7"></span>
                                        <span class="placeholder col-4"></span>
                                        <span class="placeholder col-4"></span>
                                    </p>
                                    <a tabindex="-1" class="btn btn-primary col-6" data-bs-toggle="offcanvas" href="#cupCreator" role="button" aria-controls="cupCreator">
                                        Becher hinzufügen
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <CupCreator />
        </>
    );
}

export default Cup;