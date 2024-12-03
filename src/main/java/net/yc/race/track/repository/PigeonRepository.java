package net.yc.race.track.repository;

import net.yc.race.track.model.Pigeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PigeonRepository extends JpaRepository<Pigeon, Long> {

    boolean existsByNumeroDeBadge(String numeroDeBadge);
}
