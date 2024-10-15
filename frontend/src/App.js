import './App.css';
import Home from './Home.js';
import Login from './Login.js';
import Ice from './Ice.js';
import Contact from './Contact.js';
import React, {useState, useTransition} from 'react';


function App() {
  
  const [current, setCurrent] = useState("Home");
  const [isPending, startTransition] = useTransition();

  const handleClick = (page) => {
    startTransition(() => {
      setCurrent(page);
    });
  };

  const renderPage = () => {
    switch (current) {
      case "Home":
        return <Home />;
      case "Ice":
        return <Ice />;
      case "Contact":
        return <Contact />;
      case "Login":
        return <Login />;
      default:
        return <Home />;
    }
  }


  return (
    <div className="App">
      <nav className="navbar navbar-expand-lg bg-dark border-bottom border-body" data-bs-theme="dark">
        <div className="container-fluid">
          <a className="navbar-brand" onClick={() => handleClick("Home")}>Navbar w/ text</a>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <a className={current === "Home" ? 'nav-link active' : 'nav-link'} aria-current={current === "Home" ? 'page' : ''} onClick={() => handleClick("Home")}>Home</a>
              </li>
              <li className="nav-item">
                <a className={current === "Ice" ? 'nav-link active' : 'nav-link'} aria-current={current === "Ice" ? 'page' : ''} onClick={() => handleClick("Ice")}>Eis</a>
              </li>
              <li className="nav-item">
                <a className={current === "Contact" ? 'nav-link active' : 'nav-link'} aria-current={current === "Contact" ? 'page' : ''} onClick={() => handleClick("Contact")}>Pricing</a>
              </li>
            </ul>
            <div className="d-flex justify-content-center">
              <a className={current === "Login" ? 'nav-link active' : 'nav-link'} aria-current={current === "Login" ? 'page' : ''} onClick={() => handleClick("Login")} style={{color: 'white'}}>Login</a>
            </div>
          </div>
        </div>
      </nav>
      {isPending && "Loading..."}
      {renderPage()}
    </div>
  );
}

export default App;
