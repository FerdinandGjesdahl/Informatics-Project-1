package itp.gr23.elevatu.webservice;

import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import itp.gr23.elevatu.api.logic.UserManager;
import itp.gr23.elevatu.api.logic.WorkoutManager;
import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.protos.ElevatUNetworkProtos;
import itp.gr23.elevatu.protos.ElevatUProtos;
import itp.gr23.elevatu.webservice.controllers.UserRESTController;
import itp.gr23.elevatu.webservice.controllers.WorkoutRESTController;
import itp.gr23.elevatu.sample_data.SampleDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
public class TestWorkoutMethods {

    private WorkoutManager workoutManager;
    private WorkoutRESTController restController;
    private UserManager userManager;

    private ElevatUNetworkProtos.UserSession validSession;

    @BeforeEach
    public void setup() {
        workoutManager = StaticManagersHacker.getAndSetCurrentWorkoutManager();
        restController = new WorkoutRESTController();

        userManager = StaticManagersHacker.getAndSetCurrentUserManager();
        userManager.createUser("username", "password");
        validSession = new UserRESTController().login("username", "password");
    }

    @Test
    public void testAddWorkoutPlan() {
        ElevatUProtos.WorkoutPlan wp = WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan());

        wp = wp.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.WorkoutPlanAddRequest request = ElevatUNetworkProtos.WorkoutPlanAddRequest.newBuilder()
                .setUserSession(validSession)
                .setWorkoutPlan(wp)
                .build();

        ElevatUProtos.WorkoutPlan wp2 = restController.addWorkout(request);

        assertEquals(wp.getName(), wp2.getName());
        assertEquals(wp.getOwner(), wp2.getOwner());
        assertEquals(wp.getExerciseListList(), wp2.getExerciseListList());

        // return correct id
        ElevatUProtos.WorkoutPlan wp3 = restController.addWorkout(request);
        assertEquals(wp3.getId(), wp2.getId() + 1);

        // wrong session
        ElevatUNetworkProtos.WorkoutPlanAddRequest request2 = request.toBuilder()
                .setUserSession(ElevatUNetworkProtos.UserSession.newBuilder().build())
                .build();
        assertThrows(ResponseStatusException.class, () -> restController.addWorkout(request2));

