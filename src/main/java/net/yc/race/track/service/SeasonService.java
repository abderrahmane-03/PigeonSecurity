package net.yc.race.track.service;

import net.yc.race.track.model.*;
import net.yc.race.track.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeasonService {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private PigeonRepository pigeonRepository;
    @Autowired
    private UserRepository userRepository;

    public Season saveSeason(Season season) {
        return seasonRepository.save(season);
    }

    public Optional<Season> findSeasonById(String id) {
        return seasonRepository.findById(id);
    }

    public List<Map<String, Object>> getBreederRankings(String seasonId) {
        // Fetch competitions for the season
        List<Competition> competitions = competitionRepository.findBySeasonId(seasonId);

        // Map to store total points by user
        Map<String, Integer> userPointsMap = new HashMap<>();

        // Process each competition
        for (Competition competition : competitions) {
            for (Integer pigeonId : competition.getPigeonId()) {
                // Fetch pigeon details
                Optional<Pigeon> pigeonOpt = pigeonRepository.findById(pigeonId);
                if (pigeonOpt.isPresent()) {
                    Pigeon pigeon = pigeonOpt.get();

                    // Simulate fetching points for the pigeon from results
                    int pigeonPoints = resultRepository.getPointsForPigeon(pigeonId);

                    // Add to user's total
                    userPointsMap.merge(pigeon.getUser_id(), pigeonPoints, Integer::sum);
                }
            }
        }

        // Create rankings
        return userPointsMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> {
                    String userId = entry.getKey();
                    int totalPoints = entry.getValue();
                    Optional<User> userOpt = userRepository.findById(userId);

                    // Prepare the response data for each user
                    Map<String, Object> userRanking = new HashMap<>();
                    userRanking.put("userId", userId);
                    userRanking.put("username", userOpt.map(User::getUsername).orElse("Unknown"));
                    userRanking.put("totalPoints", totalPoints);
                    return userRanking;
                })
                .collect(Collectors.toList());
    }

    public List<Season> findSeasons() {
        return seasonRepository.findAll();
    }

    public String deleteSeasonById(String id) {
        if (seasonRepository.existsById(id)) {
            seasonRepository.deleteById(id);
            return "Season supprimé avec succès.";
        } else {
            return "Season non trouvé.";
        }
    }
}
