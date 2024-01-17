package itp.gr23.elevatu.objects;

import itp.gr23.elevatu.protos.ElevatUProtos;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtobufTest {

    public final String testUsername = "lorem";
    public final String testPassword = "ipsum";


    @Test
    public void testExerciseProtobuf(){
        Exercise exercise = new Exercise(1, testUsername, "Bench Press", "random notes");

        ElevatUProtos.Exercise protoExercise = Exercise.toProto(exercise);
        assertEquals(exercise, Exercise.fromProto(protoExercise));
        assertEquals(exercise.getOwner(), protoExercise.getOwner());
        assertEquals(exercise.getName(), protoExercise.getName());
        assertEquals(exercise.getNotes(), protoExercise.getNotes());
        assertEquals(exercise.getId(), protoExercise.getId());

        ElevatUProtos.Exercise proto = ElevatUProtos.Exercise.newBuilder()
                .setId(0)
                .setName("Squat")
                .setOwner("some owner")
                .setNotes("Some notes")
                .build();
        Exercise protoParsed = Exercise.fromProto(proto);

        assertEquals(proto.getId(), protoParsed.getId());
        assertEquals(proto.getOwner(), protoParsed.getOwner());
        assertEquals(proto.getName(), protoParsed.getName());
        assertEquals(proto.getNotes(), protoParsed.getNotes());

    }

    @Test
    public void testUserProtobuf(){
        User testUser = User.createFromPlaintextPassword(testUsername, testPassword);

        ElevatUProtos.User protoUser = User.toProto(testUser);
        User decoded = User.fromProto(protoUser);

        assertEquals(protoUser.getPasswordHash(), decoded.getHashString());
        assertEquals(protoUser.getPasswordSalt(), decoded.getSaltString());
        assertEquals(protoUser.getUsername(), decoded.getUsername());
    }

    @Test
    public void testWorkoutLogProtobuf(){
        final WorkoutLog workoutLog = new WorkoutLog(1, testUsername);

        workoutLog.addSet(new WorkoutLogSet(new Exercise(1, testUsername, "Bench press"), 10, 3));

        ElevatUProtos.WorkoutLog protonized = WorkoutLog.toProto(workoutLog);
        WorkoutLog decoded = WorkoutLog.fromProto(protonized);

        assertEquals(protonized.getId(), decoded.getId());
        assertEquals(protonized.getOwner(), decoded.getOwner());
        assertEquals(protonized.getPerformedSetsList().size(), decoded.getPerformedSets().size());
    }

    @Test
    public void testWorkoutLogSet(){
        WorkoutLogSet workoutLogSet = new WorkoutLogSet(new Exercise(1, testUsername, "Bench Press")
        , 3, 10);

        assertEquals(workoutLogSet,
                WorkoutLogSet.fromProto(WorkoutLogSet.toProto(workoutLogSet)));
    }

    @Test
    public void testWorkoutPlan(){
        final WorkoutPlan workoutPlan = new WorkoutPlan(1, testUsername,
                "testExercise", "testNotes", new ArrayList<>());

        workoutPlan.addExercise(new Exercise(1, testUsername, "Barbell Bench Press"));

        ElevatUProtos.WorkoutPlan protonized = WorkoutPlan.toProto(workoutPlan);

        WorkoutPlan decoded = WorkoutPlan.fromProto(protonized);

        assertEquals(protonized.getId(), decoded.getId());
        assertEquals(protonized.getOwner(), decoded.getOwner());
        assertEquals(protonized.getName(), decoded.getName());
        assertEquals(protonized.getNotes(), decoded.getNotes());
        assertEquals(protonized.getExerciseListList().size(), decoded.getExercises().size());
    }
}
