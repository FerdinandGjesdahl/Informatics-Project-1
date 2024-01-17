import React, { useState, useEffect } from 'react';
import './CreateWorkout.css';
import { useNavigate } from 'react-router-dom';
import Cookies from 'universal-cookie';

import networkPackets from "../../protos/network_packets_pb.js";
import DataSource from '../../helpers/DataSource';
import elevatuObjects from "../../protos/elevatu_objects_pb.js";

/**
 * React functional component representing the CreateWorkout feature.
 * This component allows users to input workout details and save them.
 *
 * @returns {JSX.Element} The JSX representation of the CreateWorkout component.
 */

const CreateWorkout = () => {
  const [exerciseNotesInput, setExerciseNotesInput] = useState('');
  const [workoutNameInput, setWorkoutNameInput] = useState('');
  const [selectedExercise, setSelectedExercise] = useState(null);
  const [allExercises, setAllExercises] = useState([]);

  const [settInput, setSettInput] = useState('');
  const [repsInput, setRepsInput] = useState('');

  const [userSession, setUserSession] = useState(null);
  const [sessionState, setSessionState] = useState(false);
  const cookies = new Cookies();

  const [result, setResult] = useState('');
  const [displayedExercises, setDisplayedExercises] = useState([]); 
  const [chosenExercises, setChosenExercises] = useState([]);
    /**
   * React Router hook for navigation.
   */
  const navigate = useNavigate();

  useEffect(() => {
    // Retrieve user session from cookies on component mount
    const storedUsername = cookies.get('username');
    const storedSecret = cookies.get('secret');
    
    const newUserSession = new networkPackets.UserSession();
    newUserSession.setUsername(storedUsername);
    newUserSession.setSecret(storedSecret);
    setUserSession(newUserSession);
    setSessionState(true);

  }, []); 

  useEffect(() => {
    // Fetch exercises from the server when the component mounts
    const fetchExercises = async () => {
      try {
        if (sessionState) {
          const exercises = await DataSource.getExercises(cookies.get('username'), userSession);
          setAllExercises(exercises);
        }
        else {
          setResult("Please login to view your exercises, or create a new account.")
      }
        } catch (error) {
          console.error('Error fetching exercises:', error);
        }
    };
  
    fetchExercises();
  }, [sessionState]); 

  // Input handles 

  /**
   * Handles the click event for the "Start Workout" button.
   * Navigates to the '/workout' route.
   * @returns {void}
   */
  const handleStartWorkoutButton = () => {
    navigate('/workout'); 
  };

   /**
   * Handles the click event for the "Add Exercise" button.
   * Adds a new exercise to the list based on user input.
   * @returns {void}
   */
   const handleAddExerciseButton = async  () => {
    if (selectedExercise != ("empty" || null)) {
      const exercise = await DataSource.getExercise(selectedExercise, userSession);
      const exerciseDetails = new elevatuObjects.WorkoutPlanExercise();
      exerciseDetails.setExercise(exercise);
      exerciseDetails.setReps(repsInput);
      exerciseDetails.setSets(settInput);
      setChosenExercises([...chosenExercises, exerciseDetails]);
      setDisplayedExercises([...displayedExercises, exercise.getName()]);

      //Reset fields:
      setResult("");
      setRepsInput('');
      setSettInput('');
    } else {setResult("Please select an exercise.");}
  };

   /**
   * Handles the change event for the exercise dropdown.
   * Updates the selected exercise.
   * @param {React.ChangeEvent<HTMLSelectElement>} e - The event object.
   * @returns {void}
   */
   const handleExerciseSelectChange = (e) => {
    setSelectedExercise(e.target.value);
  };

   /**
   * Handles the click event for the "Save Workout" button.
   * Sends a POST request to save workout data to the server.
   * @returns {void}
   */
   const handleSaveWorkoutButton = async () => {
    const workoutPlan = new elevatuObjects.WorkoutPlan();
    workoutPlan.setOwner(userSession.getUsername()); 
    workoutPlan.setName(workoutNameInput);
    workoutPlan.setNotes(exerciseNotesInput);
    for (let i=0; i < chosenExercises.length; i++) {
      workoutPlan.addExerciselist(chosenExercises[i]);
    }
    
    try {
      await DataSource.addWorkout(workoutPlan, userSession);
  
      // Clear the exercises after saving the workout
      setAllExercises([]);
      
      setResult('Workout saved successfully');
    } catch (error) {
      setResult('Failed to save workout');
      console.error('Error:', error);
    }
  };

    /**
   * Handles the change event for the Reps input.
   * Ensures that only numeric input is allowed.
   *
   * @param {React.ChangeEvent<HTMLInputElement>} e - The event object.
   * @returns {void}
   */
    const handleRepsInputChange = (e) => {
      const value = e.target.value;
      // Use a regular expression to allow only valid numeric input
      const numericValue = value.replace(/\D/g, ''); // Remove non-numeric characters
      setRepsInput(numericValue);
    };
  
     /**
     * Handles the keypress event for the Reps input.
     * Prevents non-numeric characters from being entered.
     *
     * @param {React.KeyboardEvent<HTMLInputElement>} e - The event object.
     * @returns {void}
     */
    const handleRepsKeyPress = (e) => {
      if (e.key && !/^\d$/.test(e.key)) {
        e.preventDefault();
      }
    };
  
      /**
     * Handles the change event for the Sets input.
     * Ensures that only numeric input is allowed.
     *
     * @param {React.ChangeEvent<HTMLInputElement>} e - The event object.
     * @returns {void}
     */
    const handleSetsInputChange = (e) => {
      const value = e.target.value;
      const numericValue = value.replace(/\D/g, '');
      setSettInput(numericValue);
    };
  
      /**
     * Handles the keypress event for the Sets input.
     * Prevents non-numeric characters from being entered.
     *
     * @param {React.KeyboardEvent<HTMLInputElement>} e - The event object.
     * @returns {void}
     */
    const handleSetsKeyPress = (e) => {
      if (e.key && !/^\d$/.test(e.key)) {
        e.preventDefault();
      }
    };

  return (
    <div className="WorkoutContainer">
      <h1>Create Workout</h1>
      <div className="input-group">
        <label>Workout Name:</label>
        <input
          type="text"
          id="workoutNameInput"
          value={workoutNameInput}
          onChange={(e) => setWorkoutNameInput(e.target.value)}
        />
      </div>
      <div className="input-group">
        <label>Notes:</label>
        <textarea
          id="exerciseNotesInput"
          value={exerciseNotesInput}
          onChange={(e) => setExerciseNotesInput(e.target.value)}
          rows="4"
        />
      </div>
      <div className="input-group">
        <label>Select Exercise:</label>
        <select value={selectedExercise} onChange={handleExerciseSelectChange}>
          <option value="empty">Select an exercise</option>
          {allExercises.map((exercise, index) => (
            <option key={index} value={exercise.getId()}>
              {exercise.getName()}
            </option>
          ))}
        </select>
      </div>
      <button id="addExerciseButton" onClick={handleAddExerciseButton}>
        Add Exercise
      </button>
      <div className="WorkoutExercisePlan">
        <ul id="exerciseListView">
          {
            displayedExercises.map((exercise) => (
              <li>{exercise}</li>
            )
          )
          }
        </ul>
        <label className="exercise-label">Sets:</label>
        <input
          className="exercise-input"
          type="number"
          id="settInput"
          value={settInput}
          onKeyPress={handleSetsKeyPress}
          onChange={handleSetsInputChange}
        />
        <label className="exercise-label">Reps:</label>
        <input
          className="exercise-input"
          type="number"
          id="repsInput"
          value={repsInput}
          onKeyPress={handleRepsKeyPress}
          onChange={handleRepsInputChange}
        />
      </div>
      <div className="button-stack">
        <button id="startWorkoutButton" onClick={handleStartWorkoutButton}>
          Start Workout
        </button>
        <button id="saveWorkoutButton" onClick={handleSaveWorkoutButton}>
          Save Workout
        </button>
        <div id="result">{result}</div> {/* Result message div */}
      </div>
    </div>
  );}

export default CreateWorkout;
