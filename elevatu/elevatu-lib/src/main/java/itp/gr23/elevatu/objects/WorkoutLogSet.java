package itp.gr23.elevatu.objects;


import itp.gr23.elevatu.protos.ElevatUProtos;

import java.util.Objects;

/**
Class describing a set performed in a workout.
 For example, a 3x10 session of Bench Press,
 where the user lifts 10x60 kg, 8x60kg and 8x55 kg corresponds to
 3 x WorkoutLogSet:
    - Bench Press, 10 reps, 60 kg
    - Bench Press, 8 reps, 60 kg
    - Bench Press, 8 reps, 55 kg
Each set consists of an exercise, repetitions and weight.
 */
public final class WorkoutLogSet {
    private final Exercise exercise;
    private int reps;
    private float weight;

    /**
     * Gets the exercise performed.
     * @return Exercise performed
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * Gets number of repetitions of exercise performed.
     * @return Number of repetitions performed.
     */
    public int getReps() {
        return reps;
    }

    /**
     * Set the number of repetitions performed.
     * @param reps Number of sets performed
     */
    public void setReps(final int reps) {
        this.reps = reps;
    }

    /**
     * Gets the weight lifted in the performed sets (in kg).
     * @return Weight lifted in performed sets (kg)
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set weight lifted in performed sets (in kg).
     * @param weight Weight lifted in performed sets (kg)
     */
    public void setWeight(final float weight) {
        this.weight = weight;
    }

    /**
     * Constructor for WorkoutLogSet.
     * @param exercise Exercise performed
     * @param reps Repetitions performed of exercise
     * @param weight Weight lifted during those repetitions
     */
    public WorkoutLogSet(final Exercise exercise, final int reps, final float weight) {
        this.exercise = exercise;
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * Creates protobuf instance of WorkoutLogSet.
     * @param workoutLogSet WorkoutLogSet object
     * @return Protobuf instance of WorkoutLogSet object.
     */
    public static ElevatUProtos.WorkoutLogSet toProto(final WorkoutLogSet workoutLogSet) {
        return ElevatUProtos.WorkoutLogSet.newBuilder()
                .setExercise(Exercise.toProto(workoutLogSet.exercise))
                .setWeight(workoutLogSet.weight)
                .setReps(workoutLogSet.reps)
                .build();
    }

    /**
     * Creates WorkoutLogSet instance from protobuf.
     * @param protoWorkoutLogSet Protobuf instance of WorkoutLogSet
     * @return WorkoutLogSet instance
     */
    public static WorkoutLogSet fromProto(final ElevatUProtos.WorkoutLogSet protoWorkoutLogSet) {
        return new WorkoutLogSet(Exercise.fromProto(protoWorkoutLogSet.getExercise()),
                protoWorkoutLogSet.getReps(), protoWorkoutLogSet.getWeight());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutLogSet that = (WorkoutLogSet) o;
        return reps == that.reps && Float.compare(weight, that.weight) == 0 && Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise, reps, weight);
    }
}
