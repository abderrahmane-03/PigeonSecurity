package net.yc.race.track.serviceInf;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.PigeonRepository;
import net.yc.race.track.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface PigeonServiceInf {

     String savePigeon(Pigeon pigeon, Long userId);
     Optional<Pigeon> findPigeonById(Long pigeonId);
     String generateUniqueBadge(Pigeon pigeon);
     List<Pigeon> findPigeons();
     String deletePigeonById(Long id);
}
