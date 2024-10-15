function Intro() {
    return (
        <div className="row border rounded">
            <p>Welcome to the Home Page</p>
            <div className="col-md-12"> 
        
            </div>
    </div>    
    );
}

function Main() {
    return (
        <div className="row">
            <div className="col-md-8"> 
               
            </div>
            <div className="col-md-4"> 
                <Intro/>
            </div>
        </div>    
    );
}

function Home() {
    return (
      <div className="container">
        <div className="row my-2">
            <div className="col-md-1"> </div>
            <div className="col-md-10"> 
                <Main/>
            </div>
            <div className="col-md-1"> </div>
        </div>    
    </div>
    );
}


export default Home;
  
  