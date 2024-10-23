import React from "react";
import IceContainer from "./component/container/IceContainer";

function Topping({}) {
    return (
        <div className="row my-2 border rounded">
            <div className="col-md-12 mt-2 ">
                <p>Toppings</p>
            </div>
            <div className="col-md-12 mt-2">
                <ul className="nav nav-tabs" id="myTab" role="tablist">
                    <li className="nav-item" role="presentation">
                        <button className="nav-link active" id="fruits-tab" data-bs-toggle="tab" data-bs-target="#fruits" type="button" role="tab" aria-controls="fruits" aria-selected="true">Früchte und Obst</button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button className="nav-link" id="candy-tab" data-bs-toggle="tab" data-bs-target="#candy" type="button" role="tab" aria-controls="candy" aria-selected="false">Süßigkeiten</button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button className="nav-link" id="sauce-tab" data-bs-toggle="tab" data-bs-target="#sauce" type="button" role="tab" aria-controls="sauce" aria-selected="false">Soßen</button>
                    </li>
                </ul>

                <div className="tab-content">
                    <div className="tab-pane active" id="fruits" role="tabpanel" aria-labelledby="fruits-tab" tabindex="0">
                        <IceContainer api={"toppings/fruits"}/>
                    </div>
                    <div className="tab-pane" id="candy" role="tabpanel" aria-labelledby="candy-tab" tabindex="0">
                        <IceContainer api={"toppings/candy"}/>
                    </div>
                    <div className="tab-pane" id="sauce" role="tabpanel" aria-labelledby="sauce-tab" tabindex="0">
                        <IceContainer api={"toppings/sauce"}/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Topping;