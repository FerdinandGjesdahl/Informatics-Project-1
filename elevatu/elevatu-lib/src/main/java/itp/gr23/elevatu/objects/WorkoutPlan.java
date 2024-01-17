package itp.gr23.elevatu.objects;

import itp.gr23.elevatu.protos.ElevatUProtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
Class describing a workout plan created by the user.
Contains the name of the workout, the goal, notes as well as a list of the exercises to be performed
(including goal repetitions and sets)
 Example WorkoutPlan might be Push Day, listing exercises to perform when training push.
 */
public final class WorkoutPlan {

    private final int id;
    private String name;
    private String notes;
    private final ArrayList<WorkoutPlanExercise> exerciseList;
    private final String owner;

    /**
     * Constructor for WorkoutPlan.
     * @param owner User who created workout
     * @param name Name of workout (eg. Push day)
     * @param notes Notes for workout (eg. requires freeweight rack)
     * @param exerciseList List of exercises to perform
     * @param id Object id
     */
    public WorkoutPlan(final int id, final String owner, final String name, final String notes,
                       final ArrayList<WorkoutPlanExercise> exerciseList) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.notes = notes;
        this.exerciseList = exerciseList;
    }

    /** Simple constructor for WorkoutPlan needing only name for workout.
     * @param owner User who created workout
     * @param name Name of workout (eg. Push day)
     * @param id Object id
     */
    public WorkoutPlan(final int id, final String owner, final String name) {
        this(id, owner, name, "", new ArrayList<WorkoutPlanExercise>());
    }


    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Add exercise to be performed during workout.
     * @param workoutPlanExercise WorkoutPlanExercise to be performed
     */
    public void addExercise(final WorkoutPlanExercise workoutPlanExercise) {
        exerciseList.add(workoutPlanExercise);
    }

    /**
     * Add exercise to be performed during workout.
     * Complex version.
     * @param exercise Exercise to be performed
     * @param sets Sets of exercise
     * @param reps Repetitions of exercise
     */
    public void addExercise(final Exercise exercise, final int sets, final int reps) {
        addExercise(new WorkoutPlanExercise(exercise, sets, reps));
    }

    /**
     * Add exercise to be performed during workout.
     * Simple version.
     * @param exercise
     */
    public void addExercise(final Exercise exercise) {
        addExercise(new WorkoutPlanExercise(exercise));
    }

    /**
     * Returns plan of exercises to be performed in workout.
     * @return List of WorkoutPlanExercise's to be performed.
     */
    public List<WorkoutPlanExercise> getExercises() {
        return exerciseList;
    }

    /**
     * Remove exercise from plan.
     * @param exerciseIndex Index of exercise to be removed
     * @return true whenever index in bounds and exercise removed.
     */
    public boolean removeExercise(final int exerciseIndex) {
        if (exerciseIndex >= exerciseList.size()) return false;
        else {
            exerciseList.remove(exerciseIndex);
            return true;
        }
    }

    /**
     * Get name of workout (eg. Push day).
     * @return Name of workout
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of workout plan (eg. Push day).
     * @param name New name for workout plan
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get notes regarding workout.
     * @return Notes regarding workout
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set notes regarding workout.
     * @param notes Notes regarding workout.
     */
    public void setNotes(final String notes) {
        this.notes = notes;
    }

    /**
     * Gets user who has made this workout plan.
     * @return username of User
     */
    public String getOwner() {
        return owner;
    }


    /**
     * Gets id of WorkoutPlan object.
     * @return id of WorkoutPlan object.
     */
    public int getId() {
        return id;
    }

    /**
     * Creates protobuf instance of WorkoutPlan.
     * @param workoutPlan WorkoutPlan object
     * @return Protobuf instance of WorkoutPlan object.
     */
    public static ElevatUProtos.WorkoutPlan toProto(final WorkoutPlan workoutPlan) {
        List<ElevatUProtos.WorkoutPlanExercise> protoExercises = new ArrayList<>();

        workoutPlan.exerciseList.forEach((exercise) -> {
            protoExercises.add(WorkoutPlanExercise.toProto(exercise));
        });

        return ElevatUProtos.WorkoutPlan.newBuilder()
                .setName(workoutPlan.name)
                .setNotes(workoutPlan.notes)
                .setOwner(workoutPlan.owner)
                .setId(workoutPlan.id)
                .addAllExerciseList(protoExercises)
                .build();
    }

    /**
     * Creates WorkoutPlan instance from protobuf.
     * @param protoWorkoutPlan Protobuf instance of WorkoutPlan
     * @return WorkoutPlan instance
     */
    public static WorkoutPlan fromProto(final ElevatUProtos.WorkoutPlan protoWorkoutPlan) {
        ArrayList<WorkoutPlanExercise> exerciseList = new ArrayList<>();

        protoWorkoutPlan.getExerciseListList().forEach((exercise) -> {
            exerciseList.add(WorkoutPlanExercise.fromProto(exercise));
        });

        return new WorkoutPlan(
                protoWorkoutPlan.getId(),
                protoWorkoutPlan.getOwner(),
                protoWorkoutPlan.getName(), protoWorkoutPlan.getNotes(),
                exerciseList);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutPlan that = (WorkoutPlan) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notes, exerciseList, owner);
    }
}
