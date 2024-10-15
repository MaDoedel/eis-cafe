import React, { useState, useEffect, useCallback} from "react";
import Flavour from "./Flavour";
import Topping from "./Topping";


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
            const response = await fetch('/api/v2/ice/flavours');
            const newFlavours = await response.json();
    
            if (response.ok) {
                setFlavours(newFlavours);
            } else {
                throw new Error(response.status);
            }
        } catch (error) {
            console.error(error);
        }
    }, []);

    const fetchCups = useCallback(async () => {
        try {
            const response = await fetch('/api/v2/ice/cups');
            const newCups = await response.json();
    
            if (response.ok) {
                setCups(newCups);
            } else {
                throw new Error(response.status);
            }
        } catch (error) {
            console.error(error);
        }
    }, []);

    const fetchToppings = useCallback(async () => {
        try {
            const response = await fetch('/api/v2/ice/toppings');
            const newToppings = await response.json();
    
            if (response.ok) {
                setToppings(newToppings);
            } else {
                throw new Error(response.status);
            }
        } catch (error) {
            console.error(error);
        }
    }, []);

    // Maybe GraphQL
    useEffect(() => {
        Promise.all([
            
            fetchFlavours('/api/v2/ice/flavours'),
            fetchToppings('/api/v2/ice/toppings'),
            fetchCups('/api/v2/ice/cups')
        ]);
    }, []);

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
  
  