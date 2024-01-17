package itp.gr23.elevatu.webservice.controllers;

import itp.gr23.elevatu.objects.Exercise;
import itp.gr23.elevatu.objects.WorkoutLog;
import itp.gr23.elevatu.objects.WorkoutPlan;
import itp.gr23.elevatu.protos.ElevatUNetworkProtos;
import itp.gr23.elevatu.protos.ElevatUProtos;
import itp.gr23.elevatu.webservice.ElevatUService;
import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RequestMapping("/workoutapi")
@RestController
public final class WorkoutRESTController {

    /**
     * Gets Workouts for user.
     * @param username User to check
     * @param userSession Session object for logged in session.
     * @return List of workouts
     */
    @PostMapping("/workouts/plans/list/{username}")
    public ElevatUNetworkProtos.WorkoutPlanList getWorkouts(final @PathVariable String username,
                                          final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }
        if (!(username.equals(userSession.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only check own workouts.");
        }

        return ElevatUNetworkProtos.WorkoutPlanList.newBuilder()
                .addAllWorkoutPlans(ElevatUService.getWorkoutManager().getWorkoutsForUser(username).stream()
                        .map(itp.gr23.elevatu.objects.WorkoutPlan::toProto).toList())
                .build();
    }

    /**
     * Gets a workout by ID.
     * @param id ID of workout to get
     * @param userSession Session object for logged in session.
     * @return Workout
     */
    @PostMapping("/workouts/plans/id/{id}")
    public ElevatUProtos.WorkoutPlan getWorkout(final @PathVariable int id,
                                                final @RequestBody ElevatUNetworkProtos.UserSession userSession) {
        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }

        try {
            WorkoutPlan plan = ElevatUService.getWorkoutManager().getWorkoutById(id);

            if (plan == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found.");
            }

            if (!(plan.getOwner().equals(userSession.getUsername()))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only check own workouts.");
            }

            return WorkoutPlan.toProto(plan);
        } catch (DuplicateIDException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate ID in database.");
        }
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
        ElevatUProtos.WorkoutPlan workoutPlan = wpAddRequest.getWorkoutPlan();
        ElevatUNetworkProtos.UserSession userSession = wpAddRequest.getUserSession();


        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }
        if (!(workoutPlan.getOwner().equals(userSession.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only add own workouts.");
        }

        ElevatUProtos.WorkoutPlan idSwapped = workoutPlan.toBuilder().setId(
                ElevatUService.getWorkoutManager().getNextAvailableWorkoutId()
        ).build();

        WorkoutPlan workoutPlanObject = WorkoutPlan.fromProto(idSwapped);

        try {
            ElevatUService.getWorkoutManager().addWorkout(workoutPlanObject);
        } catch (DuplicateIDException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return idSwapped;
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
        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }
        if (!(username.equals(userSession.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only check own exercises.");
        }

        return ElevatUNetworkProtos.ExerciseList.newBuilder()
                .addAllExercises(ElevatUService.getWorkoutManager().getExercisesForUser(username).stream()
                        .map(itp.gr23.elevatu.objects.Exercise::toProto).toList())
                .build();
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
        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }

        try {
            Exercise exercise = ElevatUService.getWorkoutManager().getExerciseById(id);

            if (exercise == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found.");
            }

            if (!(exercise.getOwner().equals(userSession.getUsername()))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only check own exercises.");
            }

            return Exercise.toProto(exercise);
        } catch (DuplicateIDException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate ID in database.");
        }
    }

    /**
     * Adds an exercise to the database.
     * Sets ID to next available ID.
     * @param exAddRequest Exercise Add Request (user session and exercise)
     * @return Exercise (with ID set)
     */
    @PostMapping("/exercises/add")
    public ElevatUProtos.Exercise addExercise(final @RequestBody ElevatUNetworkProtos.ExerciseAddRequest exAddRequest) {
        ElevatUProtos.Exercise exercise = exAddRequest.getExercise();
        ElevatUNetworkProtos.UserSession userSession = exAddRequest.getUserSession();

        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }
        if (!(exercise.getOwner().equals(userSession.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only add own exercises.");
        }

        ElevatUProtos.Exercise idSwapped = exercise.toBuilder().setId(
                ElevatUService.getWorkoutManager().getNextAvailableExerciseId()
        ).build();

        Exercise exerciseObject = Exercise.fromProto(idSwapped);

        try {
            ElevatUService.getWorkoutManager().addExercise(exerciseObject);
        } catch (DuplicateIDException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return idSwapped;
    }

    /**
     * Add a workout log to the database.
     * @param wlAddRequest Workout Log Add Request (user session and workout log)
     * @return Workout log (with ID set)
     */
    @PostMapping("/workouts/logs/addWorkoutLog")
    public ElevatUProtos.WorkoutLog addWorkoutLog(final @RequestBody
                                                      ElevatUNetworkProtos.WorkoutLogAddRequest wlAddRequest) {
        ElevatUProtos.WorkoutLog workoutLog = wlAddRequest.getWorkoutLog();
        ElevatUNetworkProtos.UserSession userSession = wlAddRequest.getUserSession();

        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }

        if (!(workoutLog.getOwner().equals(userSession.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only add own workout logs.");
        }

        ElevatUProtos.WorkoutLog idSwapped = workoutLog.toBuilder().setId(
                ElevatUService.getWorkoutManager().getNextAvailableWorkoutLogId()
        ).build();

        WorkoutLog workoutLogObject = WorkoutLog.fromProto(idSwapped);

        try {
            ElevatUService.getWorkoutManager().addWorkoutLog(workoutLogObject);
        } catch (DuplicateIDException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return idSwapped;
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
        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }
        if (!(username.equals(userSession.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only check own workout logs.");
        }

        return ElevatUNetworkProtos.WorkoutLogList.newBuilder()
                .addAllWorkoutLogs(ElevatUService.getWorkoutManager().getWorkoutLogsForUser(username).stream()
                        .map(itp.gr23.elevatu.objects.WorkoutLog::toProto).toList())
                .build();
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
        if (!ElevatUService.getUserManager().sessionCorrect(userSession.getUsername(), userSession.getSecret())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User session secret wrong.");
        }

        try {
            WorkoutLog workoutLog = ElevatUService.getWorkoutManager().getWorkoutLogById(id);

            if (workoutLog == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout log not found.");
            }

            if (!(workoutLog.getOwner().equals(userSession.getUsername()))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only check own workout logs.");
            }

            return WorkoutLog.toProto(workoutLog);
        } catch (DuplicateIDException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate ID in database.");
        }
    }
}
