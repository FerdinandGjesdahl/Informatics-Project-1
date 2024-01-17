package itp.gr23.elevatu.webservice.controllers;

import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.protos.ElevatUNetworkProtos;
import itp.gr23.elevatu.protos.ElevatUProtos;
import itp.gr23.elevatu.sample_data.SampleDataGenerator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Class for testing the API and getting sample data for the frontend.
 */
@CrossOrigin
@RequestMapping("/test_workoutapi")
@RestController
public final class TestDataRESTController {

    /**
     * Gets sample workout data list.
     * @param username Some username
     * @param userSession Some session
     * @return List of workouts
     */
    @PostMapping("/workouts/plans/list/{username}")
    public ElevatUNetworkProtos.WorkoutPlanList getWorkouts(final @PathVariable String username,
                                          final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        ElevatUNetworkProtos.WorkoutPlanList.Builder wbp = ElevatUNetworkProtos.WorkoutPlanList.newBuilder();

        for (int i = 0; i < new Random().nextInt(4); i++) {
            wbp.addWorkoutPlans(
                    WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan()));
        }

        return wbp.build();
    }

    /**
     * Gets a sample workout by ID.
     * @param id ID of workout to get
     * @param userSession Session object for logged in session.
     * @return Workout
     */
    @PostMapping("/workouts/plans/id/{id}")
    public ElevatUProtos.WorkoutPlan getWorkout(final @PathVariable int id,
                                                final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        return WorkoutPlan.toProto(SampleDataGenerator.getRandomWorkoutPlan())
                .toBuilder().setId(id).build();
    }

    /**
     * Adds a workout to the database.
     * Sets ID to next available ID.
     * @param wpAddRequest Workout Add Request (user session and workout)
     * @return Workout
     */
    @PostMapping("/workouts/plans/add")
    public ElevatUProtos.WorkoutPlan addWorkout(final @RequestBody
                                                    ElevatUNetworkProtos.WorkoutPlanAddRequest wpAddRequest) {
        return wpAddRequest.getWorkoutPlan().toBuilder().setId(1).build();
    }



    /**
     * Gets Exercises for user.
     * @param username User to check
     * @param userSession Session object for logged in session.
     * @return List of exercises
     */
    @PostMapping("/exercises/list/{username}")
    public ElevatUNetworkProtos.ExerciseList getExercises(final @PathVariable String username,
                                           final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        ElevatUNetworkProtos.ExerciseList.Builder elb = ElevatUNetworkProtos.ExerciseList.newBuilder();

        for (int i = 0; i < new Random().nextInt(4); i++) {
            elb.addExercises(
                    Exercise.toProto(SampleDataGenerator.getRandomExercise()));
        }

        return elb.build();
    }

    /**
     * Gets a exercise by ID.
     * @param id ID of exercise to get
     * @param userSession Session object for logged in session.
     * @return Exercise
     */
    @PostMapping("/exercises/id/{id}")
    public ElevatUProtos.Exercise getExercise(final @PathVariable int id,
                                              final @RequestBody ElevatUNetworkProtos.UserSession userSession) {

        return Exercise.toProto(SampleDataGenerator.getRandomExercise()).toBuilder().setId(id).build();

    }

    /**
     * Adds an exercise to the database.
     * Sets ID to next available ID.
     * @param exAddRequest Exercise Add Request (user session and exercise)
     * @return Exercise (with ID set)
     */
    @PostMapping("/exercises/add")
    public ElevatUProtos.Exercise addExercise(final @RequestBody ElevatUNetworkProtos.ExerciseAddRequest exAddRequest) {
        return exAddRequest.getExercise().toBuilder().setId(1).build();
    }

    /**
     * Add a workout log to the database.
     * @param wlAddRequest Workout Log Add Request (user session and workout log)
     * @return Workout log (with ID set)
     */
    @PostMapping("/workouts/logs/addWorkoutLog")
    public ElevatUProtos.WorkoutLog addWorkoutLog(final @RequestBody
                                                      ElevatUNetworkProtos.WorkoutLogAddRequest wlAddRequest) {
        return wlAddRequest.getWorkoutLog().toBuilder().setId(1).build();
    }

    /**
     * Gets WorkoutLogs for user.
     * @param username User to check
     * @param userSession Session object for logged in session.
     * @return List of workout logs
     */
    @PostMapping("/workouts/logs/list/{username}")
    public ElevatUNetworkProtos.WorkoutLogList getWorkoutLogs(final @PathVariable String username,
                                                  final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        ElevatUNetworkProtos.WorkoutLogList.Builder wlb = ElevatUNetworkProtos.WorkoutLogList.newBuilder();

        for (int i = 0; i < new Random().nextInt(4); i++) {
            wlb.addWorkoutLogs(
                    WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog()));
        }

        return wlb.build();
    }

    /**
     * Gets a workout log by ID.
     * @param id ID of workout log to get
     * @param userSession Session object for logged in session.
     * @return Workout log
     */
    @PostMapping("/workouts/logs/id/{id}")
    public ElevatUProtos.WorkoutLog getWorkoutLog(final @PathVariable int id,
                                                  final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        return WorkoutLog.toProto(SampleDataGenerator.getRandomWorkoutLog()).toBuilder().setId(id).build();
    }
}
