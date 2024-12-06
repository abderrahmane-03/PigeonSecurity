package net.yc.race.track.service;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.DTO.RequestDTO.UserRequest;
import net.yc.race.track.DTO.ResponseDTO.UserResponse;
import net.yc.race.track.Enum.RoleEnum;
import net.yc.race.track.exceptions.UsernameAlreadyExistsException;
import net.yc.race.track.mapper.UserMapper;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.UserRepository;
import net.yc.race.track.serviceInf.UserServiceInf;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInf {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserResponse registerUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new UsernameAlreadyExistsException("Username '" + userRequest.getUsername() + "' already exists.");
        }

        userRequest.setRole(RoleEnum.ROLE_BREEDER);
        User user = userMapper.toEntityD(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);


//        User user = new User();
//        user.setUsername(userRequest.getUsername());
//        user.setLoftName(userRequest.getLoftName());
//        user.setGpsCoordinates(userRequest.getGpsCoordinates());
//        user.setRole(userRequest.getRole());
//        userRepository.save(user);

    }

    @Override
    public void changeRole(Long userId, RoleEnum roleEnum) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setRole(roleEnum);
        userRepository.save(user);
    }

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}
