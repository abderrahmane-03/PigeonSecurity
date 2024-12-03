package net.yc.race.track.controller;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.User;
import net.yc.race.track.service.UserService;
import net.yc.race.track.serviceInf.UserServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")

public class AuthController {

    private final UserServiceInf userServiceinf;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userServiceinf.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<User> user = userServiceinf.authenticate(username, password);

        if (user.isPresent()) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }



}