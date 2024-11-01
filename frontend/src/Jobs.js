import React from "react";
import JobContainer from "./component/container/JobContainer";

function Jobs() {
  return (
    <div>
        <div className="row">
            <div className="col-md-1"></div>
            <div className="col-md-10">
                <div className="row my-2 border rounded">
                    <div className="col-md-12 mt-2 ">
                        <p>Bewerbunsgformalar</p>
                    </div>
                    <JobContainer />
                    {/* <Flavour flavours={flavours}/> */}
                </div>  
            </div>
            <div className="col-md-1"></div>
        </div>
    </div>
);
  }
  
  export default Jobs;
  
  