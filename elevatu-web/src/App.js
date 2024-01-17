import './App.css';

import { BrowserRouter, Routes, Route } from 'react-router-dom';
import {useState} from "react";

import { Home, Login, Workout,Log, CreateExercise, CreateWorkout } from './pages';
import { Sidebar } from './components';
import {AuthContext, AuthProvider} from "./context/AuthContext";


/**
 * The main App component rendering the application structure.
 *
 * @returns {JSX.Element} The JSX representation of the App component.
 */
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
      <Sidebar>
        <Routes>
          <Route path="/" element={<Home/>} />
          <Route path="/home" element={<Home/>} />
          <Route path="/login" element={<Login />} />
          <Route path="/Workout" element={<Workout />} />
          <Route path="/Log" element={<Log/>} /> 
          <Route path="/CreateExercise" element={<CreateExercise />} /> 
          <Route path="/CreateWorkout" element={<CreateWorkout />} />

        </Routes>
      </Sidebar>
      </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
