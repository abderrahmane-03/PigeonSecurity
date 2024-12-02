package net.yc.race.track.repository;

import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Season;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends MongoRepository<Season, String> {
    Optional<Season> findById(String seasonId);
}
