package net.yc.race.track.repository;

import net.yc.race.track.model.Pigeon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PigeonRepository extends MongoRepository<Pigeon, Integer> {


    boolean existsByNumeroDeBadge(String generatedBadge);
}
