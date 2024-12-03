package net.yc.race.track.repository;

import net.yc.race.track.model.Result;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByCompetitionId(Long competitionId, Sort sort);

    @Query("SELECT SUM(r.point) FROM Result r WHERE r.pigeon.id = :pigeonId")
    Double getPointsForPigeon(@Param("pigeonId") Long pigeonId);


}
