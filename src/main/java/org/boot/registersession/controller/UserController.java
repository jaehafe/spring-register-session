package org.boot.registersession.controller;

import jakarta.validation.Valid;
import org.boot.registersession.exception.user.UserAuthenticationResponse;
import org.boot.registersession.exception.user.UserLoginRequestBody;
import org.boot.registersession.model.user.User;
import org.boot.registersession.model.user.UserSignUpRequestBody;
import org.boot.registersession.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody
    ) {
        User user = userService.signUp(userSignUpRequestBody);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody userSignUpRequestBody
    ) {
        UserAuthenticationResponse response = userService.authenticate(userSignUpRequestBody);
        return ResponseEntity.ok(response);
    }
}
