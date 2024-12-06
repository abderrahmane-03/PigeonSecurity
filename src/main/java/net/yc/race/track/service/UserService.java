package net.yc.race.track.service;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.DTO.RequestDTO.UserRequest;
import net.yc.race.track.DTO.ResponseDTO.UserResponse;
import net.yc.race.track.Enum.RoleEnum;
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
        Optional<User> byUsername = userRepository.findByUsername(userRequest.getUsername());

        if (byUsername.isPresent()){
            throw new IllegalArgumentException("this username already exist.");
        }
        if (userRequest.getRole() == RoleEnum.ROLE_BREEDER) {

            if (userRequest.getLoftName() == null || userRequest.getLoftName().isBlank()) {
                throw new IllegalArgumentException("Loft name is required for Breeders.");
            }
            if (userRequest.getGpsCoordinates() == null || userRequest.getGpsCoordinates().isBlank()) {
                throw new IllegalArgumentException("GPS coordinates are required for Breeders.");
            }
        }
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

    public void changeRole(Long userId,RoleEnum roleEnum){
        Optional<User> byId = userRepository.findById(userId);
        if(byId.isPresent()){
         byId.orElse(new User()).setRole(roleEnum);
        }
    }

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}
