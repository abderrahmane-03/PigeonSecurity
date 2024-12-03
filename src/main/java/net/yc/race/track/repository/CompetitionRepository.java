package net.yc.race.track.repository;

import net.yc.race.track.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    @Query("SELECT c FROM Competition c WHERE c.season.seasonId = :seasonId")
    List<Competition> findBySeasonId(@Param("seasonId") Long seasonId);

}