        // wrong owner
        ElevatUProtos.WorkoutPlan wp4 = wp.toBuilder().setOwner("wrong").build();
        ElevatUNetworkProtos.WorkoutPlanAddRequest request3 = request.toBuilder()
                .setWorkoutPlan(wp4)
                .build();
        assertThrows(ResponseStatusException.class, () -> restController.addWorkout(request3));
    }

    @Test
    public void testAddExercise() {
        ElevatUProtos.Exercise e = Exercise.toProto(SampleDataGenerator.getRandomExercise());

        e = e.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.ExerciseAddRequest request = ElevatUNetworkProtos.ExerciseAddRequest.newBuilder()
                .setUserSession(validSession)
                .setExercise(e)
                .build();

        ElevatUProtos.Exercise e2 = restController.addExercise(request);

        assertEquals(e.getName(), e2.getName());
        assertEquals(e.getOwner(), e2.getOwner());

        // return correct id
        ElevatUProtos.Exercise e3 = restController.addExercise(request);
        assertEquals(e3.getId(), e2.getId() + 1);

        // wrong session
        ElevatUNetworkProtos.ExerciseAddRequest request2 = request.toBuilder()
                .setUserSession(ElevatUNetworkProtos.UserSession.newBuilder().build())
                .build();
        assertThrows(ResponseStatusException.class, () -> restController.addExercise(request2));

        // wrong owner
        ElevatUProtos.Exercise e4 = e.toBuilder().setOwner("wrong").build();
        ElevatUNetworkProtos.ExerciseAddRequest request3 = request.toBuilder()
                .setExercise(e4)
                .build();
        assertThrows(ResponseStatusException.class, () -> restController.addExercise(request3));
    }

    @Test
    public void testAddWorkoutLog() {
        ElevatUProtos.WorkoutLog wl = WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog());

        wl = wl.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.WorkoutLogAddRequest request = ElevatUNetworkProtos.WorkoutLogAddRequest.newBuilder()
                .setUserSession(validSession)
                .setWorkoutLog(wl)
                .build();

        ElevatUProtos.WorkoutLog wl2 = restController.addWorkoutLog(request);

        assertEquals(wl.getOwner(), wl2.getOwner());
        assertEquals(wl.getDate(), wl2.getDate());
        assertEquals(wl.getDate(), wl2.getDate());
        assertEquals(wl.getPerformedSetsList(), wl2.getPerformedSetsList());

        // return correct id
        ElevatUProtos.WorkoutLog wl3 = restController.addWorkoutLog(request);
        assertEquals(wl3.getId(), wl2.getId() + 1);

        // wrong session
        ElevatUNetworkProtos.WorkoutLogAddRequest request2 = request.toBuilder()
                .setUserSession(ElevatUNetworkProtos.UserSession.newBuilder().build())
                .build();
        assertThrows(ResponseStatusException.class, () -> restController.addWorkoutLog(request2));

        // wrong owner
        ElevatUProtos.WorkoutLog wl4 = wl.toBuilder().setOwner("wrong").build();
        ElevatUNetworkProtos.WorkoutLogAddRequest request3 = request.toBuilder()
                .setWorkoutLog(wl4)
                .build();
        assertThrows(ResponseStatusException.class, () -> restController.addWorkoutLog(request3));
    }

    @Test
    public void testGetExercisesForUser() throws DuplicateIDException {
        ElevatUProtos.Exercise e = Exercise.toProto(SampleDataGenerator.getRandomExercise());
        e = e.toBuilder().setOwner("username").build();

        ElevatUProtos.Exercise e2 = Exercise.toProto(SampleDataGenerator.getRandomExercise());
        e2 = e2.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.ExerciseList expected = ElevatUNetworkProtos.ExerciseList.newBuilder()
                .addExercises(e)
                .addExercises(e2)
                .build();

        // empty
        assertEquals(
                ElevatUNetworkProtos.ExerciseList.newBuilder().build()
                , restController.getExercises("username", validSession));

        workoutManager.addExercise(Exercise.fromProto(e));
        workoutManager.addExercise(Exercise.fromProto(e2));


        ElevatUNetworkProtos.ExerciseList actual = restController.getExercises("username", validSession);

        assertEquals(expected, actual);

        // wrong session
        assertThrows(ResponseStatusException.class, () -> restController.getExercises("username",
                ElevatUNetworkProtos.UserSession.newBuilder().build()));

        // wrong owner
        assertThrows(ResponseStatusException.class, () -> restController.getExercises("wrong",
                validSession));

    }

    @Test
    public void testGetWorkoutsForUser() throws DuplicateIDException {
        ElevatUProtos.WorkoutPlan wp = WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan());
        wp = wp.toBuilder().setOwner("username").build();

        ElevatUProtos.WorkoutPlan wp2 = WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan());
        wp2 = wp2.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.WorkoutPlanList expected = ElevatUNetworkProtos.WorkoutPlanList.newBuilder()
                .addWorkoutPlans(wp)
                .addWorkoutPlans(wp2)
                .build();

        // empty
        assertEquals(
                ElevatUNetworkProtos.WorkoutPlanList.newBuilder().build()
                , restController.getWorkouts("username", validSession));

        workoutManager.addWorkout(WorkoutPlan.fromProto(wp));
        workoutManager.addWorkout(WorkoutPlan.fromProto(wp2));

        ElevatUNetworkProtos.WorkoutPlanList actual = restController.getWorkouts("username", validSession);

        assertEquals(expected, actual);

        // wrong session
        assertThrows(ResponseStatusException.class, () -> restController.getWorkouts("username",
                ElevatUNetworkProtos.UserSession.newBuilder().build()));

        // wrong owner
        assertThrows(ResponseStatusException.class, () -> restController.getWorkouts("wrong",
                validSession));
    }


    @Test
    public void testGetWorkoutLogsForUser() throws DuplicateIDException {
        ElevatUProtos.WorkoutLog wl = WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog());
        wl = wl.toBuilder().setOwner("username").build();

        ElevatUProtos.WorkoutLog wl2 = WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog());
        wl2 = wl2.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.WorkoutLogList expected = ElevatUNetworkProtos.WorkoutLogList.newBuilder()
                .addWorkoutLogs(wl)
                .addWorkoutLogs(wl2)
                .build();

        // empty
        assertEquals(
                ElevatUNetworkProtos.WorkoutLogList.newBuilder().build()
                , restController.getWorkoutLogs("username", validSession));

        workoutManager.addWorkoutLog(WorkoutLog.fromProto(wl));
        workoutManager.addWorkoutLog(WorkoutLog.fromProto(wl2));

        ElevatUNetworkProtos.WorkoutLogList actual = restController.getWorkoutLogs("username", validSession);

        assertEquals(expected, actual);

        // wrong session
        assertThrows(ResponseStatusException.class, () -> restController.getWorkoutLogs("username",
                ElevatUNetworkProtos.UserSession.newBuilder().build()));

        // wrong owner
        assertThrows(ResponseStatusException.class, () -> restController.getWorkoutLogs("wrong",
                validSession));
    }

    @Test
    public void testGetExerciseById() throws DuplicateIDException {
        ElevatUProtos.Exercise e = Exercise.toProto(SampleDataGenerator.getRandomExercise());
        e = e.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.ExerciseAddRequest request = ElevatUNetworkProtos.ExerciseAddRequest.newBuilder()
                .setUserSession(validSession)
                .setExercise(e)
                .build();

        ElevatUProtos.Exercise e2 = restController.addExercise(request);

        ElevatUProtos.Exercise e3 = restController.getExercise(e2.getId(), validSession);

        assertEquals(e2, e3);

        // no exercise
        assertThrows(ResponseStatusException.class, () -> restController.getExercise(-1, validSession));

        // wrong session
        assertThrows(ResponseStatusException.class, () -> restController.getExercise(e2.getId(),
                ElevatUNetworkProtos.UserSession.newBuilder().build()));


        ElevatUProtos.Exercise eDiffOwner = Exercise.toProto(SampleDataGenerator.getRandomExercise());
        workoutManager.addExercise(Exercise.fromProto(eDiffOwner));

        // wrong owner
        assertThrows(ResponseStatusException.class, () -> restController.getExercise(eDiffOwner.getId(),
                validSession));
    }

    @Test
    public void testGetWorkoutPlanById() throws DuplicateIDException {
        ElevatUProtos.WorkoutPlan wp = WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan());
        wp = wp.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.WorkoutPlanAddRequest request = ElevatUNetworkProtos.WorkoutPlanAddRequest.newBuilder()
                .setUserSession(validSession)
                .setWorkoutPlan(wp)
                .build();

        ElevatUProtos.WorkoutPlan wp2 = restController.addWorkout(request);

        ElevatUProtos.WorkoutPlan wp3 = restController.getWorkout(wp2.getId(), validSession);

        assertEquals(wp2, wp3);

        // no workout
        assertThrows(ResponseStatusException.class, () -> restController.getWorkout(-1, validSession));

        // wrong session
        assertThrows(ResponseStatusException.class, () -> restController.getWorkout(wp2.getId(),
                ElevatUNetworkProtos.UserSession.newBuilder().build()));

        ElevatUProtos.WorkoutPlan wpDiffOwner = WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan());
        workoutManager.addWorkout(WorkoutPlan.fromProto(wpDiffOwner));

        // wrong owner
        assertThrows(ResponseStatusException.class, () -> restController.getWorkout(wpDiffOwner.getId(),
                validSession));
    }

    @Test
    public void testGetWorkoutLogById() throws DuplicateIDException {
        ElevatUProtos.WorkoutLog wl = WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog());
        wl = wl.toBuilder().setOwner("username").build();

        ElevatUNetworkProtos.WorkoutLogAddRequest request = ElevatUNetworkProtos.WorkoutLogAddRequest.newBuilder()
                .setUserSession(validSession)
                .setWorkoutLog(wl)
                .build();

        ElevatUProtos.WorkoutLog wl2 = restController.addWorkoutLog(request);

        ElevatUProtos.WorkoutLog wl3 = restController.getWorkoutLog(wl2.getId(), validSession);

        assertEquals(wl2, wl3);

        // no workout log
        assertThrows(ResponseStatusException.class, () -> restController.getWorkoutLog(-1, validSession));

        // wrong session
        assertThrows(ResponseStatusException.class, () -> restController.getWorkoutLog(wl2.getId(),
                ElevatUNetworkProtos.UserSession.newBuilder().build()));

        ElevatUProtos.WorkoutLog wlDiffOwner = WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog());
        workoutManager.addWorkoutLog(WorkoutLog.fromProto(wlDiffOwner));

        // wrong owner
        assertThrows(ResponseStatusException.class, () -> restController.getWorkoutLog(wlDiffOwner.getId(),
                validSession));
    }


}
