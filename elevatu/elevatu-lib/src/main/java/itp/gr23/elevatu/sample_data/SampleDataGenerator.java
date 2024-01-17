package itp.gr23.elevatu.sample_data;

import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.User;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutLogSet;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.objects.WorkoutPlanExercise;

import java.util.Date;
import java.util.Random;

public final class SampleDataGenerator {
    private SampleDataGenerator() {
        // no instances
    }

    private static String[] exampleUsernames = {"Henrik", "Steffen", "Anders", "Hakon"};
    private static String[] exampleExerciseNames = {"Bench Press", "Squat", "Deadlift",
            "Overhead Press", "Barbell Row" };
    private static String[] exampleWorkoutPlans = {"Starting Strength", "Stronglifts 5x5", "PPL" };

    /**
     * Generates random string of length between 10 and 20.
     * @return a random string
     */
    static String generateRandomString() {
        String abcd = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder password = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < r.nextInt(10, 20); i++) {
            password.append(abcd.charAt(r.nextInt(0, abcd.length())));
        }

        return password.toString();
    }

    /**
     * Generates a random user.
     * @return a random user
     */
    public static User getRandomUser() {
        Random r = new Random();
        return User.createFromPlaintextPassword(exampleUsernames[r.nextInt(0, exampleUsernames.length)],
                generateRandomString());
    }

    /**
     * Generates a random exercise.
     * @return a random exercise
     */
    public static Exercise getRandomExercise() {
        Random r = new Random();

        return new Exercise(r.nextInt(10000000),
                getRandomUser().getUsername(),
                exampleExerciseNames[r.nextInt(0, exampleExerciseNames.length)]);
    }

    /**
     * Generates a random workout log set.
     * @return a random workout log set
     */
    public static WorkoutLogSet getRandomWorkoutLogSet() {
        Random r = new Random();

        return new WorkoutLogSet(getRandomExercise(),
                r.nextInt(1, 10),
                r.nextFloat(0, 100));
    }

    /**
     * Generates a random workout plan exercise.
     * @return a random workout plan exercise
     */
    public static WorkoutPlanExercise getRandomWorkoutPlanExercise() {
        Random r = new Random();

        return new WorkoutPlanExercise(getRandomExercise(),
                r.nextInt(1, 10),
                r.nextInt(1, 10));
    }

    /**
     * Generates a random workout plan.
     * @return a random workout plan
     */
    public static WorkoutPlan getRandomWorkoutPlan() {
        Random r = new Random();

        WorkoutPlan wp = new WorkoutPlan(r.nextInt(10000000),
                getRandomUser().getUsername(),
                exampleWorkoutPlans[r.nextInt(0, exampleWorkoutPlans.length)]);

        for (int i = 0; i < r.nextInt(3, 10); i++) {
            wp.addExercise(getRandomWorkoutPlanExercise());
        }

        return wp;
    }

    /**
     * Generates a random workout log.
     * @return a random workout log
     */
    public static WorkoutLog getRandomWorkoutLog() {
        Random r = new Random();

        WorkoutLog wl = new WorkoutLog(r.nextInt(10000000),
                getRandomUser().getUsername(),
                new Date());

        for (int i = 0; i < r.nextInt(3, 10); i++) {
            wl.addSet(getRandomWorkoutLogSet());
        }

        return wl;
    }
}
