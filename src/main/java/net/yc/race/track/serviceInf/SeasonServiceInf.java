package net.yc.race.track.serviceInf;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Season;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface SeasonServiceInf {
     Season saveSeason(Season season);
     Optional<Season> findSeasonById(Long id);
     List<Map<String, Object>> getBreederRankings(Long seasonId);
     List<Season> findSeasons();
     String deleteSeasonById(Long id);
}
