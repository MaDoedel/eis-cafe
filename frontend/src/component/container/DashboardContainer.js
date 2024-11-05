
import React, {useEffect, useState} from 'react';
import { jwtDecode } from 'jwt-decode';


function DashboardContainer() {
    const [user, setUser] = useState({});

    useEffect(() => {
        const token = localStorage.getItem('token');
        const decodedToken = jwtDecode(token);

        fetch(`/api/v2/users/${decodedToken.userId}`)
        .then(response => response.json())
        .then(data => {
            setUser(data);
        });
    }, []);


    return (
        <div>
        <h1>Dashboard</h1>
        </div>
    );
}

export default DashboardContainer;