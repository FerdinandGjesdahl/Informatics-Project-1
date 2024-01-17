package itp.gr23.elevatu.webservice;

import itp.gr23.elevatu.api.logic.UserManager;
import itp.gr23.elevatu.api.logic.WorkoutManager;
import itp.gr23.elevatu.api.storage.JSONStorageController;
import itp.gr23.elevatu.api.storage.StorageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

@SpringBootApplication
public class ElevatUService {

    /** Defualt constructor. */
    public ElevatUService() {  // for some reason spring boot requires constructor
        storageController = new JSONStorageController();

        userManager = new UserManager(storageController);
        workoutManager = new WorkoutManager(storageController);

    }

    /** Application wide UserManager. */
    private static  UserManager userManager;

    /** Application wide StorageController. */
    private static StorageController storageController;

    /** Application wide WorkoutManager. */
    private static WorkoutManager workoutManager;

    /**
     * Returns the UserManager used by the application.
     * @return the used UserManager
     */
    public static UserManager getUserManager() {
        return userManager;
    }

    /**
     * Returns the StorageController used by the application.
     * @return the used StorageController
     */
    public static StorageController getStorageController() {
        return storageController;
    }

    /**
     * Returns the WorkoutManager used by the application.
     * @return the used WorkoutManager
     */
    public static WorkoutManager getWorkoutManager() {
        return workoutManager;
    }

    /**
     * Main method. Launch Spring REST server.
     * @param args arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatUService.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }
}
