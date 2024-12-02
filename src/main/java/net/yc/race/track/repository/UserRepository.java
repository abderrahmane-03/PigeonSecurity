package net.yc.race.track.repository;

import net.yc.race.track.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    Optional<User>findByLoftName(String loftName);
}