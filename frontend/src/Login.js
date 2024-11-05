import React, { useRef } from "react";
import { useNavigate } from "react-router-dom";

function Login ( {setCurrent} ) {
    const usernameInputRef = useRef(null);
    const passwordInputRef = useRef(null);
    const navigate = useNavigate();



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
                window.location.href = '/';
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
                            <div class="col-md-12 mt-2">
                                <div className="input-group">
                                    <span className="input-group-text">@E-Mail</span>
                                    <input type="text" class="form-control" placeholder="me@example.com" aria-describedby="userHelp" ref={usernameInputRef}/>
                                </div>
                                <div id="userHelp" class="form-text text-start"> 
                                    Please enter your email address.
                                </div>
                            </div>
                            <div class="col-md-12 mt-2">
                            </div>

                            <div class="col-md-12 mt-2">
                                <div className="input-group ">
                                    <span className="input-group-text">Password</span>
                                    <input type="password" class="form-control" placeholder="password" aria-describedby="passwordHelp" ref={passwordInputRef}/>
                                </div>
                                <div id="passwordHelp" class="form-text text-start"> 
                                    Your password must be 8-20 characters long, contain letters and numbers, and must not contain spaces, special characters, or emoji. 
                                </div>
                            </div>

                            <div class="col-me-12 my-2">
                                <button type="submit" class="btn btn-outline-primary">Login</button>            
                            </div>
                        </form>
                    </div>
                </div>
            <div className="col-md-4"></div>
        </div>
    )
}

export default Login;
