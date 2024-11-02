import React from "react";
import JobContainer from "./component/container/JobContainer";
import JobListPresenter from "./component/presentational/JobListPresenter";

function Jobs() {
    return (
        <div>
            <div className="row">
                <div className="col-md-4"></div>
                <div className="col-md-4">
                    <div className="row my-2 border rounded">
                        <div className="col-md-12 mt-2 ">
                            <p>Bewerbunsgformalar</p>
                        </div>
                        <JobContainer />
                        {/* <Flavour flavours={flavours}/> */}
                    </div>  
                    <div className="row my-2 border rounded">
                        <div className="col-md-12 my-2 ">
                            <JobListPresenter />
                        </div>
                    </div>  
                </div>
                <div className="col-md-4"></div>
            </div>
        </div>
    );
}
  
  export default Jobs;
  
  