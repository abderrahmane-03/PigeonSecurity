package net.yc.race.track.service;

import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Season;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.CompetitionRepository;
import net.yc.race.track.repository.PigeonRepository;
import net.yc.race.track.repository.SeasonRepository;
import net.yc.race.track.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {
    @Autowired
    private PigeonRepository pigeonRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private UserRepository userRepository;

    public String saveCompetition(Competition competition, String seasonId) {
        Optional<Season> seasonOpt = seasonRepository.findById(seasonId);

        // Check if there is at least one pigeon ID
        if (competition.getPigeonId().isEmpty()) {
            return "No pigeon found for the competition.";
        }

        int firstPigeonId = competition.getPigeonId().get(0);
        Optional<Pigeon> firstPigeonOpt = pigeonRepository.findById(firstPigeonId);
        if (firstPigeonOpt.isPresent()) {
            Pigeon firstPigeon = firstPigeonOpt.get();
            Optional<User> userOpt = userRepository.findById(firstPigeon.getUser_id());

            if (seasonOpt.isPresent() && seasonOpt.get().getStatus() == Status.DONE) {
                return "La saison n'est pas active. Impossible d'enregistrer la compétition.";
            } else if (userOpt.isPresent()) {
                double distance = calculateDistance(competition.getCoordinatesGPS(), userOpt.get().getGpsCoordinates());

                if (Math.abs(competition.getDistance() - distance) <= 5) {
                    competitionRepository.save(competition);
                    return "Compétition enregistrée avec succès.";
                } else {
                    return "Competition is out of your range.";
                }
            } else {
                return "User not found.";
            }
        } else {
            return "First pigeon not found.";
        }
    }


    private double calculateDistance(String gps1, String gps2) {
        String[] coordinates1 = gps1.split(",");
        String[] coordinates2 = gps2.split(",");

        double lat1 = Math.toRadians(Double.parseDouble(coordinates1[0]));
        double lon1 = Math.toRadians(Double.parseDouble(coordinates1[1]));
        double lat2 = Math.toRadians(Double.parseDouble(coordinates2[0]));
        double lon2 = Math.toRadians(Double.parseDouble(coordinates2[1]));

        final int EARTH_RADIUS = 6371;

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public String updatePigeonToCompetition(Competition competition, Integer pigeonId) {
        Date now = new Date();

        if (competition.getStartDateTime().compareTo(now) <= 0) {
            return "La competition est déjà commencée ou terminée. Impossible d'enregistrer le pigeon.";
        } else {
            if (!competition.getPigeonId().contains(pigeonId)) {
                competition.getPigeonId().add(pigeonId);
                competitionRepository.save(competition);
                return "Pigeon ajouté avec succès.";
            } else {
                return "Le pigeon est déjà enregistré pour cette compétition.";
            }
        }
    }

    public List<Competition> findCompetitions() {
        return competitionRepository.findAll();
    }

    public Optional<Competition> findCompetitionById(String id) {
        return competitionRepository.findById(id);
    }

    public String deleteCompetitionById(String id) {
        if (competitionRepository.existsById(id)) {
            competitionRepository.deleteById(id);
            return "Competition supprimé avec succès.";
        } else {
            return "Competition non trouvé.";
        }
    }
}
