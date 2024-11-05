import './App.css';
import Home from './Home.js';
import Login from './Login.js';
import Ice from './Ice.js';
import Jobs from './Jobs.js';
import DashboardContainer from './component/container/DashboardContainer.js';
import React, {useState, useTransition, useEffect} from 'react';
import { jwtDecode } from 'jwt-decode';



function App() {
  const [current, setCurrent] = useState("Home");
  const [isPending, startTransition] = useTransition();
  const [isTokenExpired, setIsTokenExpired] = useState(false);

  const handleClick = (page) => {
    startTransition(() => {
      setCurrent(page);
    });
  };

  const logout = () => {
    localStorage.removeItem("token");
    setIsTokenExpired(true);
    setCurrent("Home");
  };



  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      const decoded = jwtDecode(token);
      const expirationTime = decoded.exp * 1000;

      setIsTokenExpired(expirationTime < Date.now());

      const timer = setTimeout(() => {
        setIsTokenExpired(true);
      }, expirationTime - Date.now());

      return () => clearTimeout(timer);
    }
  }, []);

  const renderPage = () => {
    switch (current) {
      case "Home":
        return <Home />;
      case "Ice":
        return <Ice />;
      case "Jobs":
        return <Jobs />;
      case "Login":
        return <Login />;
      case "Dashboard":
        return <DashboardContainer />;
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
                <a className={current === "Jobs" ? 'nav-link active' : 'nav-link'} aria-current={current === "Jobs" ? 'page' : ''} onClick={() => handleClick("Jobs")}>Jobs</a>
              </li>
            </ul>
            {isTokenExpired ? (
              <div className="d-flex justify-content-center">
              <a className={current === "Login" ? 'nav-link active' : 'nav-link'} aria-current={current === "Login" ? 'page' : ''} onClick={() => handleClick("Login")} style={{color: 'white'}}>Login</a>
            </div>
            ) : (
              <>
                <div className="d-flex justify-content-center">
                  <a className={current === "Dashboard" ? 'nav-link active' : 'nav-link'} aria-current={current === "Dashboard" ? 'page' : ''} onClick={() => handleClick("Dashboard")} style={{color: 'white'}}>Dashboard</a>
                </div>
                <div className="d-flex justify-content-center">
                  <a className={'nav-link'} onClick={logout} style={{color: 'white'}}>Logout</a>
                </div>
              </>
            )
            }
            
          </div>
        </div>
      </nav>
      {isPending && "Loading..."}
      {renderPage()}

      {/* <footer className="footer mt-auto py-3">
        <div className="container d-flex flex-wrap justify-content-between align-items-center">
          <div className="col-md-4 d-flex align-items-center">
            <a href="https://getbootstrap.com/" className="mb-3 me-2 mb-md-0 text-white text-decoration-none lh-1">
              <i className="bi bi-bootstrap" style={{ color: "white" }} ></i>
            </a>
            <span className="text-white">© 2021 Company, Inc</span>
          </div>

          <ul className="nav col-md-4 justify-content-end list-unstyled d-flex">
            <li className="ms-3"><a className="text-white" href="#"><svg className="bi" width="24" height="24"></svg></a></li>
            <li className="ms-3"><a className="text-white" href="#"><svg className="bi" width="24" height="24"></svg></a></li>
            <li className="ms-3"><a className="text-white" href="#"><svg className="bi" width="24" height="24"></svg></a></li>
          </ul>
        </div>
      </footer> */}

    </div>
  );
}

export default App;
