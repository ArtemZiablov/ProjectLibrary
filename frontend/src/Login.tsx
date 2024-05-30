import React, { useState } from 'react';
import './Login.css';
import { useNavigate } from "react-router-dom";
import SuccNotifications from "./components/BottomBar/SuccNotifications/SuccNotifications";

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [requestStatus, setRequestStatus] = useState("initial");
    const navigate = useNavigate();

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username, password})
            });

            const responseData = await response.json();

            if (!response.ok || responseData.message) {
                setRequestStatus("failure");
                setTimeout(() => {
                    setRequestStatus("initial");
                }, 1500);
                throw new Error(responseData.message || 'Login failed');
            }

            const {'jwt-token': token} = responseData;

            // сохранить токен в localStorage или в состоянии приложения
            localStorage.setItem('token', token);

            const token1 = localStorage.getItem('token');
            // вывести токен в консоли браузера
            console.log('Token:', token1);

            navigate('/library')
        } catch (error) {
            console.error('Login error:', error);
            setRequestStatus("failure");
        }
    };

    const handleOkClick = () => {
        setRequestStatus("initial");
    }

    return (
        <div className="login-container">
            <div className="header">
                <img src="https://i.imgur.com/lZ7rw2l.png" alt="Logo" className="logo-login"/>
                <h1 className="app-title">Library Management System</h1>
            </div>
            <form className="login-form" onSubmit={handleSubmit}>
                <h2>Login</h2>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={handleUsernameChange}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={handlePasswordChange}
                />
                <button type="submit">Login</button>
                {requestStatus === "failure" &&
                    <SuccNotifications message={"Incorrect email or password"} requestStatus={requestStatus}
                                       onOkClick={handleOkClick}/>}
            </form>
        </div>
    );
};

export default Login;
