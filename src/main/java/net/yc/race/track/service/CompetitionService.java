package net.yc.race.track.service;

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
import net.yc.race.track.serviceInf.CompetitionServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetitionService implements CompetitionServiceInf {

    private final PigeonRepository pigeonRepository;

    private final CompetitionRepository competitionRepository;

    private final SeasonRepository seasonRepository;

    private final UserRepository userRepository;

    public String saveCompetition(Competition competition, Long seasonId) {
        Optional<Season> seasonOpt = seasonRepository.findById(seasonId);

        // Check if there is at least one pigeon associated
        if (competition.getPigeons() == null || competition.getPigeons().isEmpty()) {
            return "No pigeon found for the competition.";
        }

        // Get the first pigeon from the list
        Pigeon firstPigeon = competition.getPigeons().get(0);

        // Find the user associated with the pigeon
        Optional<User> userOpt = userRepository.findById(firstPigeon.getUser().getId());

        if (seasonOpt.isPresent() && seasonOpt.get().getStatus() == Status.DONE) {
            return "La saison n'est pas active. Impossible d'enregistrer la compétition.";
        } else if (userOpt.isPresent()) {
            // Calculate the distance
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
    }


    public double calculateDistance(String gps1, String gps2) {
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

    public String updatePigeonToCompetition(Competition competition, Long pigeonId) {
        Date now = new Date();

        // Check if the competition has already started
        if (competition.getStartDateTime().compareTo(now) <= 0) {
            return "La competition est déjà commencée ou terminée. Impossible d'enregistrer le pigeon.";
        }

        // Check if the pigeon is already part of the competition
        boolean pigeonExists = competition.getPigeons().stream()
                .anyMatch(pigeon -> pigeon.getId().equals(pigeonId));

        if (pigeonExists) {
            return "Le pigeon est déjà enregistré pour cette compétition.";
        }

        // Find the pigeon by its ID
        Optional<Pigeon> pigeonOpt = pigeonRepository.findById(pigeonId);
        if (pigeonOpt.isPresent()) {
            Pigeon pigeon = pigeonOpt.get();
            competition.getPigeons().add(pigeon);
            pigeon.setCompetition(competition); // Associate the pigeon with the competition
            competitionRepository.save(competition);
            return "Pigeon ajouté avec succès.";
        } else {
            return "Pigeon introuvable.";
        }
    }

    public List<Competition> findCompetitions() {
        return competitionRepository.findAll();
    }

    public Optional<Competition> findCompetitionById(Long id) {
        return competitionRepository.findById(id);
    }

    public String deleteCompetitionById(Long id) {
        if (competitionRepository.existsById(id)) {
            competitionRepository.deleteById(id);
            return "Competition supprimé avec succès.";
        } else {
            return "Competition non trouvé.";
        }
    }
}
