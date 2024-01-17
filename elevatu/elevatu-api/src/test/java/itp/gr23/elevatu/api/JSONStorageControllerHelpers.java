package itp.gr23.elevatu.api;

import itp.gr23.elevatu.api.storage.JSONStorageController;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JSONStorageControllerHelpers {
    public static void clearJSONController(JSONStorageController controller){
        try {
            tryDeleteControllerFile("getUserFilename", controller);
            tryDeleteControllerFile("getWorkoutLogFilename", controller);
            tryDeleteControllerFile("getWorkoutPlanFilename", controller);
            tryDeleteControllerFile("getExercisesFilename", controller);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void tryDeleteControllerFile(String typeFilename, JSONStorageController controller) throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {
        File file = new File(getControllerFileName(typeFilename, controller));
        if (file.exists()){
            file.delete();
        }
    }

    public static String getControllerFileName(String methodName, JSONStorageController controller) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method method = JSONStorageController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);

        return (String) method.invoke(controller);
    }
}
