package net.yc.race.track.service;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.UserRepository;
import net.yc.race.track.serviceInf.UserServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInf {


    private final UserRepository userRepository;


    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }
}