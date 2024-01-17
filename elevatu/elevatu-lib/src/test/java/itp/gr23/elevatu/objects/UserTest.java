package itp.gr23.elevatu.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    final String testUsername = "testUser";
    final String testPassword = "testPassword";

    @Test
    public void testPasswordCheck(){
        User testUser = User.createFromPlaintextPassword(testUsername, testPassword);

        assertTrue(testUser.passwordCorrect(testPassword));
    }

    @Test
    public void testHashToStringConversion(){
        User testUser = User.createFromPlaintextPassword(testUsername, testPassword);

        String encodedHash = testUser.getHashString();
        String encodedSalt = testUser.getSaltString();

        User convertedUser = new User(testUsername, encodedHash, encodedSalt);

        assertTrue(convertedUser.passwordCorrect(testPassword));
    }

    @Test
    public void testUserEquals() {
        User u1 = User.createFromPlaintextPassword("lorem", "ipsum");
        User u2 = User.createFromPlaintextPassword("lorem", "changed-password");

        assertEquals(u1.hashCode(), u2.hashCode());
        assertEquals(u1, u2);
    }
}
