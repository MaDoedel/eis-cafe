import React, { useState, useEffect, useCallback} from "react";

function Flavour({flavours}) {
    return (
        <>
            <div className="row my-2">
                <div className="col-md-12 mt-2 border rounded">
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

function Ice() {
    const [flavours, setFlavours] = useState({});
    const [toppings, setToppings] = useState({});


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

    useEffect(() => {
        fetchFlavours()
        fetchToppings()
    },[fetchFlavours, fetchToppings])

    return (
        <div>
            <div className="row">
                <div className="col-md-1"></div>
                <div className="col-md-10">
                    <Flavour flavours={flavours}/>
                </div>
                <div className="col-md-1"></div>
            </div>
        </div>
    );
  }
  
  export default Ice;
  
  