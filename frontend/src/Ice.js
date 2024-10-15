import React, { useState, useEffect, useCallback, useReducer} from "react";

function Flavour({flavours}) {
    return (
        <>
            <div className="row my-2 border rounded">
                <div className="col-md-12 mt-2 ">
                    <p>Flavours</p>
                </div>
                <div className="col-md-12 mt-2">
                    {Object.entries(flavours).map(([key, flavour]) => (
                        <div className="card" key={flavour.id}>
                            <div className="card-body">
                                <h5 className="card-title">{flavour.name}</h5>
                                <p className="card-text">{flavour.description}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}



function ToppingContent({toppings}) {
    return (
        <>
            <div class="card-group">
                {Object.entries(toppings).map(([key, topping]) => (
                    <div className="card" key={topping.id}>
                        <img src="..." class="card-img-top" alt="..." />
                        <div className="card-body">
                            <h5 className="card-title">{topping.name}</h5>
                            <p className="card-text">{topping.description}</p>
                        </div>
                    </div>
                ))}
            </div>
        </>
    );
}

function Topping({toppings}) {

    const filterByType = useCallback((type) => {
        return toppings.filter(item => item.type === type);
    }, [toppings]);
    
    const filterFruits = useCallback(() => filterByType("Fruits"), [filterByType]);
    const filterSweets = useCallback(() => filterByType("Sweets"), [filterByType]);
    const filterSauce = useCallback(() => filterByType("Sauce"), [filterByType]);
    
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
                            <ToppingContent toppings={filterFruits}/>
                        </div>
                        <div class="tab-pane" id="sweets" role="tabpanel" aria-labelledby="sweets-tab" tabindex="0">
                            <ToppingContent toppings={filterSweets}/>
                        </div>
                        <div class="tab-pane" id="sauce" role="tabpanel" aria-labelledby="sauce-tab" tabindex="0">
                            <ToppingContent toppings={filterSauce}/>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

function Cups({cups}) {
    return (
        <>
            <div className="row my-2 border rounded">
                <div className="col-md-12 mt-2 ">
                    <p>Cups</p>
                </div>
                <div className="col-md-12 mt-2">
                </div>
            </div>
        </>
    );
}


function Ice() {
    const [flavours, setFlavours] = useState([]);
    const [toppings, setToppings] = useState([]);
    const [cups, setCups] = useState([]);



    const fetchFlavours = useCallback(async () => {
        try {
            const response = await fetch('/getFlavours');
            const newFlavours = await response.json();
          
            if (response.ok) {
                setFlavours(newFlavours);
            } else {
                if (response.status === 404) throw new Error('404, Not found');
                if (response.status === 500) throw new Error('500, internal server error');
              throw new Error(response.status);
            }
          } catch (error) {
            console.error('fetch', error);
          }
          
        }
    , []);

    const fetchToppings = useCallback(async () => {
        try {
            const response = await fetch('/getToppings');
            const newToppings = await response.json();
          
            if (response.ok) {
                setToppings(newToppings);
            } else {
                if (response.status === 404) throw new Error('404, Not found');
                if (response.status === 500) throw new Error('500, internal server error');
              throw new Error(response.status);
            }
          } catch (error) {
            console.error('fetch', error);
          }
          
        }
    , []);

    const fetchCups = useCallback(async () => {
        try {
            const response = await fetch('/getCups');
            const newToppings = await response.json();
          
            if (response.ok) {
                setCups(newToppings);
            } else {
                if (response.status === 404) throw new Error('404, Not found');
                if (response.status === 500) throw new Error('500, internal server error');
              throw new Error(response.status);
            }
          } catch (error) {
            console.error('fetch', error);
          }
          
        }
    , []);

    // Maybe GraphQL
    useEffect(() => {
        fetchFlavours()
        fetchToppings()
        fetchCups()
    },[fetchFlavours, fetchToppings, fetchCups])

    return (
        <div>
            <div className="row">
                <div className="col-md-1"></div>
                <div className="col-md-10">
                    <Flavour flavours={flavours}/>
                    <Topping toppings={toppings}/>
                    <Cups cups={cups}/>
                </div>
                <div className="col-md-1"></div>
            </div>
        </div>
    );
  }
  
  export default Ice;
  
  