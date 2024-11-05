import React from 'react';
import JobListPresenter from './JobListPresenter';
import { jwtDecode } from 'jwt-decode';

function UserInfo( {user} ) {

    const token = localStorage.getItem('token');
    const decodedToken = jwtDecode(token);


    function handleLogout() {
        localStorage.removeItem('token');
        window.location.href = '/';
    }

    return (
        <div className="container">
            <div className="row border rounded">
                <div className="col-md-12"> 
                    <h1>Willkommen {user.name},</h1>
                    <p className='text-start'> Auf deinem persönlichen Dashboard kannst du deine Informationen einsehen, sowie weitere Aktionen abhängig von deiner Rolle auführen. Bleib doch noch etwas!</p>

                    <table class="table">
                        <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Name</th>
                                <th scope="col">Nachname</th>
                                <th scope="col">E-Mail</th>
                                <th scope="col">Rollen</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="row" >{decodedToken.userId}</th>
                                <td><span>{user.name}</span></td>
                                <td><span>{user.surname}</span></td>
                                <td><span>{decodedToken.sub}</span></td>
                                <td>{decodedToken.roles.map((role) => (
                                <span >{role.authority}</span>
                            ))}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div className="col-md-12 my-2">
                    <button className="d-flex justify-content-end btn btn-outline-primary " onClick={handleLogout}>Logout</button>
                </div>
            </div>    
        </div>
    );
}

function DashboardPresenter( {user} ) {
    return (
        <div className="container">
            <div className="row my-2">
                <div className="col-md-1"> </div>
                <div className="col-md-10"> 
                    <UserInfo user={user}/>
                    <JobListPresenter/>
                </div>
                <div className="col-md-1"> </div>
            </div>    
        </div>
    );
}

export default DashboardPresenter;