package itp.gr23.elevatu.objects;

import itp.gr23.elevatu.protos.ElevatUProtos;

import java.util.Objects;

/**
    Class describing an exercise (eg. Bench Press).
    Contains name of exercise as well as optional notes regarding its execution.
 */
public final class Exercise {

    /** Username of User owning exercise. */
    private final String owner;

    /** Exercise id. */
    private final int id;

    /** Exercise name. */
    private String name;
    /** Notes regarding the exercise. */
    private String notes;

    /**
     * Create Exercise without supplying notes.
     * @param id Object id
     * @param owner Username of whoever owns the exercise
     * @param name Name of exercise (eg. Bench Press).
     */
    public Exercise(final int id, final String owner, final String name) {
        this(id, owner, name, "");
    }

    /**
     * Create Exercise supplying notes.
     * @param id Object id
     * @param name Name of exercise (eg. Bench Press)
     * @param owner Username of whoever owns the exercise
     * @param notes Notes regarding exercise (eg. using dumbbells)
     */
    public Exercise(final int id, final String owner, final String name, final String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Get name of exercise.
     * @return name of exercise
     */
    public String getName() {
        return name;
    }

    /**
     * Change name of exercise.
     * @param name new name to use
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get exercise notes.
     * @return exercise notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set exercise notes.
     * @param notes new notes to use
     */
    public void setNotes(final String notes) {
        this.notes = notes;
    }

    /**
     * Gets the username of who owns the exercise.
     * @return Username of User who owns exercise.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets id of Exercise object.
     * @return id of Exercise object.
     */
    public int getId() {
        return id;
    }

    /**
     * Creates protobuf instance of Exercise.
     * @param exercise Exercise object
     * @return Protobuf instance of Exercise object.
     */
    public static ElevatUProtos.Exercise toProto(final Exercise exercise) {
        return ElevatUProtos.Exercise.newBuilder()
                .setId(exercise.id)
                .setName(exercise.name)
                .setNotes(exercise.notes)
                .setOwner(exercise.owner)
                .build();
    }

    /**
     * Creates Exercise instance from protobuf.
     * @param protoExercise Protobuf instance of Exercise
     * @return Exercise instance
     */
    public static Exercise fromProto(final ElevatUProtos.Exercise protoExercise) {
        return new Exercise(
                protoExercise.getId(),
                protoExercise.getOwner(),
                protoExercise.getName(),
                protoExercise.getNotes());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id == exercise.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notes);
    }
}
