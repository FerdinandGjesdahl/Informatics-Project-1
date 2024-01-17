package itp.gr23.elevatu.webservice;

import itp.gr23.elevatu.api.logic.UserManager;
import itp.gr23.elevatu.api.logic.WorkoutManager;
import itp.gr23.elevatu.api.storage.JSONStorageController;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StaticManagersHacker {
    /*
    This is an unly hack to set the managers in the webservice to the same as in the api.
    This is required as we are not starting the application from the main method, but from a test.


    This is superugly and should probably never be done, but the deadline is soon and we have no time rewrite the
    REST controllers to not use the static managers
     */

    // stolen from elevatu-api
    private static void clearJSONController(JSONStorageController controller){
        try {
            tryDeleteControllerFile("getUserFilename", controller);
            tryDeleteControllerFile("getWorkoutLogFilename", controller);
            tryDeleteControllerFile("getWorkoutPlanFilename", controller);
            tryDeleteControllerFile("getExercisesFilename", controller);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // stolen from elevatu-api
    private static void tryDeleteControllerFile(String typeFilename, JSONStorageController controller) throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {
        File file = new File(getControllerFileName(typeFilename, controller));
        if (file.exists()){
            file.delete();
        }
    }
    private static String getControllerFileName(String methodName, JSONStorageController controller) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method method = JSONStorageController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);

        return (String) method.invoke(controller);
    }


    public static JSONStorageController getAndSetCurrentStorageController() {
        JSONStorageController controller = new JSONStorageController("test");

        clearJSONController(controller);

        // set in elevatu service using reflection
        try {
            Field f = ElevatUService.class.getDeclaredField("storageController");
            f.setAccessible(true);
            f.set(null, controller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return controller;
    }

    public static UserManager getAndSetCurrentUserManager() {
        UserManager manager = new UserManager(getAndSetCurrentStorageController());

        // set in elevatu service using reflection
        try {
            Field f = ElevatUService.class.getDeclaredField("userManager");
            f.setAccessible(true);
            f.set(null, manager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return manager;
    }

    public static WorkoutManager getAndSetCurrentWorkoutManager() {
        WorkoutManager manager = new WorkoutManager(getAndSetCurrentStorageController());

        // set in elevatu service using reflection
        try {
            Field f = ElevatUService.class.getDeclaredField("workoutManager");
            f.setAccessible(true);
            f.set(null, manager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return manager;
    }
}
