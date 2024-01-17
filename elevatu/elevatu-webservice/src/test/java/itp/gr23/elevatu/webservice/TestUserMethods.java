package itp.gr23.elevatu.webservice;

import itp.gr23.elevatu.api.logic.UserManager;
import itp.gr23.elevatu.protos.ElevatUNetworkProtos;
import itp.gr23.elevatu.webservice.controllers.UserRESTController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.TestTemplate;
import org.springframework.web.server.ResponseStatusException;

public class TestUserMethods {

    private UserManager userManager;
    private UserRESTController restController;
    @BeforeEach
    public void setup(){
        userManager = StaticManagersHacker.getAndSetCurrentUserManager();
        restController = new UserRESTController();
    }

    @Test
    public void checkEmptyUserOrPassword(){
        assertThrows(ResponseStatusException.class, () -> restController.createUser(null, null));
        assertThrows(ResponseStatusException.class, () -> restController.createUser("username", null));
        assertThrows(ResponseStatusException.class, () -> restController.createUser(null, "password"));
    }

    @Test
    public void checkUserCreated() {
        restController.createUser("username", "password");
        assertTrue(userManager.credentialsCorrect("username", "password"));
    }

    @Test
    public void checkUserDuplicate() {
        restController.createUser("test", "test");
        assertThrows(ResponseStatusException.class, () -> restController.createUser("test", "other_pass"));
    }

    @Test
    public void testLogin() {
        restController.createUser("username", "password");

        assertThrows(ResponseStatusException.class, () -> restController.login("username",
                "wrongpassword"));

        ElevatUNetworkProtos.UserSession session = restController.login("username", "password");
        assertEquals(session.getUsername(), "username");
        assertNotNull(session.getSecret());

        assertTrue(userManager.sessionCorrect(session.getUsername(), session.getSecret()));
    }

}
