package itp.gr23.elevatu.objects;

import itp.gr23.elevatu.protos.ElevatUProtos;

import java.util.Objects;

/**
 * Class describing an exercise that is planned for a workout.
 * Eg. Bench press, 3 sets, 10 reps per set
 * Consists of the exercise itself as well as how many sets and repetitions should be performed
 */
public final class WorkoutPlanExercise {
    private final Exercise exercise;
    private int sets;
    private int reps;

    private static final int DEFAULT_SETS = 3;
    private static final int DEFAULT_REPS = 10;

    /**
     * Constructor for WorkoutPlanExercise.
     * @param exercise Exercise to be performed
     * @param sets Goal sets
     * @param reps Goal repetitions
     */
    public WorkoutPlanExercise(final Exercise exercise, final int sets, final int reps) {
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
    }

    /**
     * Constructor for WorkoutPlanExercise. Assumes 3 sets and 10 reps.
     * @param exercise Exercise to be performed.
     */
    public WorkoutPlanExercise(final Exercise exercise) {
        this(exercise, DEFAULT_SETS, DEFAULT_REPS);
    }

    /**
     * Gets goal amount sets to be performed.
     * @return Sets to be performed
     */
    public int getSets() {
        return sets;
    }

    /**
     * Sets goal sets to be performed.
     * @param sets Sets to be performed
     */
    public void setSets(final int sets) {
        this.sets = sets;
    }

    /**
     * Gets goal reps to be performed.
     * @return Amount of reps to be performed
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets goal reps to be performed.
     * @param reps Amount of reps to be performed
     */
    public void setReps(final int reps) {
        this.reps = reps;
    }

    /**
     * Gets Exercise to be performed.
     * @return Exercise to be performed
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * Creates protobuf instance of WorkoutPlanExercise.
     * @param workoutPlanExercise WorkoutPlanExercise object
     * @return Protobuf instance of WorkoutPlanExercise object.
     */
    public static ElevatUProtos.WorkoutPlanExercise toProto(final WorkoutPlanExercise workoutPlanExercise) {
        return ElevatUProtos.WorkoutPlanExercise.newBuilder()
                .setExercise(Exercise.toProto(workoutPlanExercise.exercise))
                .setReps(workoutPlanExercise.reps)
                .setSets(workoutPlanExercise.sets)
                .build();
    }

    /**
     * Creates WorkoutPlanExercise instance from protobuf.
     * @param protoWorkoutPlanExercise Protobuf instance of WorkoutPlanExercise
     * @return WorkoutPlanExercise instance
     */
    public static WorkoutPlanExercise fromProto(final ElevatUProtos.WorkoutPlanExercise protoWorkoutPlanExercise) {
        return new WorkoutPlanExercise(Exercise.fromProto(protoWorkoutPlanExercise.getExercise()),
                protoWorkoutPlanExercise.getSets(),
                protoWorkoutPlanExercise.getReps());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutPlanExercise that = (WorkoutPlanExercise) o;
        return sets == that.sets && reps == that.reps && Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise, sets, reps);
    }
}
