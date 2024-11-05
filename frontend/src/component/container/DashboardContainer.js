
import React, {useEffect, useState} from 'react';
import DashboardPresenter from "../presentational/DashboardPresenter";
import { jwtDecode } from 'jwt-decode';


function DashboardContainer() {
    const [user, setUser] = useState({});

    useEffect(() => {
        const token = localStorage.getItem('token');
        const decodedToken = jwtDecode(token);

        fetch(`/api/v2/users/${decodedToken.userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => response.json())
        .then(data => {
            setUser(data);
        });
    }, []);


    return (
        <DashboardPresenter 
        user={user}
        />
    );
}

export default DashboardContainer;