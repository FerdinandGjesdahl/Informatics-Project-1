package itp.gr23.elevatu.api.storage;

import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.User;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.objects.WorkoutLog;

import java.util.List;

/**
 * Interface specifying the standard for saving/loading application data.
 */
public interface StorageController {

    /**
     * Saves a list of Users to storage.
     * @param userList list of User objects to save.
     */
    void saveUsers(List<User> userList);

    /**
     * Loads a list of Users from storage to a List object.
     * @return a List of all available users.
     */
    List<User> loadUsers();

    /**
     * Loads a list of WorkoutPlan from storage.
     * @return List of available WorkoutPlan's
     */
    List<WorkoutPlan> loadWorkouts();

    /**
     * Loads a list of Exercise from storage.
     * @return List of all saved Exercise-s
     */
    List<Exercise> loadExercises();

    /**
     * Loads a list of WorkoutLog from storage.
     * @return List of all saved WorkoutLog-s
     */
    List<WorkoutLog> loadWorkoutLogs();

    /**
     * Saves a list of WorkoutPlan to storage.
     * @param workoutPlans List of WorkoutPlan objects to save.
     */
    void saveWorkouts(List<WorkoutPlan> workoutPlans);

    /**
     * Saves a list of Exercise objects to storage.
     * @param exerciseList List of Exercise objects to save.
     */
    void saveExercises(List<Exercise> exerciseList);

    /**
     * Saves a list of WorkoutLog objects to storage.
     * @param workoutLogList List of WorkoutLog objects to save.
     */
    void saveWorkoutLogs(List<WorkoutLog> workoutLogList);
}
