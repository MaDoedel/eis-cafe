import React, { useState, useEffect, useCallback} from "react";
import Topping from "./Topping";
import Cup from "./Cup";
import IceContainer from "./component/container/IceContainer";

function Ice() {
    const [cups, setCups] = useState([]);

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

    // Maybe GraphQL
    useEffect(() => {
        Promise.all([
            fetchCups('/api/v2/ice/cups')
        ]);
    }, [fetchCups]);

    return (
        <div>
            <div className="row">
                <div className="col-md-1"></div>
                <div className="col-md-10">
                    <div className="row my-2 border rounded">
                        <div className="col-md-12 mt-2 ">
                            <p>Flavours</p>
                        </div>
                        <IceContainer api={"flavours"}/>
                        {/* <Flavour flavours={flavours}/> */}
                    </div>  
                    <Topping/>
                    <Cup cups={cups}/>
                </div>
                <div className="col-md-1"></div>
            </div>
        </div>
    );
  }
  
  export default Ice;
  
  