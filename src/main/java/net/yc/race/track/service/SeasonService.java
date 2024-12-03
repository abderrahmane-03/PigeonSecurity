package net.yc.race.track.service;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.*;
import net.yc.race.track.repository.*;
import net.yc.race.track.serviceInf.SeasonServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeasonService implements SeasonServiceInf {


    private final ResultRepository resultRepository;

    private final SeasonRepository seasonRepository;

    private final CompetitionRepository competitionRepository;

    private final PigeonRepository pigeonRepository;

    private final UserRepository userRepository;

    public Season saveSeason(Season season) {
        return seasonRepository.save(season);
    }

    public Optional<Season> findSeasonById(Long id) {
        return seasonRepository.findById(id);
    }

    public List<Map<String, Object>> getBreederRankings(Long seasonId) {
        // Fetch competitions for the given season
        List<Competition> competitions = competitionRepository.findBySeasonId(seasonId);

        // Map to store total points by user
        Map<Long, Double> userPointsMap = new HashMap<>();

        // Process each competition
        for (Competition competition : competitions) {
            for (Pigeon pigeon : competition.getPigeons()) {
                Long pigeonId = pigeon.getId();

                // Fetch points for the pigeon
                Double pigeonPoints = resultRepository.getPointsForPigeon(pigeonId);
                if (pigeonPoints == null) {
                    pigeonPoints = 0.0; // Handle null values
                }

                // Add points to the user's total
                userPointsMap.merge(pigeon.getUser().getId(), pigeonPoints, Double::sum);
            }
        }

        // Create rankings
        return userPointsMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(entry -> {
                    Long userId = entry.getKey();
                    double totalPoints = entry.getValue();

                    // Fetch user details
                    Optional<User> userOpt = userRepository.findById(userId);
                    String username = userOpt.map(User::getUsername).orElse("Unknown");

                    // Prepare response data
                    Map<String, Object> userRanking = new HashMap<>();
                    userRanking.put("userId", userId);
                    userRanking.put("username", username);
                    userRanking.put("totalPoints", totalPoints);

                    return userRanking;
                })
                .collect(Collectors.toList());
    }



    public List<Season> findSeasons() {
        return seasonRepository.findAll();
    }

    public String deleteSeasonById(Long id) {
        if (seasonRepository.existsById(id)) {
            seasonRepository.deleteById(id);
            return "Season supprimé avec succès.";
        } else {
            return "Season non trouvé.";
        }
    }
}
