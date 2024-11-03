import React, { useRef } from "react";
import { jwtDecode } from "jwt-decode";

function Login ()  {
    const usernameInputRef = useRef(null);
    const passwordInputRef = useRef(null);


    const handleSubmit = async (e) => {
        e.preventDefault();

        const username = usernameInputRef.current.value;
        const password = passwordInputRef.current.value;

        const postData = new FormData();
        postData.append('username', username);
        postData.append('password', password);

        try {
            const response = await fetch('/login', {
                method: 'POST',
                body: postData
            });
    
            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.jwt);

                const decodedToken = jwtDecode(data.jwt);
                alert(decodedToken.userId);
            } else {
                throw new Error('Login failed');
            }
        } catch (error) {
            console.error(error);
        }

        usernameInputRef.current.value = '';
        passwordInputRef.current.value = '';
    }

    return (
        <div className="row">
            <div className="col-md-4"></div>
                <div className="col-md-4">
                    <div className="row my-2 border rounded">
                        <div className="col-md-12 mt-2 ">
                            <h1>Login</h1>
                        </div>
                        <form onSubmit={handleSubmit}> 
                            <div className="col-md-12 mt-2">
                                    <div class="me-3">
                                        <label for="nameInput" class="form-label">Username</label>
                                        <input type="text" class="form-control" aria-describedby="userHelp" ref={usernameInputRef}/>
                                    </div>
                                    <div class="me-3">
                                        <label for="passwordInput" class="form-label">Password</label>
                                        <input type="password" class="form-control" aria-describedby="passwordHelp" ref={passwordInputRef}/>
                                        <div id="passwordHelp" class="form-text text-start"> Your password must be 8-20 characters long, contain letters and numbers, and must not contain spaces, special characters, or emoji. </div>
                                    </div>
                            </div>
                            <button type="submit" class="btn btn-primary">Login</button>            
                        </form>
                    </div>
                </div>
            <div className="col-md-4"></div>
        </div>
    )
}

export default Login;
