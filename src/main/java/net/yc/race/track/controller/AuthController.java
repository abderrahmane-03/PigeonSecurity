package net.yc.race.track.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.yc.race.track.DTO.RequestDTO.UserRequest;
import net.yc.race.track.DTO.ResponseDTO.JwtResponse;
import net.yc.race.track.DTO.ResponseDTO.UserResponse;
import net.yc.race.track.mapper.UserMapper;
import net.yc.race.track.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class AuthController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder; // Inject JwtEncoder

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) {
        userService.registerUser(userRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserRequest userRequest) {
        try {
            // Authenticate the user with the AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(), userRequest.getPassword()));

            // Generate JWT token after successful authentication
            String jwtToken = generateJwtToken(authentication);

            // Return the token to the client
            return ResponseEntity.ok(new JwtResponse(jwtToken));

        } catch (Exception e) {
            // Handle authentication failure
            return ResponseEntity.status(401).body(new JwtResponse("Authentication failed"));
        }
    }

    private String generateJwtToken(Authentication authentication) {
        // Create the JWT claims
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.toList()))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(2))) // Token expiration time
                .build();

        // Wrap the claims in JwtEncoderParameters
        JwtEncoderParameters parameters = JwtEncoderParameters.from(claimsSet);

        // Encode the claims into a JWT token
        return jwtEncoder.encode(parameters).getTokenValue();
    }


}
