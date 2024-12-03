package net.yc.race.track.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Result;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.CompetitionRepository;
import net.yc.race.track.repository.ResultRepository;
import net.yc.race.track.repository.UserRepository;
import net.yc.race.track.serviceInf.ResultServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResultService implements ResultServiceInf {


    private final ResultRepository resultRepository;


    private final CompetitionRepository competitionRepository;



    private final UserRepository userRepository;


    private final PdfExportService pdfExportService;
 


    public List<Result> showResult(Long competitionId) {
        // Fetch all results for the competition
        List<Result> results = resultRepository.findAllByCompetitionId(competitionId,Sort.by(Sort.Order.desc("speed")));

        // Calculate the average distance traveled by all pigeons in the competition
        double totalDistance = results.stream()
                .mapToDouble(Result::getDistance)
                .sum();
        double averageDistance = totalDistance / results.size();

        // Adjust speed based on the distance coefficient
        results.forEach(result -> {
            double adjustmentCoefficient = averageDistance / result.getDistance();
            double adjustedSpeed = result.getSpeed() * adjustmentCoefficient;
            result.setAdjustedSpeed(adjustedSpeed);
        });

        // Sort results by adjusted speed in descending order
        results.sort((r1, r2) -> Double.compare(r2.getAdjustedSpeed(), r1.getAdjustedSpeed()));

        // Calculate the number of top players (top 25%)
        int totalPlayers = results.size();
        int top25PercentCount = (int) Math.ceil(totalPlayers * 0.25);

        // Get the top 25% results based on adjusted speed
        List<Result> topResults = results.subList(0, Math.min(top25PercentCount, totalPlayers));

        // Set initial points and calculate point difference
        double topPoints = 100.0;
        double pointDifference = (topResults.size() > 1) ? topPoints / (topResults.size()) : 0;

        // Assign ranks and points to top results
        for (int i = 0; i < topResults.size(); i++) {
            Result result = topResults.get(i);
            int rank = i + 1;
            result.setRank(rank);

            double points = topPoints - (rank - 1) * pointDifference;
            result.setPoint(Math.max(points, 0)); // Ensure points are non-negative
        }

        // Save updated results to the database
        resultRepository.saveAll(results);

        return topResults;
    }





    public String saveResult(Result result) {
        // Fetch Competition and User based on relationships
        Competition competition = result.getCompetition(); // Use the `Competition` object directly
        User user = userRepository.findByLoftName(result.getLoftName()).orElse(null);

        if (competition != null && user != null) {
            // Ensure both competition and user have GPS coordinates
            if (competition.getCoordinatesGPS() == null || user.getGpsCoordinates() == null) {
                return "GPS coordinates for competition or user are missing.";
            }

            // Calculate competition end time by adding delay to start time
            Instant startTime = competition.getStartDateTime().toInstant();
            Instant delayDuration = competition.getDelayDuration().toInstant();
            Instant endTime = startTime.plus(delayDuration.toEpochMilli(), ChronoUnit.MILLIS);

            // Check if the current time is after the end time
            Instant now = Instant.now();
            if (now.isAfter(endTime)) {
                return "The competition delay duration has already passed. Result cannot be added.";
            }

            // Calculate distance using GPS coordinates
            double distance = calculateDistance(
                    competition.getCoordinatesGPS(),
                    user.getGpsCoordinates()
            );
            result.setDistance(distance);

            // Calculate elapsed time in minutes
            Date arriveHour = result.getArriveHour();
            long timeElapsedMinutes = Duration.between(startTime, arriveHour.toInstant()).toMinutes();

            // Calculate speed in m/min
            result.setSpeed(timeElapsedMinutes > 0 ? (distance * 1000) / timeElapsedMinutes : 0);

            // Save result
            resultRepository.save(result);
            return "Result enregistré avec succès.";
        } else {
            return "Competition or User not found.";
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

    public List<Result> findResults(){
        return resultRepository.findAll();
    }


    public String deleteResultById(Long id) {
        if (resultRepository.existsById(id)) {
            resultRepository.deleteById(id);
            return "Result supprimé avec succès.";
        } else {
            return "Result non trouvé.";
        }
    }

    public String exportResultsToPdf(Long competitionId, String outputPath) {
        // Fetch competition results, sorted by speed in descending order
        List<Result> results = resultRepository.findAllByCompetitionId(competitionId, Sort.by(Sort.Order.desc("speed")));

        if (results == null || results.isEmpty()) {
            return "Aucun résultat trouvé pour la compétition avec l'ID : " + competitionId;
        }

        // Filter out results with rank = 0
        List<Result> filteredResults = results.stream()
                .filter(result -> result.getRank() > 0)
                .toList();

        if (filteredResults.isEmpty()) {
            return "Aucun résultat valide (rang > 0) trouvé pour la compétition avec l'ID : " + competitionId;

        }


        try {
            // Call the PDF export service to generate the PDF
            pdfExportService.exportResultsToPdf(filteredResults, outputPath);
            return "Résultats exportés avec succès vers " + outputPath;
        } catch (Exception e) {
            return "Erreur lors de l'exportation des résultats : " + e.getMessage();
        }
    }


}
