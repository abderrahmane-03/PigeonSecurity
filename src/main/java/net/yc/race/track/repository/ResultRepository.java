package net.yc.race.track.repository;

import net.yc.race.track.model.Result;
import net.yc.race.track.model.Season;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<Result, Integer> {
    List<Result> findAllByCompetitionId(String competitionId, Sort sort);
    int getPointsForPigeon(int pigeonId);

}
