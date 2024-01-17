import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import { FaClock } from "react-icons/fa";

import './Log.css';
import DataSource from "../../helpers/DataSource";
import {useAuth} from "../../context/AuthContext";

/**
 * Log-komponenten oppretter og viser en liste over treningslogger.
 * Hver treningslogg inneholder en samling av øvelser med detaljer
 * som navn, antall sett og notater for hver øvelse.
 * 
 * @returns {JSX.Element} En React-komponent som viser treningslogger.
 */
function Log() {

  const auth = useAuth()
  const navigate = useNavigate();

  useEffect(() => {
    if (!auth.isLoggedIn) {
      navigate('/login')
      return
    }


    const userSession = auth.userSession;
    DataSource.getWorkoutLogs(userSession.getUsername(), userSession).then((logs) => {
      const actualLogs = logs.getWorkoutlogsList()

      setLogs(actualLogs)
    })
  }, []);


  /**
   * Converts protobuf timestamp to date string
   * @param {Timestamp} timestamp Timestamp to convert
   * @returns {string} Date string
   */
  function timeStampToDate(timestamp) {
    const date = new Date(timestamp.getSeconds() * 1000)
    return date.toLocaleDateString()
  }

  const [logs, setLogs] = useState([]);

  return (
    <div className="wo-app">
      <h1>Workout Logs</h1>
      {logs.map((log, logIndex) => (
        <div key={logIndex} className="wo-group">
          <h2><FaClock/> {timeStampToDate(log.getDate())}</h2>
          {log.getPerformedsetsList().map((performedSet, performedSetIndex) => (
            <div key={performedSetIndex} className="exercise">
              <p><strong>Øvelse: </strong> {performedSet.getExercise().getName()}</p>
              <p>{performedSet.getReps().toFixed(0)} x {performedSet.getWeight().toFixed(2)}</p>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}
export default Log;
