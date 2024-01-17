package itp.gr23.elevatu.api.logic;

import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import itp.gr23.elevatu.api.storage.StorageController;
import itp.gr23.elevatu.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UserManager {

    private static final int SECRET_LENGTH = 26;

    /** List of loaded users. */
    private final List<User> users = new ArrayList<>();

    /** User sessions to take care of who is logged in and who not. */
    private final HashMap<String, User> userSessions = new HashMap<>();

    /** StorageController in use by the application. */
    private final StorageController storageController;

    /**
     * Create a UserManager specifying the StorageController to use.
     * @param storageController the StorageController we wish to use
     */
    public UserManager(final StorageController storageController) {

        this.storageController = storageController;
        this.loadUsers();
    }

    /**
     * Creates a new user with the specified credentials.
     * Throws IllegalArgumentException if the user already exists.
     * @param username Username
     * @param password Password
     * @return User object
     */
    public final User createUser(final String username, final String password) throws IllegalArgumentException {
        if (this.userExists(username)) {
            throw new IllegalArgumentException("User already exists");
        }

        User u = User.createFromPlaintextPassword(username, password);
        users.add(u);
        this.saveUsers();

        return u;
    }

    /**
     * Save users to storage.
     */
    public final void saveUsers() {
        storageController.saveUsers(users);
    }

    /**
     * Load users from storage.
     */
    private void loadUsers() {
        this.users.clear();
        this.users.addAll(storageController.loadUsers());
    }

    /**
     * Check if a user of the supplied name exists.
     * @param username Username
     * @return true whenever the supplied user exists.
     */
    public final boolean userExists(final String username) {
        return this.users.stream().anyMatch(
                user -> user.getUsername().equals(username));
    }

    /**
     * Check if a user's credentials are correct.
     * @param username Username
     * @param password Password
     * @return true whenever the credentials match a user.
     */
    public boolean credentialsCorrect(
            final String username, final String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)
                    && u.passwordCorrect(password)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Gets an user by username.
     * @param username Username
     * Throws DuplicateIDException if the user already exists.
     * (required as check for duplicate usernames wasnt before in earlier versions)
     * @return User object from username
     */
    public User getUser(final String username) throws DuplicateIDException {
        List<User> users = this.users.stream().filter(user -> user.getUsername().equals(username)).toList();
        if (users.size() > 1) {
            throw new DuplicateIDException("Multiple users with the same username");
        } else if (users.size() == 1) {
            return users.get(0);
        } else return null;
    }

    /**
     * Create new session for user.
     * Is used to check whenever the user is logged in.
     * @param user User object
     * @return Secret for session
     */
    public String createNewSession(final User user) {
        String secret = null;
        do {
            secret = getRandomSecret();
        } while (userSessions.containsKey(secret));

        userSessions.put(secret, user);
        return secret;
    }

    /**
     * Generate new secret.
     * Consists of 26 random characters.
     * @return Secret
     */
    private static final String ABC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String getRandomSecret() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SECRET_LENGTH; i++) {
            builder.append(ABC.charAt(new Random().nextInt(ABC.length())));
        }
        return builder.toString();
    }

    /**
     * Check whenever session secret matches user.
     * @param username Username
     * @param sessionSecret Session
     * @return true
     */
    public boolean sessionCorrect(final String username, final String sessionSecret) {
        User userForSession = userSessions.get(sessionSecret);

        if (userForSession == null) return false;

        else return userForSession.getUsername().equals(username);
    }
}
