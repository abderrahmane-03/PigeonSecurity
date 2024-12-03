package net.yc.race.track.serviceInf;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserServiceInf {

     User registerUser(User user);
     Optional<User> authenticate(String username, String password);
}