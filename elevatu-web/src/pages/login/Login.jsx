import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import Cookies from 'universal-cookie';

import Bicep from "../../resources/images/BicepIcon.jpg";
import networkPackets from "../../protos/network_packets_pb";
import './Login.css';
import {useAuth} from "../../context/AuthContext";
import SessionManager from "../../helpers/SessionManager";


/**
 * Login page, handles login and register of users.
 * @returns {Login} Login page
 * #TODO Make cookies work properly, on calls.
 */
function Login() {

    const [username, setUsername]= useState(null);
    const [password, setPassword] = useState(null);
    const [message, setMessage] = useState('');
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const [ip, setIp] = useState(null);
    const [portnr, setPortnr] = useState(null);
    const [address, setAddress] = useState(null);

    const navigate = useNavigate();

    const auth = useAuth()

    useEffect(() => {
        if (auth.isLoggedIn) {
            navigate('/home')
            return 
        }
    }, []);

    const createConnection = () => {
        try {
            if (ip != "" || ip != null || portnr != "" || portnr != null){
                const address = ("http://" + ip + ":" + portnr + "/"); 
                console.log(address);
                setAddress(address);
                setMessage("Addres set to: " + address);
            }
            else { setMessage("Please enter a valid ip and port number, before continuing.") }
        }
        catch (error) {
            console.log(error.message);
        }
    }    
    /**
     * POST data to API and fetches response, handles user created successfull or error.
     * @returns {String} Response from API or ERROR
     */
    async function handleRegisterButton() {

        //Check if username and password is null or "":
        if (username === null || username === "") {
            setUsername(null);
            setMessage("Please select a username.");
            return;
        } else if (password === null || password === "") {
            setPassword(null);
            setMessage("Please select a password.");
            return;
        }

        //Fetches API server and POST username and password to register.
        try {
            const response = await fetch(address + "userapi/users/createUser", {
                method: "POST",
                body: new URLSearchParams({
                    'username': username,
                    'password': password
                })
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        
            const text = await response.text();
            setMessage(text);
            console.log(text);
        } catch (error) {
            setMessage(error.message);
        }
    }

    /**
     * POST Data to API and fetches response, handles login successfull or error.
     * @returns {UserSession} UserSession    
     */
    async function handleLoginButton() {
        //Check if username and password is null or "":
        if (username === null || username === "") {
            setUsername(null);
            setMessage("Please select a username.");
            return;
        } else if (password === null || password === "") {
            setPassword(null);
            setMessage("Please select a password.");
            return;
        }

        //Fetches API server and POST username and password to login.
        try {
            await fetch(address + "userapi/users/login", {
                method: "POST",
                body: new URLSearchParams({
                    'username': username,
                    'password': password
                })
            })
                .then((response) => {
                    if (!response.ok){
                        console.log(`HTTP Error, status = ${response.status}`)
                        setMessage("Password or Username is incorrect.");
                    }
                    return response.arrayBuffer();
                })
                .then((buffer) => {
                    const userSession = networkPackets.UserSession.deserializeBinary(buffer);
                    new Cookies().set('address', address, { path: '/' });
                    SessionManager.setSessionCookies(userSession);
                    auth.setUserSession(userSession);
                    navigate('/home')
                })

        } catch (error) {
            console.log(error.message);
        }
    }
    return (
        <div className="login-container">
            <h1 className="login-header">ElevatU</h1>
            <h2 className="login-subheader">Login or Register User</h2>
            <img className="login-image" src={Bicep} alt="Bicep" />
            <div className='server-connection'>
                <label className="server-label">Server-ip:</label>
                <input className="server-input" type="text" placeholder="Server-ip" onChange={e => setIp(e.target.value)} />
                <label classname="port-label">port-nr:</label>
                <input className="port-input" type="text" placeholder="port-nr" onChange={e => setPortnr(e.target.value)} />
                <button className="connect" onClick={createConnection}> Connect </button>
            </div>
            <div className="input-container">
                <label className="login-label">Username:</label>
                <input className="login-input" type="text" placeholder="Username" onChange={e => setUsername(e.target.value)} />
                <label className="login-label">Password:</label>
                <input className="login-input" type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} />
            </div>
        
            <button className="login-button login-register-button" onClick={handleRegisterButton}>Register</button>
            <button className="login-button login-login-button" onClick={handleLoginButton}>Login</button>

            <p className="login-paragraph">{message}</p>
            {isLoggedIn ? navigate('/home') : null}
        </div>
    );
}

export default Login;