package itp.gr23.elevatu.objects;


import itp.gr23.elevatu.protos.ElevatUProtos;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
public final class User {
    private final String username;
    private final byte[] hash;
    private final byte[] salt;

    private static final int SALT_BYTE_ARRAY_SIZE = 16;


    /**
     * Constructor for User object using Base64-encoded strings.
     * @param username Username
     * @param hashString Password hash as Base64-encoded String
     * @param saltString Password salt as Base64-encoded String
     */
    public User(final String username, final String hashString, final String saltString) {
        this(username, Base64.getDecoder().decode(hashString), Base64.getDecoder().decode(saltString));
    }

    /**
     * Constructor for User object using byte[] for password hash and salt.
     * @param username Username
     * @param hash Password hash
     * @param salt Password salt
     */
    public User(final String username, final byte[] hash, final byte[] salt) {
        this.username = username;
        this.hash = hash;
        this.salt = salt;
    }

    /**
     * toString username.
     * @return Stringed username.
    */
    @Override
    public String toString() {
        return username;
    }

    // Equal on equal usernames.
    // User can for example change password, and should still be considered equal.
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Gets the username.
     * @return Username
     */
    public String getUsername() {
        return username;
    }
    private byte[] getHash() {
        return hash;
    }
    private byte[] getSalt() {
        return salt;
    }

    /**
     * Gets the password hash as Base64 encoded String.
     * @return Base64-encoded password hash String
     */
    public String getHashString() {
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Gets the password salt as Base64 encoded String.
     * @return Base64-encoded password salt String
     */
    public String getSaltString() {
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Check whenever password is correct for User.
     * @param password Password to check
     * @return true whenever password is correct for user
     */
    public boolean passwordCorrect(final String password) {
        byte[] testedPasswordHash = createPasswordHash(getSalt(), password);

        return Arrays.equals(testedPasswordHash, getHash());
    }

    /**
     * Create new User object from username and password formatted as String.
     * Two objects created with this methods will not be equal, as the salt used
     * for password encoding is random.
     * @param username Username
     * @param password Password
     * @return User object with supplied credentials
     */
    public static User createFromPlaintextPassword(final String username, final String password) {
        byte[] salt = createSalt();
        byte[] hash = createPasswordHash(salt, password);

        return new User(username, hash, salt);
    }

    /**
     * Create hash from supplied password and salt.
     * @param salt Salt
     * @param password Password
     * @return Password hash
     */
    private static byte[] createPasswordHash(final byte[] salt, final String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not find SHA-512 alghoritm. Failed to create hash.");
            e.printStackTrace();
        }
        return null; // TODO - handling this case??
    }

    /**
     * Creates random salt for use with password hash generation.
     * @return Randomly generated salt
     */
    private static byte[] createSalt() {
        SecureRandom sr = new SecureRandom();

        byte[] salt = new byte[SALT_BYTE_ARRAY_SIZE];
        sr.nextBytes(salt);

        return salt;
    }

    /**
     * Creates protobuf instance of User.
     * @param user User object
     * @return Protobuf User object
     */
    public static ElevatUProtos.User toProto(final User user) {
        return ElevatUProtos.User.newBuilder()
                .setUsername(user.username)
                .setPasswordHash(user.getHashString())
                .setPasswordSalt(user.getSaltString())
                .build();
    }

    /**
     * Create User object from protobuf instance.
     * @param protoUser Protobuf user object
     * @return User object
     */
    public static User fromProto(final ElevatUProtos.User protoUser) {
        return new User(protoUser.getUsername(), protoUser.getPasswordHash(), protoUser.getPasswordSalt());
    }

}
