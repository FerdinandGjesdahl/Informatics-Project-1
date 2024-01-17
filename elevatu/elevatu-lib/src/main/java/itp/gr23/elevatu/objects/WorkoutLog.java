package itp.gr23.elevatu.objects;

import com.google.protobuf.Timestamp;
import itp.gr23.elevatu.protos.ElevatUProtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Class describing a log of a workout session.
 * Consists of actually performed exercises during a workout session,
 * including how many repetitions and sets were performed
 */
public final class WorkoutLog {

    private final int id;
    private final ArrayList<WorkoutLogSet> performedSets;
    private final Date workoutDate;
    private final String owner;

    /**
     * Class constructor featuring a workout log with no performed sets and
     * date sat to current moment.
     * @param id Object id
     * @param owner User who did workout
     */
    public WorkoutLog(final int id, final String owner) {
        this(id, owner, new Date());
    }

    /**
     * Class constructor featuring workout log with no performed sets and
     * specified date.
     * @param id Object id
     * @param workoutDate Date of workout
     * @param owner User who did workout
     */
    public WorkoutLog(final int id, final String owner, final Date workoutDate) {
        this.performedSets = new ArrayList<>();
        this.workoutDate = workoutDate;
        this.owner = owner;
        this.id = id;
    }

    /**
     * Gets a list of performed sets.
     * @return List of performed sets
     */
    public ArrayList<WorkoutLogSet> getPerformedSets() {
        return performedSets;
    }

    /**
     * Add a set that has been performed to the log.
     * @param set Set performed
     */
    public void addSet(final WorkoutLogSet set) {
        performedSets.add(set);
    }

    /**
     * Get the date the workout happened.
     * @return Date of the workout
     */
    public Date getWorkoutDate() {
        return workoutDate;
    }

    /**
     * Remove a performed set from the log.
     * @param setIndex Index of the set to remove
     * @return true whenever the index was in bound and removed
     */
    public boolean removeSet(final int setIndex) {
        if (setIndex > performedSets.size()) return false;
        performedSets.remove(setIndex);
        return true;
    }

    /**
     * Gets user who has performed this workout.
     * @return username for the matching user
     */
    public String getOwner() {
        return owner;
    }


    /**
     * Gets id of WorkoutLog object.
     * @return id of WorkoutLog object.
     */
    public int getId() {
        return id;
    }

    /**
     * Creates protobuf instance of WorkoutLog.
     * @param workoutLog WorkoutLog object
     * @return Protobuf instance of WorkoutLog object.
     */
    public static ElevatUProtos.WorkoutLog toProto(final WorkoutLog workoutLog) {
        ArrayList<ElevatUProtos.WorkoutLogSet> protoSets = new ArrayList<>();

        workoutLog.performedSets.forEach((performedSet) -> {
            protoSets.add(WorkoutLogSet.toProto(performedSet));
        });

        Instant instant = workoutLog.workoutDate.toInstant();

        return ElevatUProtos.WorkoutLog.newBuilder()
                .setOwner(workoutLog.owner)
                .addAllPerformedSets(protoSets)
                .setDate(
                        Timestamp.newBuilder().
                                setSeconds(instant.getEpochSecond())
                                .setNanos(instant.getNano())
                                .build()
                )
                .setId(workoutLog.id)
                .build();

    }

    /**
     * Creates WorkoutLog instance from protobuf.
     * @param protoWorkoutLog Protobuf instance of WorkoutLog
     * @return WorkoutLog instance
     */
    public static WorkoutLog fromProto(final ElevatUProtos.WorkoutLog protoWorkoutLog) {
        Timestamp time = protoWorkoutLog.getDate();

        WorkoutLog workoutLog =  new WorkoutLog(
                protoWorkoutLog.getId(),
                protoWorkoutLog.getOwner(),
                Date.from(Instant.ofEpochSecond(time.getSeconds(), time.getNanos())));

        protoWorkoutLog.getPerformedSetsList().forEach((performedSet) -> {
            workoutLog.addSet(WorkoutLogSet.fromProto(performedSet));
        });

        return workoutLog;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutLog that = (WorkoutLog) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, performedSets, workoutDate, owner);
    }
}
