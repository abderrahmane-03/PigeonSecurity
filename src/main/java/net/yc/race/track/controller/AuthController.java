package net.yc.race.track.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.yc.race.track.DTO.RequestDTO.UserRequest;
import net.yc.race.track.DTO.ResponseDTO.UserResponse;
import net.yc.race.track.Enum.RoleEnum;
import net.yc.race.track.mapper.UserMapper;
import net.yc.race.track.model.Season;
import net.yc.race.track.model.User;
import net.yc.race.track.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class AuthController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) {


        userService.registerUser(
                userRequest
        );
        return ResponseEntity.ok("User registered successfully");
    }

}
