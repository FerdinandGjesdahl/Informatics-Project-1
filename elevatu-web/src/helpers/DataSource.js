import React from 'react';
import Cookies from 'universal-cookie';

import networkPackets from '../protos/network_packets_pb';
import elevatuObjects from '../protos/elevatu_objects_pb';


/** DataSource class with helper functions to fetch data from backend.
 * POSTS data based on input to functions, and fetches correct data to give back to the frontend.
 * Makes for easier and prettier code in the frontend, when trying to access and add data to the backend.
 * #TODO: Make sure they work as intended, returning correct data and not crashing.
 */
class DataSource{

    /**
     * Gets the address that we logged in with in the login page
     * @returns {String} Address of backend
     */
    static getAddress = () => {
        const cookies = new Cookies();
        const address = cookies.get('address');
        return address
    }

    // Set to true if you wish to use random mock data
    // This will instead call upon REST controller to fetch random data (for example to display test workout logs etc)
    static useMockData = false

    /**
     * Returns the correct controller to use based on if mock data is used or not
     * @returns {String} Controller name
     */
    static getController() {
        if (DataSource.useMockData) return "test_workoutapi"
        else return "workoutapi"
    }


    /** POSTS username and UserSession to API. Fetches all workouts for that user.
    * @param {String} username - as pathvariable
    * @param {UserSession} userSession - as requestbody
    * @returns {WorkoutPlanList} userSession  
    */
    static getAllWorkouts = async (username, userSession) => {
        try {
            const workouts = await fetch(DataSource.getAddress() + DataSource.getController() + "/workouts/plans/list/" + username, {
                method: "POST",  
                headers : {  
                    'Accept': 'application/x-protobuf',
                    'Content-Type': 'application/x-protobuf'
                },
                body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()
            }).then((buffer) => {
                const WorkoutPlanList = networkPackets.WorkoutPlanList.deserializeBinary(buffer);
                return WorkoutPlanList;
            })
            return workouts;
        } catch (error) { 
            console.log(error) ;
            return error;
        }
    }
    /** POSTS username and UserSession to API. Fetches single workout based on id for that user.
    * @param {Int} workoutID - as pathvariable
    * @param {UserSession} userSession -  as requestbody.
    * @returns {WorkoutPlan} all workouts for a user.
    */
    static getWorkout = async ( workoutID, userSession) => {
        try {
            const workout = await fetch(DataSource.getAddress() + DataSource.getController() + "/workouts/plans/id/" + workoutID, {
                method: "POST",  
                headers : {  
                    'Accept': 'application/x-protobuf',
                    'Content-Type': 'application/x-protobuf'
                },
                body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()
            })
            .then((buffer) => {
                const workoutPlan = networkPackets.WorkoutPlan.deserializeBinary(buffer);
                return workoutPlan;
            })
            return workout;
        } catch (error) { 
            console.log(error) 
            return error;
        }
    }

    /** POSTS usersession and workoutPlan to backend. 
     * Fetches workoutData for that user.
     * @param {workoutPlan} workoutPlan - as requestbody
     * @param {UserSession} userSession - as requestbody
     * @returns {workoutPlan} workoutPlan
     */
    static addWorkout = async (workoutPlan, userSession) => {
        const wpAddRequest = new networkPackets.WorkoutPlanAddRequest();
        wpAddRequest.setWorkoutplan(workoutPlan);
        wpAddRequest.setUsersession(userSession);

        try {
            await fetch(DataSource.getAddress() + DataSource.getController() + "/workouts/plans/add", {
                method: "POST",  
                headers : {  
                    'Accept': 'application/x-protobuf',
                    'Content-Type': 'application/x-protobuf'
                },
                body: wpAddRequest.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()                
            })
            .then((buffer) => {
                const workoutPlan = elevatuObjects.WorkoutPlan.deserializeBinary(buffer);
                return workoutPlan;
            })
        } catch (error) { 
            console.log(error)
            return error; 
        }
    }

