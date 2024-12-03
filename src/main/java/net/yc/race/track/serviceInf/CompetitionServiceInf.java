package net.yc.race.track.serviceInf;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Season;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.CompetitionRepository;
import net.yc.race.track.repository.PigeonRepository;
import net.yc.race.track.repository.SeasonRepository;
import net.yc.race.track.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface CompetitionServiceInf {
         String saveCompetition(Competition competition, Long seasonId);
         double calculateDistance(String gps1, String gps2);
         String updatePigeonToCompetition(Competition competition, Long pigeonId);
         List<Competition> findCompetitions();
         Optional<Competition> findCompetitionById(Long id);
         String deleteCompetitionById(Long id);
}
