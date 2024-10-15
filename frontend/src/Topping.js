import React, { useCallback} from "react";

function ToppingContent({toppings = []}) {
    return (
        <>
            <div class="row">
                {toppings.map(topping => {
                    return (
                        <div className="col">
                            <div className="card" key={topping.id}>
                                <img src="..." class="card-img-top" alt="..." />
                                <div className="card-body">
                                    <h5 className="card-title">{topping.name}</h5>
                                    <p className="card-text">{topping.description}</p>
                                </div>
                            </div>
                        </div>
                    )
                })}
            </div>
        </>
    );
}

function Topping({toppings = []}) {

    const filterByType = useCallback((type) => {
        return toppings ? toppings.filter(item => item.type === type) : [];
    }, [toppings]);
    
    
    // Should consider using api calls with different params for each type... could reduce the load for people who don't need all the data
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
                            <ToppingContent toppings={filterFruits()}/>
                        </div>
                        <div class="tab-pane" id="sweets" role="tabpanel" aria-labelledby="sweets-tab" tabindex="0">
                            <ToppingContent toppings={filterSweets()}/>
                        </div>
                        <div class="tab-pane" id="sauce" role="tabpanel" aria-labelledby="sauce-tab" tabindex="0">
                            <ToppingContent toppings={filterSauce()}/>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Topping;