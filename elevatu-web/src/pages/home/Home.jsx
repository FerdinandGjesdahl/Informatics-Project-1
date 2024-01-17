import React from "react";
import "./Home.css";

import elevatuLogo from '../../resources/images/ElevatuLogo.jpg'; // Korrekt filbane

const Home = () => {
    return (
        <div className="home-container">
            <img src={elevatuLogo} alt="Elevatu Logo" className="home-logo" />
            <h1 className="home-header">Velkommen til Elevatu</h1>
            <p className="home-quote">"I dag er din mulighet til å bygge morgendagens drømmer."</p>
        </div>
    );
}

export default Home;