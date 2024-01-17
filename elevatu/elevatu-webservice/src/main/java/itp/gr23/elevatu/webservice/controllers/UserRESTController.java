package itp.gr23.elevatu.webservice.controllers;

import itp.gr23.elevatu.api.exceptions.DuplicateIDException;
import itp.gr23.elevatu.objects.User;
import itp.gr23.elevatu.protos.ElevatUNetworkProtos;
import itp.gr23.elevatu.webservice.ElevatUService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/userapi")
public final class UserRESTController {

    /**
     * Create new user.
     * @param username Username
     * @param password Password
     * @return Response
     */
    @PostMapping(value = "/users/createUser")
    public String createUser(@RequestParam final String username,
                             @RequestParam final String password) {
        if (password == null || username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password not provided.");
        }
        try {
            ElevatUService.getUserManager().createUser(username, password);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists.");
        }
        return "User created.";
    }

    /**
     * Logs an user in.
     * @param username Username
     * @param password Password
     * @return Whenever login is success or not.
     */
    @PostMapping(value = "/users/login")
    public ElevatUNetworkProtos.UserSession login(@RequestParam final String username,
                                                  @RequestParam final String password) {
        if (!ElevatUService.getUserManager().credentialsCorrect(username, password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The username or password provided is wrong.");
        } else {
            try {
                User user = ElevatUService.getUserManager().getUser(username);
                return ElevatUNetworkProtos.UserSession.newBuilder()
                        .setUsername(user.getUsername())
                        .setSecret(ElevatUService.getUserManager().createNewSession(user))
                        .build();
            } catch (DuplicateIDException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate user in database.");
            }
        }
    }
}
