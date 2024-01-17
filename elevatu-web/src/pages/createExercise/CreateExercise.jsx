import React, { useState, useEffect } from 'react'; 
import './CreateExercise.css';
import Cookies from 'universal-cookie'; 

import DataSource from '../../helpers/DataSource';
import networkPackets from "../../protos/network_packets_pb.js";
import elevatuObjects from "../../protos/elevatu_objects_pb.js";

/**
 * The main functional component representing the CreateExercise feature.
 * Allows users to input exercise details and save them.
 *
 * @returns {JSX.Element} The JSX representation of the CreateExercise component.
 */
const CreateExercise = () => {
  const [exerciseName, setExerciseName] = useState('');
  const [notesField, setNotesField] = useState('');
  const [result, setResult] = useState('');
  const [userSession, setUserSession] = useState(null);
  const cookies = new Cookies();

  useEffect(() => {
    // Retrieve user session from cookies on component mount
    const storedUsername = cookies.get('username');
    const storedSecret = cookies.get('secret');

    if (storedUsername && storedSecret) {
      const newUserSession = new networkPackets.UserSession();
      newUserSession.setUsername(storedUsername);
      newUserSession.setSecret(storedSecret);
      setUserSession(newUserSession);
    }
  }, []); // Empty dependency array to ensure the effect runs only once on mount

  /**
   * Handles the click event for the "Create Exercise" button.
   * Sends a POST request to save exercise data to the server using DataSource.
   *
   * @returns {void}
   */
  const handleCreateButton = async () => {
    console.log("Button Clicked!");

    //Create protobuf object to send to backend.
    const exerciseData = new elevatuObjects.Exercise();
    exerciseData.setOwner(cookies.get('username'));
    exerciseData.setName(exerciseName);
    exerciseData.setNotes(notesField);
    //const exercise = new elevatuObjects.WorkoutPlanExercise();
    try {
      await DataSource.addExercise(exerciseData, userSession);
      
      // If successful, update state and display a success message
      setExerciseName('');
      setNotesField('');
      setResult('Exercise saved successfully');
    } catch (error) {
      // Handle error here (e.g., display an error message)
      console.error(error);
    }
  };

  return (
    <div className="exercise-container">
      <h1>Create Exercise</h1>
      <label className="exercise-label">Exercise Name:</label>
      <input
        className="exercise-input"
        type="text"
        id="exerciseName"
        value={exerciseName}
        onChange={(e) => setExerciseName(e.target.value)}
      />
      <label className="exercise-label">Notes:</label>
      <textarea
        className="exercise-textarea"
        id="notesField"
        value={notesField}
        onChange={(e) => setNotesField(e.target.value)}
      />
      <button className="exercise-button" onClick={handleCreateButton}>
        Create Exercise
      </button>
      <div className="exercise-result" id="result">
        {result}
      </div>
    </div>
  );
};

export default CreateExercise;
