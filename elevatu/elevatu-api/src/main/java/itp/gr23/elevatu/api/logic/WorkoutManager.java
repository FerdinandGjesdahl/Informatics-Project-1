package itp.gr23.elevatu.api.logic;

import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import itp.gr23.elevatu.api.storage.StorageController;

import java.util.List;

public class WorkoutManager {
    private final List<WorkoutPlan> workoutPlanList;
    private final List<Exercise> exerciseList;
    private final List<WorkoutLog> workoutLogList;

    private final StorageController storageController;

    /**
     * Create a new WorkoutManager.
     * @param storageController StorageController to use
     */
    public WorkoutManager(final StorageController storageController) {
        this.storageController = storageController;

        this.workoutPlanList = storageController.loadWorkouts();
        this.exerciseList = storageController.loadExercises();
        this.workoutLogList = storageController.loadWorkoutLogs();
    }

    /**
     * Get all workouts for a user.
     * @param username Username
     * @return List of workouts
     */
    public List<WorkoutPlan> getWorkoutsForUser(final String username) {
        return workoutPlanList.stream().filter(workoutPlan -> workoutPlan.getOwner().equals(username)).toList();
    }

    /**
     * Get all exercises for a user.
     * @param username Username
     * @return List of exercises
     */
    public List<Exercise> getExercisesForUser(final String username) {
        return exerciseList.stream().filter(exercise -> exercise.getOwner().equals(username)).toList();
    }

    /**
     * Get all workout logs for a user.
     * @param username Username
     * @return List of workout logs
     */
    public List<WorkoutLog> getWorkoutLogsForUser(final String username) {
        return workoutLogList.stream().filter(workoutLog -> workoutLog.getOwner().equals(username)).toList();
    }

    /**
     * Get workout by ID.
     * Throws DuplicateIDException if multiple workouts with the same ID are found.
     * @param id ID of workout
     * @return WorkoutPlan if found, null if not found
     */
    public WorkoutPlan getWorkoutById(final int id) throws DuplicateIDException {
        List<WorkoutPlan> workoutPlanList =
                this.workoutPlanList.stream().filter(workoutPlan -> workoutPlan.getId() == id).toList();
        if (workoutPlanList.size() == 1) {
            return workoutPlanList.get(0);
        } else if (workoutPlanList.isEmpty()) {
            return null;
        } else {
            throw new DuplicateIDException("Multiple workouts with the same ID found.");
        }
    }

    /**
     * Get exercise by ID.
     * Throws DuplicateIDException if multiple exercises with the same ID are found.
     * @param id ID of exercise
     * @return Exercise if found, null if not found
     */
    public Exercise getExerciseById(final int id) throws DuplicateIDException {
        List<Exercise> exerciseList =
                this.exerciseList.stream().filter(exercise -> exercise.getId() == id).toList();
        if (exerciseList.size() == 1) {
            return exerciseList.get(0);
        } else if (exerciseList.isEmpty()) {
            return null;
        } else {
            throw new DuplicateIDException("Multiple exercises with the same ID found.");
        }
    }

    /**
     * Get workout log by ID.
     * Throws DuplicateIDException if multiple workout logs with the same ID are found.
     * @param id ID of workout log
     * @return WorkoutLog if found, null if not found
     */
    public WorkoutLog getWorkoutLogById(final int id) throws DuplicateIDException {
        List<WorkoutLog> workoutLogList =
                this.workoutLogList.stream().filter(workoutLog -> workoutLog.getId() == id).toList();
        if (workoutLogList.size() == 1) {
            return workoutLogList.get(0);
        } else if (workoutLogList.isEmpty()) {
            return null;
        } else {
            throw new DuplicateIDException("Multiple workout logs with the same ID found.");
        }
    }

    /**
     * Add a workout to the list of workouts.
     * Throws DuplicateIDException if a workout with the same ID is found.
     * (as that may be case if you load an old database before checks were added)
     * @param workoutPlan WorkoutPlan to add
     */
    public void addWorkout(final WorkoutPlan workoutPlan) throws DuplicateIDException {
        if (workoutPlanList.stream().anyMatch(workoutPlan1 -> workoutPlan1.getId() == workoutPlan.getId())) {
            throw new DuplicateIDException("Workout with ID " + workoutPlan.getId() + " already exists.");
        }

        workoutPlanList.add(workoutPlan);
        storageController.saveWorkouts(workoutPlanList);
    }

    /**
     * Add an exercise to the list of exercises.
     * Throws DuplicateIDException if an exercise with the same ID is found.
     * (as that may be case if you load an old database before checks were added)
     * @param exercise Exercise to add
     */
    public void addExercise(final Exercise exercise) throws DuplicateIDException {
        if (exerciseList.stream().anyMatch(exercise1 -> exercise1.getId() == exercise.getId())) {
            throw new DuplicateIDException("Exercise with ID " + exercise.getId() + " already exists.");
        }

        exerciseList.add(exercise);
        storageController.saveExercises(exerciseList);
    }

    /**
     * Add a workout log to the list of workout logs.
     * Throws DuplicateIDException if a workout log with the same ID is found.
     * (as that may be case if you load an old database before checks were added)
     * @param workoutLog WorkoutLog to add
     */
    public void addWorkoutLog(final WorkoutLog workoutLog) throws DuplicateIDException {
        if (workoutLogList.stream().anyMatch(workoutLog1 -> workoutLog1.getId() == workoutLog.getId())) {
            throw new DuplicateIDException("Workout log with ID " + workoutLog.getId() + " already exists.");
        }

        workoutLogList.add(workoutLog);
        storageController.saveWorkoutLogs(workoutLogList);
    }

    /**
     * Gets the next available ID for exercises.
     * @return Next available ID
     */
    public int getNextAvailableExerciseId() {
        return exerciseList.stream().mapToInt(Exercise::getId).max().orElse(0) + 1;
    }

    /**
     * Gets the next available ID for workouts.
     * @return Next available ID
     */
    public int getNextAvailableWorkoutId() {
        return workoutPlanList.stream().mapToInt(WorkoutPlan::getId).max().orElse(0) + 1;
    }

    /**
     * Gets the next available ID for workout logs.
     * @return Next available ID
     */
    public int getNextAvailableWorkoutLogId() {
        return workoutLogList.stream().mapToInt(WorkoutLog::getId).max().orElse(0) + 1;
    }
}
