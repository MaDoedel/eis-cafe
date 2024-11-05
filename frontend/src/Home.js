function Intro() {
    return (
        <div className="row ">
            <div className="col-md-12">
                <figure className="text-center">
                    <blockquote className="blockquote">
                        <h1 className="display-1">EisCafe Roma</h1>
                    </blockquote>
                    <figcaption className="blockquote-footer">
                        Das Lied von Eis und Kaffee
                    </figcaption>
                </figure>
            </div>
            <div className="col-md-12"> 
                <p className="text-center">Im Herzen unserer Stadt erwartet Sie ein Stück italienische Lebensgefühl: Unsere familiengeführte Eisdiele und Café. Hier, wo Tradition auf Gastfreundschaft trifft, können Sie nicht nur köstliches Eis, aromatischen Kaffee und vielfältige Snacks genießen, sondern auch wertvolle Momente mit Ihren Liebsten teilen. </p>
                <p className="text-center">Egal ob an warmen Sommertagen, wenn der Duft von frischen Waffeln in der Luft liegt, oder an kalten Winterabenden, wenn der heiße Cappuccino die Hände wärmt – unser gemütliches Etablissement ist das ganze Jahr über für Sie geöffnet. Hier finden Sie den perfekten Ort, um mit Freunden zusammenzukommen, gemeinsam zu lachen und das Leben in vollen Zügen zu genießen. </p>
                <p className="text-center">Unsere italienische Familie freut sich darauf, Sie zu jeder Jahreszeit willkommen zu heißen und Ihnen ein Stück authentischer Dolce Vita zu schenken. Kommen Sie vorbei und erleben Sie die besondere Atmosphäre, die unser kleines Stück Italien so unverwechselbar macht. </p>
            </div>
    </div>    
    );
}

function Main() {
    return (
        <div className="row border rounded">
            <div className="col-md-6"> 
                <div id="carouselExampleInterval" className="carousel slide" data-bs-ride="carousel">
                    <div className="carousel-inner">
                        <div className="carousel-item active" data-bs-interval="2000">
                            <img src="/logo192.png" className="w-100" style={{width: "100%", height: "100%"}} alt="..."/>
                        </div>
                        <div className="carousel-item" data-bs-interval="2000">
                            <img src="/logo192.png" className="w-100" style={{width: "100%", height: "100%"}} alt="..."/>
                        </div>
                        <div className="carousel-item" data-bs-interval="2000">
                            <img src="/logo192.png" className="w-100" style={{width: "100%", height: "100%"}} alt="..."/>
                        </div>
                    </div>

                    <button className="carousel-control-prev" type="button" data-bs-target="#carouselExampleInterval" data-bs-slide="prev">
                        <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span className="visually-hidden">Previous</span>
                    </button>
                    <button className="carousel-control-next" type="button" data-bs-target="#carouselExampleInterval" data-bs-slide="next">
                        <span className="carousel-control-next-icon" aria-hidden="true"></span>
                        <span className="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
            <div className="col-md-6"> 
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
  
  