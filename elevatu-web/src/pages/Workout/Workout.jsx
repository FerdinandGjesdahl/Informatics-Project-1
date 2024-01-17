import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";

import './Workout.css';
import DataSource from "../../helpers/DataSource";
import {useAuth} from "../../context/AuthContext";
import workout from "./index";


/**
 * The Workout component that manages and displays exercises on the workout page.
 * @returns {JSX.Element} The rendered component.
 */
function Workout() {

  const auth = useAuth()
  const navigate = useNavigate();
  
  const [workoutPlan, setWorkoutPlan] = useState(null);

  const [availableWorkoutPlans, setAvailableWorkoutPlans] = useState([]);

  useEffect(() => {
    // Fetch workout plans from the server when the component mounts
    if (!auth.isLoggedIn) {
      navigate('/login')
      return
    }

    const userSession = auth.userSession;
    DataSource.getAllWorkouts(userSession.getUsername(), userSession).then((plans) => {
      const actualPlans = plans.getWorkoutplansList()

      setAvailableWorkoutPlans(actualPlans)
    })
  }, []);

  if (workoutPlan === null) {
    return (
      <div className="wo-app">
        <h1>Please select workout</h1>
        {availableWorkoutPlans.map((plan, planIndex) => (
            <div key={planIndex} className="wo-group">
                <button onClick={() => {
                    setWorkoutPlan(plan)
                }}>{plan.getName()}</button>
            </div>
        ))}
        <button className="wo-start-btn">Start workout</button>
        <button className="wo-home-btn">Home</button>
      </div>
    );
  }

  return (
    <div className="wo-app">
      <h1>Workout</h1>
      <h2>{workoutPlan.getName()}</h2>

      <div className="wo-grid">
        {workoutPlan.getExerciselistList().map((planExercise, index) => (
          <div key={index} className="wo-group">
            <input
              className="wo-title"
              type="text"
              value={planExercise.getExercise().getName()}
              placeholder={`Tittel`}
              readOnly
            />

            <div className="wo-row wo-header-row">
              <div className="wo-header">Sets</div>
              <div className="wo-header">Vekt</div>
              <div className="wo-header">Reps</div>
            </div>

            {[...Array(planExercise.getSets())].map((_, setIndex) => (
              <div key={setIndex} className="wo-row">
                <input
                  className="wo-cell"
                  type="text"
                  placeholder={`Sett ${setIndex + 1}`}
                  readOnly
                />
                <input
                  className="wo-cell"
                  type="text"
                  placeholder={`Vekt`}
                />
                <input
                  className="wo-cell"
                  type="text"
                  placeholder={`Reps`}
                />
              </div>
            ))}
          </div>
        ))}
      </div>
      <button className="wo-stop-btn">Save workout</button>
      <button className="wo-home-btn">Home</button>
    </div>
  );
}

export default Workout;
