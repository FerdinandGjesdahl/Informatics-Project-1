package itp.gr23.elevatu.api.logic;

import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.api.JSONStorageControllerHelpers;
import itp.gr23.elevatu.sample_data.SampleDataGenerator;
import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import itp.gr23.elevatu.api.storage.JSONStorageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutManagerTest {
    private WorkoutManager workoutManager;

    @BeforeEach
    public void setup(){
        JSONStorageController storage = new JSONStorageController("test");

        JSONStorageControllerHelpers.clearJSONController(storage);

        workoutManager = new WorkoutManager(storage);
    }

    private void reflectionSetId(Object objectToSet, int id){
        try {
            Field idField = objectToSet.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(objectToSet, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void reflecionAddToList(Object objectToAdd, String listName){
        try {
            Field listField = workoutManager.getClass().getDeclaredField(listName);
            listField.setAccessible(true);
            List<Object> list = (List<Object>) listField.get(workoutManager);
            list.add(objectToAdd);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkWorkoutLogAddThrowsErrorOnDuplicate() throws DuplicateIDException {
        WorkoutLog wl = SampleDataGenerator.getRandomWorkoutLog();
        workoutManager.addWorkoutLog(wl);

        assertThrows(DuplicateIDException.class, () -> workoutManager.addWorkoutLog(wl));
    }

    @Test
    public void checkWorkoutPlanAddThrowsErrorOnDuplicate() throws DuplicateIDException {
        WorkoutPlan wp = SampleDataGenerator.getRandomWorkoutPlan();
        workoutManager.addWorkout(wp);

        assertThrows(DuplicateIDException.class, () -> workoutManager.addWorkout(wp));
    }

    @Test
    public void checkExerciseAddThrowsErrorOnDuplicate() throws DuplicateIDException {
        Exercise ex = SampleDataGenerator.getRandomExercise();
        workoutManager.addExercise(ex);

        assertThrows(DuplicateIDException.class, () -> workoutManager.addExercise(ex));
    }

    @Test
    public void testGetNextWorkoutId() throws DuplicateIDException {
        Random r = new Random();

        ArrayList<WorkoutPlan> workouts = new ArrayList<>();

        for (int i = 0; i < r.nextInt(100, 1000); i++){
            WorkoutPlan plan = SampleDataGenerator.getRandomWorkoutPlan();
            reflectionSetId(plan, workoutManager.getNextAvailableWorkoutId());
            workoutManager.addWorkout(plan);
            workouts.add(plan);
        }

        // check no collisions
        for (int i = 0; i < workouts.size(); i++){
            for (int j = 0; j < workouts.size(); j++){
                int id1 = workouts.get(i).getId();
                int id2 = workouts.get(j).getId();

                if (i != j){
                    assertFalse((id1 == id2));
                }
            }
        }
    }

    @Test
    public void testGetNextAvailableExerciseId() throws DuplicateIDException {
        Random r = new Random();

        ArrayList<Exercise> exercises = new ArrayList<>();

        for (int i = 0; i < r.nextInt(100, 1000); i++){
            Exercise ex = SampleDataGenerator.getRandomExercise();
            reflectionSetId(ex, workoutManager.getNextAvailableExerciseId());
            workoutManager.addExercise(ex);
            exercises.add(ex);
        }

        // check no collisions
        for (int i = 0; i < exercises.size(); i++){
            for (int j = 0; j < exercises.size(); j++){
                int id1 = exercises.get(i).getId();
                int id2 = exercises.get(j).getId();

                if (i != j){
                    assertFalse((id1 == id2));
                }
            }
        }
    }

    @Test
    public void testGetNextAvailableWorkoutLogId() throws DuplicateIDException {
        Random r = new Random();

        ArrayList<WorkoutLog> workoutLogs = new ArrayList<>();

        for (int i = 0; i < r.nextInt(100, 1000); i++){
            WorkoutLog wl = SampleDataGenerator.getRandomWorkoutLog();
            reflectionSetId(wl, workoutManager.getNextAvailableWorkoutLogId());
            workoutManager.addWorkoutLog(wl);
            workoutLogs.add(wl);
        }

        // check no collisions
        for (int i = 0; i < workoutLogs.size(); i++){
            for (int j = 0; j < workoutLogs.size(); j++){
                int id1 = workoutLogs.get(i).getId();
                int id2 = workoutLogs.get(j).getId();

                if (i != j){
                    assertFalse((id1 == id2));
                }
            }
        }
    }

    /**
     * Write tests for
     * getWorkoutById(int)	27	0%	4	0%	3	3	7	7	1	1
     * getExerciseById(int)	27	0%	4	0%	3	3	7	7	1	1
     * getWorkoutLogById(int)	27	0%	4	0%	3	3	7	7	1	1
     * getWorkoutsForUser(String)	8	0%		n/a	1	1	1	1	1	1
     * getExercisesForUser(String)	8	0%		n/a	1	1	1	1	1	1
     * getWorkoutLogsForUser(String)
     */

    @Test
    public void testGetWorkoutById() throws DuplicateIDException {
        WorkoutPlan wp = SampleDataGenerator.getRandomWorkoutPlan();
        workoutManager.addWorkout(wp);

        assertEquals(wp, workoutManager.getWorkoutById(wp.getId()));
    }

    @Test
    public void testGetWorkoutByIdEmpty() throws DuplicateIDException {
        assertNull(workoutManager.getWorkoutById(0));
    }

    @Test
    public void testGetWorkoutByIdDuplicate() throws DuplicateIDException {
        WorkoutPlan wp = SampleDataGenerator.getRandomWorkoutPlan();
        workoutManager.addWorkout(wp);

        WorkoutPlan wp2 = new WorkoutPlan(wp.getId(), wp.getOwner(), "Some other workout");

        reflecionAddToList(wp2, "workoutPlanList");

        assertThrows(DuplicateIDException.class, () -> workoutManager.getWorkoutById(wp.getId()));
    }

    @Test
    public void testGetWorkoutsForUser() throws DuplicateIDException {
        WorkoutPlan wp = SampleDataGenerator.getRandomWorkoutPlan();
        workoutManager.addWorkout(wp);

        WorkoutPlan wp2 = new WorkoutPlan(workoutManager.getNextAvailableWorkoutId(),
                wp.getOwner(), "Some other workout");

        workoutManager.addWorkout(wp2);

        assertEquals(2, workoutManager.getWorkoutsForUser(wp.getOwner()).size());
        assertArrayEquals(new WorkoutPlan[] {wp, wp2},
                workoutManager.getWorkoutsForUser(wp.getOwner()).toArray());
    }

    // Same tests for but exercise
    @Test
    public void testGetExerciseById() throws DuplicateIDException {
        Exercise ex = SampleDataGenerator.getRandomExercise();
        workoutManager.addExercise(ex);

        assertEquals(ex, workoutManager.getExerciseById(ex.getId()));
    }

    @Test
    public void testGetExerciseByIdEmpty() throws DuplicateIDException {
        assertNull(workoutManager.getExerciseById(0));
    }

    @Test
    public void testGetExerciseByIdDuplicate() throws DuplicateIDException {
        Exercise ex = SampleDataGenerator.getRandomExercise();
        workoutManager.addExercise(ex);

        Exercise ex2 = new Exercise(ex.getId(), ex.getOwner(), "Some other exercise");

        reflecionAddToList(ex2, "exerciseList");

        assertThrows(DuplicateIDException.class, () -> workoutManager.getExerciseById(ex.getId()));
    }

    @Test
    public void testGetExercisesForUser() throws DuplicateIDException {
        Exercise ex = SampleDataGenerator.getRandomExercise();
        workoutManager.addExercise(ex);

        Exercise ex2 = new Exercise(workoutManager.getNextAvailableExerciseId(),
                ex.getOwner(), "Some other exercise");

        workoutManager.addExercise(ex2);

        assertEquals(2, workoutManager.getExercisesForUser(ex.getOwner()).size());
        assertArrayEquals(new Exercise[] {ex, ex2},
                workoutManager.getExercisesForUser(ex.getOwner()).toArray());
    }

    @Test
    public void testGetWorkoutLogById() throws DuplicateIDException {
        WorkoutLog wl = SampleDataGenerator.getRandomWorkoutLog();
        workoutManager.addWorkoutLog(wl);

        assertEquals(wl, workoutManager.getWorkoutLogById(wl.getId()));
    }

    @Test
    public void testGetWorkoutLogByIdEmpty() throws DuplicateIDException {
        assertNull(workoutManager.getWorkoutLogById(0));
    }

    @Test
    public void testGetWorkoutLogByIdDuplicate() throws DuplicateIDException {
        WorkoutLog wl = SampleDataGenerator.getRandomWorkoutLog();
        workoutManager.addWorkoutLog(wl);

        WorkoutLog wl2 = new WorkoutLog(wl.getId(), wl.getOwner());

        reflecionAddToList(wl2, "workoutLogList");

        assertThrows(DuplicateIDException.class, () -> workoutManager.getWorkoutLogById(wl.getId()));
    }

    @Test
    public void testGetWorkoutLogsForUser() throws DuplicateIDException {
        WorkoutLog wl = SampleDataGenerator.getRandomWorkoutLog();
        workoutManager.addWorkoutLog(wl);

        WorkoutLog wl2 = new WorkoutLog(workoutManager.getNextAvailableWorkoutLogId(),
                wl.getOwner());

        workoutManager.addWorkoutLog(wl2);

        assertEquals(2, workoutManager.getWorkoutLogsForUser(wl.getOwner()).size());
        assertArrayEquals(new WorkoutLog[] {wl, wl2},
                workoutManager.getWorkoutLogsForUser(wl.getOwner()).toArray());
    }

}
