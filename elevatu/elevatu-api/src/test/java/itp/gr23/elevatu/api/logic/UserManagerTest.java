package itp.gr23.elevatu.api.logic;

import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import itp.gr23.elevatu.objects.User;
import itp.gr23.elevatu.api.storage.JSONStorageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private UserManager userManager;
    private Random random;

    @BeforeEach
    public void setup(){
        File test_data_file = new File("test_users.json");
        if (test_data_file.exists()){
            test_data_file.delete();
        }

        JSONStorageController jsonStorageController = new JSONStorageController("test");
        userManager = new UserManager(jsonStorageController);
        random = new Random();
    }

    private List<User> reflectionGetUsers() {
        try {
            Field usersField = userManager.getClass().getDeclaredField("users");
            usersField.setAccessible(true);
            return (List<User>) usersField.get(userManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testCreateProfile() {
        userManager.createUser("testUser", "testPassword");
        List<User> users = reflectionGetUsers();
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
        assertTrue(users.get(0).passwordCorrect("testPassword"));
    }

    @Test
    public void testUserExists(){
        userManager.createUser("loremIpsum", "test123");
        assertTrue(userManager.userExists("loremIpsum"));
        assertFalse(userManager.userExists("not_loremIpsum"));
    }

    @Test
    public void testCredentialsCheck(){
        userManager.createUser("loremIpsum", "test123");
        assertTrue(userManager.credentialsCorrect("loremIpsum", "test123"));
        assertFalse(userManager.credentialsCorrect("loremIpsum", "not_test123"));
    }

    @Test
    public void testCreateUserDuplicateThrows() throws IllegalArgumentException{
        userManager.createUser("test", "pass");

        assertThrows(IllegalArgumentException.class, () -> {
            userManager.createUser("test", "pass2");
        });
    }

    @Test
    public void testGettingUserWhenDuplicateExists() throws DuplicateIDException {
        User u = User.createFromPlaintextPassword("test", "test2");
        User u2 = User.createFromPlaintextPassword("test", "test3");

        reflectionGetUsers().add(u);
        reflectionGetUsers().add(u2);

        assertThrows(DuplicateIDException.class, () -> {
            userManager.getUser("test");
        });
    }

    @Test
    public void testGettingUserNotExist() throws DuplicateIDException {
        assertNull(userManager.getUser("test"));
    }

    @Test
    public void testGettingUser() throws DuplicateIDException {
        User u = userManager.createUser("test", "test2");

        assertEquals(u, userManager.getUser("test"));
    }

    @Test
    public void testSessionIsCorrect() {
        User u = userManager.createUser("test", "test");

        String session = userManager.createNewSession(u);

        assertTrue(userManager.sessionCorrect(u.getUsername(), session));
        assertFalse(userManager.sessionCorrect(u.getUsername(), "not_correct_session"));
    }

    @Test
    public void testSessionIsRandomMedium() {
        User u = userManager.createUser("test", "test");

        List<String> someSessions = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            String session = userManager.createNewSession(u);
            assertFalse(someSessions.contains(session));
            someSessions.add(session);
        }
    }

}