    /** POSTS usersession and workoutPlan to backend. 
    * Fetches exercise data for that user as an exerciseList.
    * @param {String} username - as pathvariable
    * @param {UserSession} userSession - as requestbody
    * @returns {ExerciseList} exerciseList
    */
    static getExercises = async (username, userSession) => {
        try {
            const exercises = await fetch(DataSource.getAddress() + DataSource.getController() + "/exercises/list/" + username, {
                method: "POST",  
                headers : {  
                    'Accept': 'application/x-protobuf',
                    'Content-Type': 'application/x-protobuf'
                },
                body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                } 
                return response.arrayBuffer()               
            })
            .then((buffer) => {
                const exerciseList = networkPackets.ExerciseList.deserializeBinary(buffer);
                return exerciseList.getExercisesList();
            }) 
            return exercises 
        } catch (error) { 
            console.log(error)
        }        
    }

    /** POSTS exerciseID and usersession to the backend. 
    * Fetches exercise data for that specific exercise.
    * @param {Int} exerciseID - as pathvariable
    * @param {UserSession} userSession -as requestbody
    * @returns {Exercise} exercise
    */
    static getExercise = async (exerciseID, userSession) => {
        try {
           const returnedExercise = await fetch(DataSource.getAddress() + DataSource.getController() + "/exercises/id/" + exerciseID, {
               method: "POST",  
               headers : {  
                'Accept': 'application/x-protobuf',
                'Content-Type': 'application/x-protobuf'
            },
               body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()
            })
            .then((buffer) => {
                const Exercise = networkPackets.Exercise.deserializeBinary(buffer);
                return Exercise;
            })
            return returnedExercise        
        } catch (error) { 
            console.log(error) 
            return error;
        }
    }

    /** POSTS exercise and UserSession to backend.
     * Fetches exercise data for that spcific exercise.
     * @param {Exercise} exercise - as requestbody
     * @param {UserSession} userSession - as requestbody
     * @returns {Exercise} exercise
     */
    static addExercise = async (exercise, userSession) => {
        const exAddRequest = new networkPackets.ExerciseAddRequest();
        exAddRequest.setUsersession(userSession);
        exAddRequest.setExercise(exercise);

        try {
            await fetch(DataSource.getAddress() + DataSource.getController() + "/exercises/add", {
               method: "POST",  
               headers : {      
                'Accept': 'application/x-protobuf',
                'Content-Type': 'application/x-protobuf'
            },
               body: exAddRequest.serializeBinary()
            }).then((response) => { 
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()                
            })
            .then((buffer) => {
                const exercise = elevatuObjects.Exercise.deserializeBinary(buffer);
                return exercise;
            })
        } catch (error) { 
            console.log(error)
            return error
        }
    }

    /** POSTS WorkoutLog and Usersession to the backend.
     * Fetches Workoutlog with id set.
     * @param {WorkoutLog} workoutLog -  as requestbody
     * @param {UserSession} userSession - as requestbody
     * @returns {WorkoutLog} workoutLog
     */
    static addWorkoutLog = async (workoutLog, userSession) => {
        const wlAddRequest = new networkPackets.WorkoutLogAddRequest();
        wlAddRequest.setUsersession(userSession);
        wlAddRequest.setWorkoutLog(workoutLog);

        try {
            await fetch(DataSource.getAddress() + DataSource.getController() + "/workouts/logs/addWorkoutLog", {
               method: "POST",  
               headers : {  
                'Accept': 'application/x-protobuf',
                'Content-Type': 'application/x-protobuf'
            },
               body: wlAddRequest.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                } 
                return response.arrayBuffer()               
            })
            .then((buffer) => {
                const WorkoutLog = networkPackets.WorkoutLog.deserializeBinary(buffer);
                return WorkoutLog;
            })
        } catch (error) { 
            console.log(error) 
            return error;
        }        
    }

    /** POSTS username and userSession to the backend.
     * Fetches all workoutlogs for that user.
     * @param {String} username - as pathvariable
     * @param {userSession} userSession - as requestbody
     * @returns {WorkoutLogList} workoutLogList 
     */
    static getWorkoutLogs = async (username, userSession) => {
        try {
            const workoutlogs = await fetch(DataSource.getAddress() + DataSource.getController() + "/workouts/logs/list/" + username, {
               method: "POST",  
               headers : {  
                'Accept': 'application/x-protobuf',
                'Content-Type': 'application/x-protobuf'
            },
               body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()                
            })
            .then((buffer) => {
                const WorkoutLogList = networkPackets.WorkoutLogList.deserializeBinary(buffer);
                return WorkoutLogList;
            })
            return workoutlogs;
        } catch (error) { 
            console.log(error) 
            return error;
        }
    }

    /** POSTS workoutLogID and userSession to the backend.
     * Fetches workoutLog for that user.
     * @param {Int} workoutLogID -as pathvariable
     * @param {UserSession} userSession -as requestbody
     * @returns {WorkoutLog} workoutLog
     */
    static getWorkoutLog = async (workoutLogID, userSession) => {
        try {
            const loggedworkout = await fetch(DataSource.getAddress() + DataSource.getController() + "/workouts/logs/id/" + workoutLogID, {
               method: "POST",  
               headers : {  
                'Accept': 'application/x-protobuf',
                'Content-Type': 'application/x-protobuf'
            },
               body: userSession.serializeBinary()
            }).then((response) => {
                if (!response.ok){
                    console.log(`HTTP Error, status = ${response.status}`)
                    return response.status;
                }
                return response.arrayBuffer()               
            })
            .then((buffer) => {
                const WorkoutLog = networkPackets.WorkoutLog.deserializeBinary(buffer);
                return WorkoutLog;
            })
            return loggedworkout
        } catch (error) { 
            console.log(error) 
            return error;
        }        
    }

}

export default DataSource;