package com.codebrewers.backend.controller;

import com.codebrewers.backend.dao.User;
import com.codebrewers.backend.dao.LoginRequest;
import com.codebrewers.backend.dao.RegistrationResponse;
import com.codebrewers.backend.dao.LoginResponse;
import com.codebrewers.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            RegistrationResponse response = new RegistrationResponse(true, user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(false, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            LoginResponse response = new LoginResponse(true, user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(false, null));
        }
    }

}
