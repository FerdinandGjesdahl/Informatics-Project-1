package itp.gr23.elevatu.api.storage_controllers;

import itp.gr23.elevatu.api.storage.JSONStorageController;
import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.User;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.sample_data.SampleDataGenerator;
import itp.gr23.elevatu.api.JSONStorageControllerHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONStorageControllerTest {

    @BeforeEach
    public void setup(){
        JSONStorageController testController = getTestController();

        JSONStorageControllerHelpers.clearJSONController(testController);
    }

    private JSONStorageController getTestController(){
        return new JSONStorageController("test");
    }

    @Test
    public void testGetFilenamesNoPrefix() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONStorageController testController = new JSONStorageController();

        assertEquals("users.json", JSONStorageControllerHelpers.getControllerFileName("getUserFilename",
                testController));
        assertEquals("workoutPlans.json", JSONStorageControllerHelpers.getControllerFileName("getWorkoutPlanFilename",
                testController));
        assertEquals("exercises.json", JSONStorageControllerHelpers.getControllerFileName("getExercisesFilename",
                testController));
        assertEquals("workoutLogs.json", JSONStorageControllerHelpers.getControllerFileName("getWorkoutLogFilename",
                testController));
    }

    @Test
    public void testGetFilenamesPrefix() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONStorageController testController = new JSONStorageController("loremIpsum");

        assertEquals("loremIpsum_users.json",
                JSONStorageControllerHelpers.getControllerFileName("getUserFilename", testController));
        assertEquals("loremIpsum_workoutPlans.json",
                JSONStorageControllerHelpers.getControllerFileName("getWorkoutPlanFilename", testController));
        assertEquals("loremIpsum_exercises.json",
                JSONStorageControllerHelpers.getControllerFileName("getExercisesFilename", testController));
        assertEquals("loremIpsum_workoutLogs.json",
                JSONStorageControllerHelpers.getControllerFileName("getWorkoutLogFilename", testController));
    }

    @Test
    public void testFilenameUserPrefix() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException{
        assertEquals("loremIpsum_users.json", JSONStorageControllerHelpers.getControllerFileName(
                "getUserFilename",
                new JSONStorageController("loremIpsum")));
    }

    @Test
    public void testLoadingAndSavingUsers(){
        Random r = new Random();
        List<User> testUsers = new ArrayList<>();

        for (int i = 0; i < r.nextInt(10, 20); i++){
            testUsers.add(SampleDataGenerator.getRandomUser());
        }

        JSONStorageController testController = getTestController();

        testController.saveUsers(testUsers);

        List<User> loadedUsers = testController.loadUsers();

        assertEquals(testUsers.size(), loadedUsers.size());
        assertEquals(testUsers, loadedUsers);
    }

    @Test
    public void testLoadingAndSavingWorkoutLogs(){
        Random random = new Random();
        List<WorkoutLog> testWorkoutLogs = new ArrayList<>();

        for (int i = 0; i < random.nextInt(10, 20); i++){
            testWorkoutLogs.add(SampleDataGenerator.getRandomWorkoutLog());
        }

        JSONStorageController testController = getTestController();

        testController.saveWorkoutLogs(testWorkoutLogs);

        List<WorkoutLog> loadedWorkoutLogs = testController.loadWorkoutLogs();

        assertEquals(testWorkoutLogs.size(), loadedWorkoutLogs.size());
        assertEquals(testWorkoutLogs, loadedWorkoutLogs);
    }

    @Test
    public void testLoadingAndSavingWorkoutPlans(){
        Random random = new Random();
        List<WorkoutPlan> testWorkoutPlans = new ArrayList<>();

        for (int i = 0; i < random.nextInt(10, 20); i++){
            testWorkoutPlans.add(SampleDataGenerator.getRandomWorkoutPlan());
        }

        JSONStorageController testController = getTestController();

        testController.saveWorkouts(testWorkoutPlans);

        List<WorkoutPlan> loadedWorkoutPlans = testController.loadWorkouts();

        assertEquals(testWorkoutPlans.size(), loadedWorkoutPlans.size());
        assertEquals(testWorkoutPlans, loadedWorkoutPlans);
    }

    @Test
    public void testLoadingAndSavingExercises(){
        Random random = new Random();
        List<Exercise> testExercises = new ArrayList<>();

        for (int i = 0; i < random.nextInt(10, 20); i++){
            testExercises.add(SampleDataGenerator.getRandomExercise());
        }

        JSONStorageController testController = getTestController();

        testController.saveExercises(testExercises);

        List<Exercise> loadedExercises = testController.loadExercises();

        assertEquals(testExercises.size(), loadedExercises.size());
        assertEquals(testExercises, loadedExercises);
    }
}
