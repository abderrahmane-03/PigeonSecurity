package net.yc.race.track.repository;

import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Season;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends MongoRepository<Competition, String> {
    List<Competition> findBySeasonId(String seasonId);

}
