package itp.gr23.elevatu.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutPlanTest {

    @Test
    public void testToString() {
        WorkoutPlan wp = new WorkoutPlan(1, "Henrik", "Starting Strength");

        assertEquals("Starting Strength", wp.toString());
    }

    @Test
    public void testAddExercise() {
        WorkoutPlan wp = new WorkoutPlan(1, "Henrik", "Starting Strength");
        Exercise e = new Exercise(1, "Henrik", "Bench Press");
        WorkoutPlanExercise wpe = new WorkoutPlanExercise(e, 3, 5);

        wp.addExercise(wpe);

        assertEquals(1, wp.getExercises().size());
        assertEquals(wpe, wp.getExercises().get(0));
    }

    @Test
    public void testRemoveExerciseByIndex() {
        WorkoutPlan wp = new WorkoutPlan(1, "Henrik", "Starting Strength");
        Exercise e = new Exercise(1, "Henrik", "Bench Press");
        WorkoutPlanExercise wpe = new WorkoutPlanExercise(e, 3, 5);

        wp.addExercise(wpe);

        assertTrue(wp.removeExercise(0));

        assertEquals(0, wp.getExercises().size());

        assertFalse(wp.removeExercise(0));
    }

}
