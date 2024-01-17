package itp.gr23.elevatu.api.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.User;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.api.storage.json.UserSerializer;

import java.lang.reflect.Type;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class JSONStorageController implements StorageController {

    /**
     * Prefix to the name of the file the data will be stored in.
     * The file name will have a format of {prefix}_appData.json .
     * */
    private final String filenamePrefix;
    /**
     * Get a new JSONStorageController object setting a custom filename prefix.
     * @param filenamePrefix The filename prefix to use
     */
    public JSONStorageController(final String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        gson = new GsonBuilder().setPrettyPrinting().
                registerTypeAdapter(User.class, new UserSerializer()).
                create(); // Use pretty printing
    }


    /**
     * Get a new JSONStorageController object without
     * setting a custom filename prefix.
     * The file name will in that case just be appData.json .
     */
    public JSONStorageController() {
        this("");
    }

    /** gson object for data serialization and deserialization. */
    private final Gson gson;

    /** Type for List<User> for gson. */
    private static final Type USER_JSON_TYPE =
            new TypeToken<List<User>>() { }.getType();

    /** Type for List<WorkoutPlan> for gson. */
    private static final Type WORKOUT_PLAN_JSON_TYPE =
            new TypeToken<List<WorkoutPlan>>() { }.getType();

    /** Type for List<Exercise> for gson. */
    private static final Type EXERCISE_JSON_TYPE =
            new TypeToken<List<Exercise>>() { }.getType();

    /** Type for List<WorkoutLog> for gson. */
    private static final Type WORKOUT_LOG_TYPE =
            new TypeToken<List<WorkoutLog>>() { }.getType();

    @Override
    public void saveUsers(final List<User> userList) {
        String jsonized = gson.toJson(userList, USER_JSON_TYPE);
        saveToFile(getUserFilename(), jsonized);
    }

    @Override
    public List<User> loadUsers() {
        String fileContents = readFromFile(getUserFilename());
        if (fileContents == null) {
            return new ArrayList<>();
        }
        return gson.fromJson(fileContents, USER_JSON_TYPE);
    }

    @Override
    public List<WorkoutPlan> loadWorkouts() {
        String fileContents = readFromFile(getWorkoutPlanFilename());
        if (fileContents == null) {
            return new ArrayList<>();
        }
        return gson.fromJson(fileContents, WORKOUT_PLAN_JSON_TYPE);
    }

    @Override
    public List<Exercise> loadExercises() {
        String fileContents = readFromFile(getExercisesFilename());
        if (fileContents == null) {
            return new ArrayList<>();
        }
        return gson.fromJson(fileContents, EXERCISE_JSON_TYPE);
    }

    @Override
    public List<WorkoutLog> loadWorkoutLogs() {
        String fileContents = readFromFile(getWorkoutLogFilename());
        if (fileContents == null) {
            return new ArrayList<>();
        }
        return gson.fromJson(fileContents, WORKOUT_LOG_TYPE);
    }

    @Override
    public void saveWorkouts(final List<WorkoutPlan> workoutPlans) {
        String jsonized = gson.toJson(workoutPlans, WORKOUT_PLAN_JSON_TYPE);

        saveToFile(getWorkoutPlanFilename(), jsonized);
    }

    @Override
    public void saveExercises(final List<Exercise> exerciseList) {
        String jsonized = gson.toJson(exerciseList, EXERCISE_JSON_TYPE);

        saveToFile(getExercisesFilename(), jsonized);
    }

    @Override
    public void saveWorkoutLogs(final List<WorkoutLog> workoutLogList) {
        String jsonized = gson.toJson(workoutLogList, WORKOUT_LOG_TYPE);

        saveToFile(getWorkoutLogFilename(), jsonized);
    }


    /**
     * Gets the name of the file to store User objects.
     * @return File name
     */
    private String getUserFilename() {
        return filenamePrefix.isEmpty() ? "users.json" : filenamePrefix + "_users.json";
    }

    /**
     * Gets the name of the file to store WorkoutPlan objects.
     * @return File name
     */
    private String getWorkoutPlanFilename() {
        return filenamePrefix.isEmpty() ? "workoutPlans.json" : filenamePrefix + "_workoutPlans.json";
    }

    /**
     * Gets the name of the file to store Exercise objects.
     * @return File name
     */
    private String getExercisesFilename() {
        return filenamePrefix.isEmpty() ? "exercises.json" : filenamePrefix + "_exercises.json";
    }

    /**
     * Gets the name of the file to store WorkoutLog objects.
     * @return File name
     */
    private String getWorkoutLogFilename() {
        return filenamePrefix.isEmpty() ? "workoutLogs.json" : filenamePrefix + "_workoutLogs.json";
    }

    /**
     * Saves String  to file.
     * @param filename
     * @param string
     * @return true whenever no error occured
     */
    private boolean saveToFile(final String filename, final String string) {
        try (PrintWriter printWriter = new PrintWriter(
                filename, StandardCharsets.UTF_8)) {
            printWriter.write(string);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Could not create file to print. ");
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println("Could not get access to write to file "
                    + getUserFilename());
        } catch (IOException e) {
            System.out.println("I/O exception happened.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reads from file and returns as String.
     * @param filename Filename
     * @return File contents as String, null if error occured
     */
    private String readFromFile(final String filename) {
        File dataFile = new File(filename);
        if (!dataFile.exists()) {
            System.out.println("Data file " + dataFile.getAbsolutePath() + " does not exist. Returning empty.");
            return null;
        }
        try {
            return new String(Files.readAllBytes(dataFile.toPath()));

        } catch (IOException e) {
            System.out.println("Could not read contents from "
                    + dataFile.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }
}
