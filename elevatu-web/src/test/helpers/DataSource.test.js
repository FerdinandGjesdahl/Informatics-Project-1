import React from 'react';

import DataSource from '../../helpers/DataSource';
import networkPacket from '../../protos/network_packets_pb';
import elevatuObjects from '../../protos/elevatu_objects_pb';

import '@testing-library/jest-dom'

//User setup:
const username = "test" 
const userSession = new networkPacket.UserSession();
userSession.setUsername(username);
userSession.setSecret("cdPSaoNzS9ABfbh3fyr1cM9Tu1");

//Exercise setup:
const exercise = new elevatuObjects.Exercise();
exercise.id = 1;
exercise.owner = username;
exercise.name = "testExercise";
exercise.notes = "this is just a test";

//workoutplanexercise
const workoutplanexercise = new elevatuObjects.WorkoutPlanExercise();
workoutplanexercise.exercise = exercise;
workoutplanexercise.reps = 10;
workoutplanexercise.sets = 5;

//workoutlogset:
const workoutlogset =  new elevatuObjects.WorkoutLogSet();
workoutlogset.exercise = exercise;
workoutlogset.weight = 10;
workoutlogset.reps = 3;

//workout setup:
const workout = new elevatuObjects.WorkoutPlan();
workout.id = 1;
workout.owner = username;
workout.name = "testWorkout";
workout.notes = "this is just a test";
workout.exerciseList = [workoutplanexercise];

//workoutlog setup:
const workoutlog = new elevatuObjects.WorkoutLog();
workoutlog.id = 1;
workoutlog.owner = 2;
workoutlog.performedSets = [workoutlogset];

/**
 * Test that the DataSource class is working as intended, gathering correct dataTypes.
 */
test('Test Get all workouts', () => {
    const data = DataSource.getAllWorkouts(username, userSession);

    expect(data).toBeInstanceOf(networkPacket.WorkoutPlanList);
});


test('Test Get specific workout', () => {
    const data = DataSource.getWorkout(1, userSession);

    expect(data).toBeInstanceOf(elevatuObjects.WorkoutPlan);
});

test('Test Add workout', () => {
    const data = DataSource.addWorkout(workout, userSession);

    expect(data).toBeInstanceOf(elevatuObjects.WorkoutPlan);
});


test('Test get all exercises', () => {
    const data = DataSource.getExercises(username, userSession);

    expect(data).toBeInstanceOf(networkPacket.ExerciseList);
});

test('Test get specific exercise', () => {
    const data = DataSource.getExercise(1, userSession);

    expect(data).toBeInstanceOf(elevatuObjects.Exercise);
});

test('Test add exercise', () => {
    const data = DataSource.addExercise(exercise, userSession);

    expect(data).toBeInstanceOf(elevatuObjects.Exercise);
});

test('Add workoutLog', () => {
    const data = DataSource.addWorkoutLog(workoutlog, userSession);

    expect(data).toBeInstanceOf(elevatuObjects.WorkoutLog);
})

test('Test get all workoutlogs', () => {
    const data = DataSource.getWorkoutLogs(username, userSession);

    expect(data).toBeInstanceOf(networkPacket.WorkoutLogList);
});

test('Test get specific workoutlog', () => {
    const data = DataSource.getWorkoutLog(1, userSession);

    expect(data).toBeInstanceOf(elevatuObjects.WorkoutLog);
});

