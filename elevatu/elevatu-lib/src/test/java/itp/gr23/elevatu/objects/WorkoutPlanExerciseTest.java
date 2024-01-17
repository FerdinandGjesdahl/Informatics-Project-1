package itp.gr23.elevatu.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkoutPlanExerciseTest {

    // Also tests Exercise.equals
    @Test
    public void testEquals() {
        WorkoutPlanExercise wp1 = new WorkoutPlanExercise(new Exercise(1, "Henrik", "Bench Press"), 3, 5);
        WorkoutPlanExercise wp2 = new WorkoutPlanExercise(new Exercise(1, "Henrik", "Bench Press"), 3, 5);

        assertEquals(wp1, wp2);
        assertEquals(wp1.hashCode(), wp2.hashCode());
    }
}
